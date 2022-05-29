package com.exfaust.core_android.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.exfaust.core_android.dimentions.Dimension
import com.exfaust.core_android.dimentions.dimensions

class SpacingItemDecoration private constructor(
    private val _spacingTop: Dimension = Dimension.zero,
    private val _spacingBottom: Dimension = Dimension.zero,
    private val _spacingEnd: Dimension = Dimension.zero,
    private val _spacingStart: Dimension = Dimension.zero
) : RecyclerView.ItemDecoration() {

    companion object {
        fun horizontalSpace(value: Dimension) =
            SpacingItemDecoration(
                _spacingStart = value,
                _spacingEnd = value
            )

        fun verticalSpace(value: Dimension) =
            SpacingItemDecoration(
                _spacingTop = value,
                _spacingBottom = value
            )
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        view.context.dimensions().run {
            outRect.apply {
                this.left = (_spacingStart.toPX() / 2).toInt()
                this.top = (_spacingTop.toPX() / 2).toInt()
                this.right = (_spacingEnd.toPX() / 2).toInt()
                this.bottom = (_spacingBottom.toPX() / 2).toInt()
            }
        }
    }
}
