package com.exfaust.core

import javax.annotation.CheckReturnValue

interface Formattable<Data> {
    @CheckReturnValue
    fun format(formatter: Formatter<Data>): String
}