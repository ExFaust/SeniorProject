package com.exfaust.feature_cinema_info.data.repository

import com.exfaust.base__cinema.CinemaId
import com.exfaust.feature_cinema_info.data.api.CinemaInfoApi
import com.exfaust.feature_cinema_info.data.model.Cinema
import io.reactivex.Single

class CinemaInfoRepositoryImpl(
    private val _api: CinemaInfoApi
) : CinemaInfoRepository {
    override fun getCinemaInfo(id: CinemaId): Single<Cinema> =
        _api.getCinemaInfoAsync("rows?\$filter=Cells/global_id eq ${id.unwrap()}")
            .map { it.first() }
}