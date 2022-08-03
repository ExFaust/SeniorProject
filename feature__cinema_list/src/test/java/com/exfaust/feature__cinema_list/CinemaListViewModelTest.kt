package com.exfaust.feature__cinema_list

import com.exfaust.base__cinema.CinemaId
import com.exfaust.feature__cinema_list.data.repository.CinemaListRepository
import com.exfaust.feature__cinema_list.ui.all_cinemas.CinemaListAllAction
import com.exfaust.feature__cinema_list.ui.all_cinemas.CinemaListAllState
import com.exfaust.feature__cinema_list.ui.all_cinemas.CinemaListAllViewModel
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CinemaListViewModelTest {

    private val _repository: CinemaListRepository = mock()

    @get:Rule
    val rxRule: ImmediateSchedulersRule = ImmediateSchedulersRule()

    @get:Rule
    val taskRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val _viewModel = CinemaListAllViewModel(_repository)

    @Test
    fun initialLoading() {
        val cinemaList = CinemaListMockData.getCinemaList()
        whenever(_repository.getCinemaList()).thenReturn(Single.just(cinemaList))

        _viewModel.actions.subscribe()

        _viewModel.dispatchAction(CinemaListAllAction.StartLoading)

        val actual = _viewModel.state.value
        assertEquals(CinemaListAllState.MainState(cinemaList), actual)
    }

    @Test
    fun onCinemaClick() {
        val cinemaList = CinemaListMockData.getCinemaList()
        whenever(_repository.getCinemaList()).thenReturn(Single.just(cinemaList))

        _viewModel.actions.subscribe()
        _viewModel.dispatchAction(CinemaListAllAction.StartLoading)
        val currentState = _viewModel.state.value as CinemaListAllState.MainState

        _viewModel.dispatchAction(CinemaListAllAction.OnCinemaClick(CinemaId.restore(1)))

        val actual = _viewModel.state.value
        val expected = CinemaListAllState.GoToCinemaInfoState(CinemaId.restore(1), currentState)
        assertEquals(expected, actual)
    }

    @Test
    fun backToMainState() {
        val cinemaList = CinemaListMockData.getCinemaList()
        whenever(_repository.getCinemaList()).thenReturn(Single.just(cinemaList))

        _viewModel.actions.subscribe()
        _viewModel.dispatchAction(CinemaListAllAction.StartLoading)
        _viewModel.dispatchAction(CinemaListAllAction.OnCinemaClick(CinemaId.restore(1)))
        val currentState = _viewModel.state.value as CinemaListAllState.GoToCinemaInfoState

        _viewModel.dispatchAction(CinemaListAllAction.BackToMainState(currentState.mainState))

        val actual = _viewModel.state.value
        val expected = CinemaListAllState.MainState(cinemaList)
        assertEquals(expected, actual)
    }
}