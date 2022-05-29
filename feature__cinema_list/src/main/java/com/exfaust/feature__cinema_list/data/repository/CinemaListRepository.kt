package com.exfaust.feature__cinema_list.data.repository

import com.exfaust.feature__cinema_list.data.model.Cinema
import io.reactivex.Single
import kotlinx.collections.immutable.PersistentList

interface CinemaListRepository {
    fun getCinemaList(): Single<PersistentList<Cinema>>
}