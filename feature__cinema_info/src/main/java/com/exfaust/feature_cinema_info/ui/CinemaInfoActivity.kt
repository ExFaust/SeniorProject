package com.exfaust.feature_cinema_info.ui

import android.os.Bundle
import com.exfaust.core.analytic.Analytics
import com.exfaust.core_android.base.BaseActivity
import com.exfaust.core_android.toolbar.HasCustomToolbar
import com.exfaust.feature_cinema_info.R
import com.exfaust.feature_cinema_info.di.cinemaInfoModule
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import javax.annotation.CheckReturnValue

class CinemaInfoActivity : BaseActivity(
    contentId = R.layout.cinema_info,
    moduleName = Analytics.Modules.cinemaList,
    navigationRes = R.id.cinema_info___navigation_host_fragment,
    dependency = {
        import(cinemaInfoModule)
    }
) {
    override val topLevelDestinationIds: PersistentSet<Int> = persistentSetOf()

    override val defaultCustomToolbar: HasCustomToolbar = object : HasCustomToolbar {
        @CheckReturnValue
        override fun onCreateCustomToolbar(): HasCustomToolbar.CustomToolbar =
            HasCustomToolbar.CustomToolbar.None
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigation.setGraph(R.navigation.cinema_info, intent.extras)
    }
}