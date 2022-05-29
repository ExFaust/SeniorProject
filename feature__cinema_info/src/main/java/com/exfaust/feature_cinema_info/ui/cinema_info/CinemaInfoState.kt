package com.exfaust.feature_cinema_info.ui.cinema_info

import com.exfaust.feature_cinema_info.data.model.Cinema

sealed class CinemaInfoState {
    object Idle : CinemaInfoState()

    data class MainState(
        val cinema: Cinema
    ) : CinemaInfoState()
}
