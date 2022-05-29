package com.exfaust.core_android.dimentions

import javax.annotation.CheckReturnValue

interface Measurement<Element, Value, Unit> {
    @CheckReturnValue
    fun Element.measureIn(unit: Unit): Value

    @CheckReturnValue
    fun Unit.convertTo(
        value: Value,
        unit: Unit
    ): Value

    @CheckReturnValue
    fun Unit.convertFrom(
        value: Value,
        unit: Unit
    ): Value = unit.convertTo(value, this)
}