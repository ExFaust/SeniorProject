package com.exfaust.feature__cinema_list.ui.all_cinemas

import com.exfaust.base__cinema.CinemaId
import com.exfaust.core_android.base.BaseAction

sealed class CinemaListAllAction : BaseAction {
    object StartLoading : CinemaListAllAction()

    data class OnCinemaClick(
        val cinemaId: CinemaId
    ) : CinemaListAllAction()
}
