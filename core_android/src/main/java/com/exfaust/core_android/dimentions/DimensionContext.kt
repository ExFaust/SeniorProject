package com.exfaust.core_android.dimentions

import android.content.Context
import android.util.DisplayMetrics
import androidx.annotation.CheckResult
import androidx.annotation.DimenRes
import java.math.BigDecimal
import javax.annotation.CheckReturnValue

@CheckReturnValue
fun Dimension.Companion.context(context: Context): DimensionContext = DimensionContext(context)

@CheckReturnValue
fun Context.dimensions(): DimensionContext = Dimension.context(this)

class DimensionContext(val context: Context) :
    Measurement<Dimension, Float, DimensionUnit> {

    @CheckReturnValue
    override fun Dimension.measureIn(unit: DimensionUnit): Float = _unit.convertTo(_value, unit)

    @CheckReturnValue
    override fun DimensionUnit.convertTo(value: Float, unit: DimensionUnit): Float =
        when (unit) {
            DimensionUnit.DP -> toDP(value, context)
            DimensionUnit.SP -> toSP(value, context)
            DimensionUnit.PX -> toPX(value, context)
            DimensionUnit.EM -> toEM(value, context)
        }

    @CheckReturnValue
    fun Dimension.toDP(): Float = measureIn(DimensionUnit.DP)

    @CheckReturnValue
    fun Dimension.toSP(): Float = measureIn(DimensionUnit.SP)

    @CheckReturnValue
    fun Dimension.toPX(): Float = measureIn(DimensionUnit.PX)

    @CheckReturnValue
    fun Dimension.toEM(): Float = measureIn(DimensionUnit.EM)

    @CheckReturnValue
    operator fun Dimension.times(multiplier: Number): Dimension = times(multiplier.toFloat())

    @CheckReturnValue
    operator fun BigDecimal.times(target: Dimension): Dimension = target.times(this.toFloat())

    @CheckReturnValue
    operator fun Number.times(target: Dimension): Dimension = target.times(this.toFloat())

    val displayMetrics: DisplayMetrics get() = context.resources.displayMetrics

    @CheckResult
    fun resolveResource(@DimenRes id: Int): Dimension =
        Dimension.px(context.resources.getDimension(id))
}