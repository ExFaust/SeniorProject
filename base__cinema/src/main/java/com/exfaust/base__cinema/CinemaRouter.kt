package com.exfaust.base__cinema

import com.exfaust.core_android.base.BaseActivity

interface CinemaRouter {
    fun goToCinemaInfo(cinemaId: CinemaId, activity: BaseActivity)
}