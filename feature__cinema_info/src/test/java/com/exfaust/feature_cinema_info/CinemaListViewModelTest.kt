package com.exfaust.feature_cinema_info

import android.net.Uri
import com.exfaust.base__cinema.CinemaId
import com.exfaust.feature__cinema_list.ImmediateSchedulersRule
import com.exfaust.feature__cinema_list.InstantTaskExecutorRule
import com.exfaust.feature_cinema_info.data.repository.CinemaInfoRepository
import com.exfaust.feature_cinema_info.ui.cinema_info.CinemaInfoAction
import com.exfaust.feature_cinema_info.ui.cinema_info.CinemaInfoState
import com.exfaust.feature_cinema_info.ui.cinema_info.CinemaInfoViewModel
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CinemaListViewModelTest {

    private val _repository: CinemaInfoRepository = mock()
    private val _uri: Uri = mock()

    @get:Rule
    val rxRule: ImmediateSchedulersRule = ImmediateSchedulersRule()

    @get:Rule
    val taskRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val _viewModel = CinemaInfoViewModel(_repository)

    @Test
    fun initialLoading() {
        val cinema = CinemaInfoMockData.getCinemaInfo(_uri)
        whenever(_repository.getCinemaInfo(any())).thenReturn(Single.just(cinema))

        _viewModel.actions.subscribe()

        _viewModel.dispatchAction(CinemaInfoAction.StartLoading(CinemaId.restore(1)))

        val actual = _viewModel.state.value
        assertEquals(CinemaInfoState.MainState(cinema), actual)
    }
}