package com.exfaust.feature_cinema_info.ui.cinema_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.exfaust.base__cinema.CinemaId
import com.exfaust.core.email.formatDefault
import com.exfaust.core.rx.invoke
import com.exfaust.core_android.base.BaseFragment
import com.exfaust.core_android.bundler.Bundler
import com.exfaust.core_android.bundler.getParams
import com.exfaust.core_android.databinding.ApplicationToolbarWhiteBinding
import com.exfaust.core_android.di.fragmentViewModel
import com.exfaust.core_android.images
import com.exfaust.core_android.layoutInflater
import com.exfaust.core_android.toolbar.HasCustomDynamicToolbar
import com.exfaust.core_android.toolbar.HasCustomToolbar
import com.exfaust.feature_cinema_info.analytics.CinemaInfoAnalytics
import com.exfaust.feature_cinema_info.databinding.CinemaInfoMainBinding
import com.google.android.material.appbar.AppBarLayout
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import org.kodein.di.instance
import timber.log.Timber
import javax.annotation.CheckReturnValue

class CinemaInfoFragment :
    BaseFragment(screenName = CinemaInfoAnalytics.Screen.info),
    HasCustomDynamicToolbar<String> {

    private val _bundler: Bundler by instance()
    private val _viewModel: CinemaInfoViewModel by fragmentViewModel()

    private lateinit var _binding: CinemaInfoMainBinding

    private val _onFinishLoading: PublishProcessor<String> = PublishProcessor.create()
    val onFinishLoading: Flowable<String> get() = _onFinishLoading.hide()

    @CheckReturnValue
    override fun onCreateCustomToolbar(): HasCustomToolbar.CustomToolbar =
        object :
            HasCustomToolbar.CustomToolbar.Layout() {

            @CheckReturnValue
            override fun onCreateView(container: ViewGroup): View =
                ApplicationToolbarWhiteBinding.inflate(
                    container.context.layoutInflater,
                    container,
                    false
                ).root

            @CheckReturnValue
            override fun getAppBarLayout(view: View): AppBarLayout =
                ApplicationToolbarWhiteBinding.bind(view).applicationAppBar

            @CheckReturnValue
            override fun getToolbar(view: View): Toolbar =
                ApplicationToolbarWhiteBinding.bind(view).applicationToolbar
        }

    @CheckReturnValue
    override fun onUpdateCustomToolbar(
        event: String,
        appBar: AppBarLayout?,
        toolbar: Toolbar
    ) {
        appBar?.let {
            ApplicationToolbarWhiteBinding.bind(it).applicationToolbar.title = event
        }
    }

    @CheckReturnValue
    override fun onCustomToolbarChanged(): Flowable<String> = onFinishLoading

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = CinemaInfoMainBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val request: CinemaId? = arguments?.getParams(_bundler)
        if (request === null) {

            navigation.navigateUp()
            Timber.e("Invalid request")
            return
        }

        _viewModel.onEvent()
            .observe(viewLifecycleOwner, eventsObserver(_binding.cinemaInfoMainProgress))
        _viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                CinemaInfoState.Idle -> {
                    _viewModel.dispatchAction(CinemaInfoAction.StartLoading(request))
                }
                is CinemaInfoState.MainState -> {
                    _onFinishLoading(state.cinema.name)

                    images.load(state.cinema.image)
                        .centerCrop()
                        .into(_binding.cinemaInfoMainImage)

                    _binding.cinemaInfoMainAddress.text = state.cinema.address
                    _binding.cinemaInfoMainEmail.text = state.cinema.email?.formatDefault()
                    _binding.cinemaInfoMainSite.text = state.cinema.site.toString()
                }
            }
        }
    }
}