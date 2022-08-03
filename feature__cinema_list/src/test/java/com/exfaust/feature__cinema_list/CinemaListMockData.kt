package com.exfaust.feature__cinema_list

import com.exfaust.base__cinema.CinemaId
import com.exfaust.feature__cinema_list.data.model.Cinema
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

object CinemaListMockData {
    fun getCinemaList(): PersistentList<Cinema> =
        (1..3).map {
            Cinema(
                id = CinemaId.restore(it.toLong()),
                name = "Cinema #$it",
                address = "Address #$it"
            )
        }.toPersistentList()

    fun getCinemaCachedList(): List<Cinema> =
        (1..3).map {
            Cinema(
                id = CinemaId.restore(it.toLong()),
                name = "Cinema #$it",
                address = "Address #$it"
            )
        }
}