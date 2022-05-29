package com.exfaust.core_android.dimentions

import android.content.Context
import android.util.TypedValue
import javax.annotation.CheckReturnValue

enum class DimensionUnit {
    DP {
        @CheckReturnValue
        override fun toSP(
            value: Float,
            context: Context
        ): Float = toPX(value, context) / context.resources.displayMetrics.scaledDensity

        @CheckReturnValue
        override fun toDP(
            value: Float,
            context: Context
        ): Float = value

        @CheckReturnValue
        override fun toPX(
            value: Float,
            context: Context
        ): Float = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            context.resources.displayMetrics
        )

        @CheckReturnValue
        override fun toEM(
            value: Float,
            context: Context
        ): Float = toSP(value, context) * _spEmRatio
    },
    SP {
        @CheckReturnValue
        override fun toSP(
            value: Float,
            context: Context
        ): Float = value

        @CheckReturnValue
        override fun toDP(
            value: Float,
            context: Context
        ): Float = toPX(value, context) / context.resources.displayMetrics.density

        @CheckReturnValue
        override fun toPX(
            value: Float,
            context: Context
        ): Float = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            value,
            context.resources.displayMetrics
        )

        @CheckReturnValue
        override fun toEM(
            value: Float,
            context: Context
        ): Float = value * _spEmRatio
    },
    PX {
        @CheckReturnValue
        override fun toSP(
            value: Float,
            context: Context
        ): Float = value / context.resources.displayMetrics.scaledDensity

        @CheckReturnValue
        override fun toDP(
            value: Float,
            context: Context
        ): Float = value / context.resources.displayMetrics.density

        @CheckReturnValue
        override fun toPX(
            value: Float,
            context: Context
        ): Float = value

        @CheckReturnValue
        override fun toEM(
            value: Float,
            context: Context
        ): Float = toSP(value, context) * _spEmRatio
    },
    EM {
        @CheckReturnValue
        override fun toDP(
            value: Float,
            context: Context
        ): Float = toPX(value, context) / context.resources.displayMetrics.density

        @CheckReturnValue
        override fun toSP(
            value: Float,
            context: Context
        ): Float = value / _spEmRatio

        @CheckReturnValue
        override fun toPX(
            value: Float,
            context: Context
        ): Float = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            toSP(value, context),
            context.resources.displayMetrics
        )

        @CheckReturnValue
        override fun toEM(
            value: Float,
            context: Context
        ): Float = value
    };

    @CheckReturnValue
    abstract fun toDP(
        value: Float,
        context: Context
    ): Float

    @CheckReturnValue
    abstract fun toSP(
        value: Float,
        context: Context
    ): Float

    @CheckReturnValue
    abstract fun toPX(
        value: Float,
        context: Context
    ): Float

    @CheckReturnValue
    abstract fun toEM(
        value: Float,
        context: Context
    ): Float

    companion object {
        private const val _spEmRatio = 0.0624f
    }
}

