package com.exfaust.core_android.toolbar

import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import io.reactivex.Flowable
import javax.annotation.CheckReturnValue

/**
 * Интерфейс тулбара, который может изменяться в зависимости от каких-либо событий
 */
interface HasCustomDynamicToolbar<Event> : HasCustomToolbar {
    fun onUpdateCustomToolbar(event: Event, appBar: AppBarLayout?, toolbar: Toolbar)

    @CheckReturnValue
    fun onCustomToolbarChanged(): Flowable<Event>
}