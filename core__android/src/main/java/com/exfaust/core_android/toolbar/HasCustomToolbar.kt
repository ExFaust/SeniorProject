package com.exfaust.core_android.toolbar

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.exfaust.core_android.toolbar.HasCustomToolbar.CustomToolbarStyle.OverlapContent
import com.exfaust.core_android.toolbar.HasCustomToolbar.CustomToolbarStyle.OverlapContentWithTransparentSystemWindow
import com.exfaust.core_android.toolbar.HasCustomToolbar.CustomToolbarStyle.ShiftContent
import com.exfaust.core_android.toolbar.HasCustomToolbar.CustomToolbarStyle.ShiftTransparent
import com.google.android.material.appbar.AppBarLayout
import javax.annotation.CheckReturnValue

/**
 * Базовый интерфейс тулбара
 */
interface HasCustomToolbar {
    @CheckReturnValue
    fun onCreateCustomToolbar(): CustomToolbar

    /**
     * Доступные стили тулбара
     * @property ShiftContent - классический
     * @property ShiftTransparent - прозрачный и без тени
     * @property OverlapContent - контент компонента "заходит" под тулбар
     * @property OverlapContentWithTransparentSystemWindow - контент компонента "заходит" под тулбар и под статусбар
     */
    enum class CustomToolbarStyle {
        ShiftContent,
        ShiftTransparent,
        OverlapContent,
        OverlapContentWithTransparentSystemWindow
    }

    sealed class CustomToolbar {
        object None : CustomToolbar()

        abstract class Layout(
            val style: CustomToolbarStyle = ShiftContent
        ) : CustomToolbar() {
            @CheckReturnValue
            abstract fun onCreateView(container: ViewGroup): View

            @CheckReturnValue
            abstract fun getAppBarLayout(view: View): AppBarLayout?

            @CheckReturnValue
            abstract fun getToolbar(view: View): Toolbar
        }
    }
}