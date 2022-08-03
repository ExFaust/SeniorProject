package com.exfaust.feature_cinema_info

import android.net.Uri
import com.exfaust.base__cinema.CinemaId
import com.exfaust.feature_cinema_info.data.model.Cinema
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

object CinemaInfoMockData {
    fun getCinemaInfo(mockUri: Uri): Cinema =
        Cinema(
            id = CinemaId.restore(1),
            name = "Cinema #1",
            address = "Address #1",
            email = null,
            image = mockUri,
            site = mockUri
        )

    fun getCinemaInfoResponse(mockUri: Uri): PersistentList<Cinema> =
        persistentListOf(
            Cinema(
                id = CinemaId.restore(1),
                name = "Cinema #1",
                address = "Address #1",
                email = null,
                image = mockUri,
                site = mockUri
            )
        )
}