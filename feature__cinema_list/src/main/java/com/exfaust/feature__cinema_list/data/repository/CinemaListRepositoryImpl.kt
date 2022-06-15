package com.exfaust.feature__cinema_list.data.repository

import com.exfaust.feature__cinema_list.data.api.CinemaListApi
import com.exfaust.feature__cinema_list.data.model.Cinema
import com.exfaust.feature__cinema_list.db.CinemaListItemDao
import io.reactivex.Single
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

class CinemaListRepositoryImpl(
    private val _api: CinemaListApi,
    private val _dao: CinemaListItemDao
) : CinemaListRepository {
    override fun getCinemaList(): Single<PersistentList<Cinema>> =
        _api.getCinemaListAsync()
            .flatMap {
                _dao.insert(it)
                    .andThen(Single.just(it))
            }
            .onErrorResumeNext {
                _dao.getAllCinema().map { it.toPersistentList() }
            }
}