package com.exfaust.feature__cinema_list

import com.exfaust.feature__cinema_list.data.api.CinemaListApi
import com.exfaust.feature__cinema_list.data.repository.CinemaListRepositoryImpl
import com.exfaust.feature__cinema_list.db.CinemaListItemDao
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CinemaListRepositoryTest {

    private val _api: CinemaListApi = mock()
    private val _dao: CinemaListItemDao = mock()

    @get:Rule
    val rxRule: ImmediateSchedulersRule = ImmediateSchedulersRule()

    private val _repository = CinemaListRepositoryImpl(_api, _dao)

    @Test
    fun getCinemaListFromApi() {
        val expected = CinemaListMockData.getCinemaList()
        whenever(_api.getCinemaListAsync()).thenReturn(Single.just(expected))
        whenever(_dao.insert(expected)).thenReturn(Completable.complete())

        _repository.getCinemaList()
            .test()
            .assertValue(expected)
        verify(_dao).insert(expected)
    }

    @Test
    fun getCachedCinemaListAfterError() {
        val expected = CinemaListMockData.getCinemaList()
        whenever(_api.getCinemaListAsync()).thenReturn(Single.error(RuntimeException("Some error")))
        whenever(_dao.getAllCinema()).thenReturn(Single.just(CinemaListMockData.getCinemaCachedList()))

        _repository.getCinemaList()
            .test()
            .assertValue(expected)
    }
}