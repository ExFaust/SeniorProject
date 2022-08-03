package com.exfaust.feature_cinema_info

import android.net.Uri
import com.exfaust.base__cinema.CinemaId
import com.exfaust.feature__cinema_list.ImmediateSchedulersRule
import com.exfaust.feature_cinema_info.data.api.CinemaInfoApi
import com.exfaust.feature_cinema_info.data.repository.CinemaInfoRepositoryImpl
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CinemaInfoRepositoryTest {

    private val _api: CinemaInfoApi = mock()
    private val _uri: Uri = mock()

    @get:Rule
    val rxRule: ImmediateSchedulersRule = ImmediateSchedulersRule()

    private val _repository = CinemaInfoRepositoryImpl(_api)

    @Test
    fun getCinemaInfoFromApi() {
        val expected = CinemaInfoMockData.getCinemaInfo(_uri)
        whenever(_api.getCinemaInfoAsync(any())).thenReturn(
            Single.just(
                CinemaInfoMockData.getCinemaInfoResponse(
                    _uri
                )
            )
        )

        _repository.getCinemaInfo(CinemaId.restore(1))
            .test()
            .assertValue(expected)
    }
}