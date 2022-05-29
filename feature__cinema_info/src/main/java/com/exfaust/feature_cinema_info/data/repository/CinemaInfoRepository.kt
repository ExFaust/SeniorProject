package com.exfaust.feature_cinema_info.data.repository

import com.exfaust.base__cinema.CinemaId
import com.exfaust.feature_cinema_info.data.model.Cinema
import io.reactivex.Single

interface CinemaInfoRepository {
    fun getCinemaInfo(id: CinemaId): Single<Cinema>
}