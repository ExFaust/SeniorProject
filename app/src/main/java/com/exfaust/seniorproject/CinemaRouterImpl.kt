package com.exfaust.seniorproject

import android.content.Intent
import com.exfaust.base__cinema.CinemaId
import com.exfaust.base__cinema.CinemaRouter
import com.exfaust.core.exception.ContractException
import com.exfaust.core_android.base.BaseActivity
import com.exfaust.core_android.bundler.Bundler
import com.exfaust.feature__cinema_list.ui.CinemaListActivity
import com.exfaust.feature_cinema_info.ui.CinemaInfoActivity

class CinemaRouterImpl(
    private val _bundler: Bundler
) : CinemaRouter {
    override fun goToCinemaInfo(cinemaId: CinemaId, activity: BaseActivity) {
        (activity as? CinemaListActivity)?.let {
            activity.startActivity(
                _bundler.intoIntent(
                    CinemaId::class.java,
                    cinemaId,
                    Intent(activity, CinemaInfoActivity::class.java)
                )
            )
        } ?: throw ContractException("Unsupported route")
    }
}