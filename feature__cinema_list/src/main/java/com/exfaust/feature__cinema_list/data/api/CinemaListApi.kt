package com.exfaust.feature__cinema_list.data.api

import com.exfaust.feature__cinema_list.data.model.Cinema
import io.reactivex.Single
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import retrofit2.http.Body
import retrofit2.http.POST

interface CinemaListApi {
    @POST("rows?top=10")
    fun getCinemaListAsync(
        @Body filters: PersistentList<String> = persistentListOf(
            "ObjectAddress",
            "CommonName"
        )
    ): Single<PersistentList<Cinema>>
}