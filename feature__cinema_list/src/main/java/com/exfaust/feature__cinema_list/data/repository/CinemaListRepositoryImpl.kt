package com.exfaust.feature__cinema_list.data.repository

import com.exfaust.feature__cinema_list.data.api.CinemaListApi
import com.exfaust.feature__cinema_list.data.model.Cinema
import io.reactivex.Single
import kotlinx.collections.immutable.PersistentList

class CinemaListRepositoryImpl(
    private val _api: CinemaListApi
) : CinemaListRepository {
    override fun getCinemaList(): Single<PersistentList<Cinema>> =
        _api.getCinemaListAsync()
}