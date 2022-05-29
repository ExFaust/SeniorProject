package com.exfaust.feature_cinema_info.data.api

import com.exfaust.feature_cinema_info.data.model.Cinema
import io.reactivex.Single
import kotlinx.collections.immutable.PersistentList
import retrofit2.http.GET
import retrofit2.http.Url

interface CinemaInfoApi {
    @GET
    fun getCinemaInfoAsync(@Url url: String): Single<PersistentList<Cinema>>
}