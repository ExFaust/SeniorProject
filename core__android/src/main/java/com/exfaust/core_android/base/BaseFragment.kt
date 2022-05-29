package com.exfaust.core_android.base

import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.exfaust.core.AnalyticManager
import com.exfaust.core.analytic.Analytics
import com.exfaust.core.lazyGet
import com.exfaust.core.unreachable
import com.exfaust.core_android.R
import com.exfaust.core_android.di.DependencyBuilder
import com.exfaust.core_android.di.basicFragmentModule
import com.exfaust.core_android.lifecycle.LifecycleResourcesScopeOwner
import com.exfaust.core_android.lifecycle.createResourcesScope
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.subDI
import org.kodein.di.android.x.closestDI
import org.kodein.di.instance
import timber.log.Timber

abstract class BaseFragment(
    dependency: DependencyBuilder<BaseFragment> = {},
    private val screenName: String
) : Fragment(),
    DIAware, LifecycleResourcesScopeOwner {
    final override val di: DI by subDI(closestDI()) {
        dependency(this@BaseFragment)

        importOnce(basicFragmentModule(this@BaseFragment), allowOverride = true)
    }

    protected val analyticManager: AnalyticManager by instance()
    protected val navigation: NavController by instance()

    protected fun eventsObserver(progressView: View? = null): Observer<Events> {
        return Observer<Events> { event ->
            when (event) {
                is Events.Error -> {
                    Timber.e(event.throwable)
                    event.getMessage(requireContext(), event.throwable)?.let { showMessage(it) }
                    progressView?.visibility = View.GONE
                }
                is Events.ErrorWithMessage -> {
                    showMessage(event.message)
                    progressView?.visibility = View.GONE
                }
                Events.Unauthorized -> {
                    unreachable()
                }
                Events.NoConnectivity -> {
                    showMessage(getString(R.string.application___error__no_internet_connection))
                    progressView?.visibility = View.GONE
                }
                Events.Loading -> {
                    progressView?.visibility = View.VISIBLE
                }
                Events.HideLoading -> {
                    progressView?.visibility = View.GONE
                }
                is Events.ErrorWithExit -> {
                    Timber.e(event.throwable)
                    event.getMessage(requireContext(), event.throwable)?.let { showMessage(it) }
                    activity?.onBackPressed()
                }
                Events.Exit -> {
                    activity?.onBackPressed()
                }
            }
        }
    }

    protected fun showMessage(
        message: String
    ) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showMessage(
        @StringRes
        message: Int
    ) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()

        (activity as? BaseActivity)?.moduleName?.let { analyticManager.reportEvent(it + Analytics.System.show + screenName) }
    }

    override fun onPause() {
        super.onPause()

        (activity as? BaseActivity)?.moduleName?.let { analyticManager.reportEvent(it + Analytics.System.hide + screenName) }
    }

    final override val onCreateResourcesScope by lazyGet {
        lifecycle.createResourcesScope(Lifecycle.Event.ON_CREATE)
    }
    final override val onStartResourcesScope by lazyGet {
        lifecycle.createResourcesScope(Lifecycle.Event.ON_START)
    }
    final override val onResumeResourcesScope by lazyGet {
        lifecycle.createResourcesScope(Lifecycle.Event.ON_RESUME)
    }
    final override val onPauseResourcesScope by lazyGet {
        lifecycle.createResourcesScope(Lifecycle.Event.ON_PAUSE)
    }
    final override val onStopResourcesScope by lazyGet {
        lifecycle.createResourcesScope(Lifecycle.Event.ON_STOP)
    }
    final override val onDestroyResourcesScope by lazyGet {
        lifecycle.createResourcesScope(Lifecycle.Event.ON_DESTROY)
    }
}