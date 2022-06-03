package com.exfaust.feature__cinema_list.ui

import com.exfaust.core.analytic.Analytics
import com.exfaust.core_android.base.BaseActivity
import com.exfaust.core_android.toolbar.HasCustomToolbar
import com.exfaust.feature__cinema_list.R
import com.exfaust.feature__cinema_list.di.cinemaListModule
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import javax.annotation.CheckReturnValue

class CinemaListActivity : BaseActivity(
    contentId = R.layout.cinema_list,
    moduleName = Analytics.Modules.cinemaList,
    navigationRes = R.id.cinema_list___navigation_host_fragment,
    dependency = {
        import(cinemaListModule)
    }
) {
    override val topLevelDestinationIds: PersistentSet<Int> = persistentSetOf(
        R.id.cinema_list__all
    )

    override val defaultCustomToolbar: HasCustomToolbar = object : HasCustomToolbar {
        @CheckReturnValue
        override fun onCreateCustomToolbar(): HasCustomToolbar.CustomToolbar =
            HasCustomToolbar.CustomToolbar.None
    }
}