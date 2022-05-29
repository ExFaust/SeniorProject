package com.exfaust.core_android.format

import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import javax.annotation.CheckReturnValue

@CheckReturnValue
fun <Data : TemporalAccessor> DateTimeFormatter.toFormatter(): Formatter<Data> =
    object : Formatter<Data> {
        override fun format(data: Data): String = this@toFormatter.format(data)
    }