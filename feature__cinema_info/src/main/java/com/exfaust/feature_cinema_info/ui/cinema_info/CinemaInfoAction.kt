package com.exfaust.feature_cinema_info.ui.cinema_info

import com.exfaust.base__cinema.CinemaId
import com.exfaust.core_android.base.BaseAction

sealed class CinemaInfoAction : BaseAction {
    data class StartLoading(
        val id: CinemaId
    ) : CinemaInfoAction()
}
