package com.exfaust.feature__cinema_list.ui.all_cinemas

import com.exfaust.base__cinema.CinemaId
import com.exfaust.feature__cinema_list.data.model.Cinema
import kotlinx.collections.immutable.PersistentList

sealed class CinemaListAllState {
    object Idle : CinemaListAllState()

    data class MainState(
        val cinemas: PersistentList<Cinema>
    ) : CinemaListAllState() {
        fun toCinemaInfoState(cinemaId: CinemaId): GoToCinemaInfoState =
            GoToCinemaInfoState(
                id = cinemaId,
                mainState = this
            )
    }

    data class GoToCinemaInfoState(
        val id: CinemaId,
        val mainState: MainState
    ) : CinemaListAllState()
}
