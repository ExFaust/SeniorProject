package com.exfaust.core_android.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.exfaust.core.AnalyticManager
import com.exfaust.core.analytic.Analytics
import com.exfaust.core.lazyGet
import com.exfaust.core.rx.invoke
import com.exfaust.core.rx.ofSubtype
import com.exfaust.core.rx.onBackpressureBufferLast
import com.exfaust.core.unreachable
import com.exfaust.core_android.FragmentLifecycleEvent
import com.exfaust.core_android.NavigateBackHandler
import com.exfaust.core_android.R
import com.exfaust.core_android.bundler.Bundler
import com.exfaust.core_android.di.DependencyBuilder
import com.exfaust.core_android.di.basicActivityModule
import com.exfaust.core_android.di.bindNavigationFromContainer
import com.exfaust.core_android.inflateInto
import com.exfaust.core_android.lifecycle.LifecycleResourcesScopeOwner
import com.exfaust.core_android.lifecycle.ResourcesScope
import com.exfaust.core_android.lifecycle.createResourcesScope
import com.exfaust.core_android.onFragmentLifecycleChanged
import com.exfaust.core_android.toolbar.HasCustomDynamicToolbar
import com.exfaust.core_android.toolbar.HasCustomToolbar
import com.exfaust.core_android.toolbar.HasCustomToolbar.CustomToolbar
import com.exfaust.core_android.toolbar.HasCustomToolbar.CustomToolbarStyle
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.common___scaffold.*
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentSet
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.android.subDI
import org.kodein.di.instance
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import javax.annotation.CheckReturnValue

abstract class BaseActivity(
    @LayoutRes val contentId: Int? = null,
    val moduleName: String,
    @IdRes val navigationRes: Int? = null,
    dependency: DependencyBuilder<AppCompatActivity> = {}
) : AppCompatActivity(), DIAware, LifecycleResourcesScopeOwner {

    protected val navigation: NavController by instance()
    protected val analyticManager: AnalyticManager by instance()
    protected val bundler: Bundler by instance()

    private val _navigationConfiguration: AppBarConfiguration by lazyGet {
        AppBarConfiguration(topLevelDestinationIds)
    }
    protected abstract val topLevelDestinationIds: PersistentSet<Int>

    protected abstract val defaultCustomToolbar: HasCustomToolbar

    private val _onNavigateBack = PublishProcessor.create<Unit>()
    private var _navigateBackHandlers = persistentSetOf<NavigateBackHandler>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableCustomToolbarSupport().scopedByDestroy()

        setContentView(R.layout.common___scaffold)

        _onToolbarContainersChanged(
            ToolbarContainers(
                shift = common___scaffold__toolbar__shift__container,
                overlap = common___scaffold__toolbar__overlap__container,
                shiftTransparent = common___scaffold__toolbar__shift__transparent_container
            )
        )

        if (contentId !== null) {
            layoutInflater.inflateInto(contentId, common___scaffold__container)
        }

        // Reset navigator state.
        if (savedInstanceState !== null && navigationRes != null) {
            navigation.popBackStack(navigation.graph.startDestinationId, false)
        }

        _onNavigateBack
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                val handlerResults = _navigateBackHandlers
                    .asSequence()
                    .map { it.onNavigateBack() }
                    .toPersistentSet()

                if (NavigateBackHandler.Result.Exit in handlerResults) {
                    finishAfterTransition()
                } else if (NavigateBackHandler.Result.Consumed in handlerResults) {
                    return@subscribeBy
                } else if (navigationRes == null || !NavigationUI.navigateUp(
                        navigation,
                        _navigationConfiguration
                    )
                ) {
                    finishAfterTransition()
                }
            }.scopedByDestroy()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                analyticManager.reportEvent(Analytics.System.clickBack)
                _onNavigateBack.invoke()
            }
        })

        supportFragmentManager
            .onFragmentLifecycleChanged(recursive = true)
            .ofSubtype<FragmentLifecycleEvent.Resumed>()
            .map { it.fragment }
            .ofType<NavigateBackHandler>()
            .subscribeBy {
                _navigateBackHandlers = _navigateBackHandlers.add(it)
            }
            .scopedByDestroy()

        supportFragmentManager
            .onFragmentLifecycleChanged(recursive = true)
            .ofSubtype<FragmentLifecycleEvent.Paused>()
            .map { it.fragment }
            .ofType<NavigateBackHandler>()
            .subscribeBy {
                _navigateBackHandlers = _navigateBackHandlers.remove(it)
            }
            .scopedByDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        _onNavigateBack()

        return true
    }

    final override val di: DI by subDI(closestDI()) {
        importOnce(basicActivityModule(this@BaseActivity), allowOverride = true)

        if (navigationRes != null)
            bindNavigationFromContainer(this@BaseActivity, navigationRes)

        dependency(this@BaseActivity)
    }

    fun setupTopInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            v.updatePadding(top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top)
            insets
        }
        updateInsets(view)
    }

    private fun clearTopInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            v.updatePadding(top = 0)
            insets
        }
        updateInsets(view)
    }

    private fun switchToLightTheme() {
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
    }

    private fun switchToDarkTheme() {
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
    }

    private fun updateInsets(view: View) {
        view.apply {
            if (isAttachedToWindow) {
                requestApplyInsets()
            } else {
                addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                    override fun onViewAttachedToWindow(v: View) {
                        v.removeOnAttachStateChangeListener(this)
                        v.requestApplyInsets()
                    }

                    override fun onViewDetachedFromWindow(v: View) = Unit
                })
            }
        }
    }

    final override val onCreateResourcesScope: ResourcesScope by lazyGet {
        lifecycle.createResourcesScope(Lifecycle.Event.ON_CREATE)
    }
    override val onStartResourcesScope: ResourcesScope by lazyGet {
        lifecycle.createResourcesScope(Lifecycle.Event.ON_START)
    }
    final override val onResumeResourcesScope: ResourcesScope by lazyGet {
        lifecycle.createResourcesScope(Lifecycle.Event.ON_RESUME)
    }
    final override val onPauseResourcesScope: ResourcesScope by lazyGet {
        lifecycle.createResourcesScope(Lifecycle.Event.ON_PAUSE)
    }
    final override val onStopResourcesScope: ResourcesScope by lazyGet {
        lifecycle.createResourcesScope(Lifecycle.Event.ON_STOP)
    }
    final override val onDestroyResourcesScope: ResourcesScope by lazyGet {
        lifecycle.createResourcesScope(Lifecycle.Event.ON_DESTROY)
    }

    private class ToolbarContainers(
        val shift: ViewGroup,
        val overlap: ViewGroup,
        val shiftTransparent: ViewGroup
    ) {
        val all: PersistentList<ViewGroup> = persistentListOf(
            shift,
            overlap,
            shiftTransparent
        )

        @CheckReturnValue
        fun getByStyle(style: CustomToolbarStyle): ViewGroup =
            when (style) {
                CustomToolbarStyle.ShiftContent -> shift
                CustomToolbarStyle.OverlapContent -> overlap
                CustomToolbarStyle.OverlapContentWithTransparentSystemWindow -> overlap
                CustomToolbarStyle.ShiftTransparent -> shiftTransparent
            }
    }

    private val _onToolbarContainersChanged: PublishSubject<ToolbarContainers> =
        PublishSubject.create()

    @CheckReturnValue
    private fun enableCustomToolbarSupport(): Disposable {
        val updateActionBarSubscription: AtomicReference<Disposable?> = AtomicReference(null)
        val fragmentsWithToolbar: AtomicReference<PersistentList<HasCustomToolbar>> =
            AtomicReference(persistentListOf())

        return supportFragmentManager
            .onFragmentLifecycleChanged(recursive = true)
            .observeOn(Schedulers.computation())
            .filter {
                (it is FragmentLifecycleEvent.Started && it.fragment is HasCustomToolbar)
                    || (it is FragmentLifecycleEvent.Stopped && it.fragment is HasCustomToolbar)
            }
            .map { event ->
                when (event) {
                    is FragmentLifecycleEvent.Started -> fragmentsWithToolbar.updateAndGet {
                        it.add(event.fragment as HasCustomToolbar)
                    }
                    is FragmentLifecycleEvent.Stopped -> fragmentsWithToolbar.updateAndGet {
                        it.remove(event.fragment as HasCustomToolbar)
                    }
                    else -> unreachable()
                }.lastOrNull() ?: defaultCustomToolbar
            }
            .startWith(defaultCustomToolbar)
            .distinctUntilChanged()
            .withLatestFrom(_onToolbarContainersChanged) { host, containers ->
                Pair(
                    host,
                    containers
                )
            }
            .toFlowable(BackpressureStrategy.MISSING)
            .onBackpressureBufferLast(AndroidSchedulers.mainThread())
            .subscribeBy { (host, containers) ->
                updateActionBarSubscription.getAndSet(null)?.dispose()

                setSupportActionBar(null)

                val customToolbar: CustomToolbar = host.onCreateCustomToolbar()

                val selectedContainer: ViewGroup? = when (customToolbar) {
                    CustomToolbar.None -> null
                    is CustomToolbar.Layout -> {
                        containers.getByStyle(customToolbar.style)
                    }
                }

                // Когда нет акшионбара, отступы для статус бара добавляются к контейнеру с контентом,
                // когда есть, у контента оступ следует убрать и оставить у акшионбара.
                selectedContainer?.let {
                    setupTopInsets(it)
                    if (!(customToolbar is CustomToolbar.Layout
                            && customToolbar.style == CustomToolbarStyle.OverlapContent)
                    )
                        clearTopInsets(common___scaffold__container)
                    else
                        setupTopInsets(common___scaffold__container)

                    //изменяет тему статус бара в зависимости от стиля тулбара
                    if (customToolbar is CustomToolbar.Layout && customToolbar.style == CustomToolbarStyle.OverlapContentWithTransparentSystemWindow) {
                        switchToDarkTheme()
                    } else
                        switchToLightTheme()

                } ?: setupTopInsets(common___scaffold__container)

                for (container in containers.all) {
                    container.isVisible = container === selectedContainer
                    container.removeAllViews()
                }

                when (customToolbar) {
                    CustomToolbar.None -> Unit
                    is CustomToolbar.Layout -> {
                        selectedContainer ?: unreachable()

                        val view: View = customToolbar.onCreateView(selectedContainer)

                        val toolbar = customToolbar.getToolbar(view)
                        val appBarLayout = customToolbar.getAppBarLayout(view)

                        selectedContainer.addView(appBarLayout ?: toolbar)

                        setSupportActionBar(toolbar)

                        if (navigationRes != null)
                            toolbar.setupWithNavController(navigation, _navigationConfiguration)

                        toolbar.setNavigationOnClickListener { _onNavigateBack() }

                        if (host is HasCustomDynamicToolbar<*>) {
                            host
                                .onCustomToolbarChanged()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeBy { event ->
                                    @Suppress("UNCHECKED_CAST")
                                    (host as HasCustomDynamicToolbar<Any?>)
                                        .onUpdateCustomToolbar(event, appBarLayout, toolbar)
                                }
                                .also { updateActionBarSubscription.getAndSet(it)?.dispose() }
                                .scopedByDestroy()
                        }
                    }
                }
            }
    }

    protected fun showMessage(@StringRes messageRes: Int) {
        Toast.makeText(this, messageRes, Toast.LENGTH_LONG).show()
    }

    protected fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}