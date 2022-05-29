package com.exfaust.core_android.dimentions

import javax.annotation.CheckReturnValue

class Dimension private constructor(
    internal val _value: Float,
    internal val _unit: DimensionUnit
) {
    companion object {
        val zero = create(0f, DimensionUnit.PX)

        @CheckReturnValue
        fun create(
            value: Float,
            unit: DimensionUnit
        ) = Dimension(value, unit)

        @CheckReturnValue
        fun dp(value: Float): Dimension = create(value, DimensionUnit.DP)

        @CheckReturnValue
        fun sp(value: Float): Dimension = create(value, DimensionUnit.SP)

        @CheckReturnValue
        fun px(value: Float): Dimension = create(value, DimensionUnit.PX)

        @CheckReturnValue
        fun em(value: Float): Dimension = create(value, DimensionUnit.EM)
    }
}