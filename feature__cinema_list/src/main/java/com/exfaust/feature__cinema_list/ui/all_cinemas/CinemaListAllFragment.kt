package com.exfaust.feature__cinema_list.ui.all_cinemas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import com.exfaust.base__cinema.CinemaRouter
import com.exfaust.core.lazyGet
import com.exfaust.core.rx.throttleUserInput
import com.exfaust.core_android.base.BaseActivity
import com.exfaust.core_android.base.BaseFragment
import com.exfaust.core_android.common.SpacingItemDecoration
import com.exfaust.core_android.databinding.ApplicationToolbarWhiteBinding
import com.exfaust.core_android.di.fragmentViewModel
import com.exfaust.core_android.dimentions.Dimension
import com.exfaust.core_android.layoutInflater
import com.exfaust.core_android.toolbar.HasCustomToolbar
import com.exfaust.feature__cinema_list.analytics.CinemaListAnalytics
import com.exfaust.feature__cinema_list.databinding.CinemaListAllBinding
import com.google.android.material.appbar.AppBarLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import org.kodein.di.instance
import javax.annotation.CheckReturnValue

class CinemaListAllFragment :
    BaseFragment(screenName = CinemaListAnalytics.Screen.all),
    HasCustomToolbar {

    private val _viewModel: CinemaListAllViewModel by fragmentViewModel()
    private val _adapter: CinemaListAllAdapter by lazyGet { CinemaListAllAdapter() }
    private val _router: CinemaRouter by instance()

    private lateinit var _binding: CinemaListAllBinding

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = CinemaListAllBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.cinemaListAll.adapter = _adapter
        _binding.cinemaListAll.addItemDecoration(SpacingItemDecoration.verticalSpace(Dimension.dp(4f)))
        _binding.cinemaListAll.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        _viewModel.onEvent()
            .observe(viewLifecycleOwner, eventsObserver(_binding.cinemaListAllProgress))
        _viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                CinemaListAllState.Idle -> {}
                CinemaListAllState.Start -> {
                    _viewModel.dispatchAction(CinemaListAllAction.StartLoading)
                }
                is CinemaListAllState.MainState -> {
                    _adapter.submitList(state.cinemas)
                }
                is CinemaListAllState.GoToCinemaInfoState -> {
                    _router.goToCinemaInfo(state.id, requireActivity() as BaseActivity)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        _adapter
            .onItemClick
            .throttleUserInput()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                _viewModel.dispatchAction(CinemaListAllAction.OnCinemaClick(it))
            }
            .scopedByPause()
    }
}