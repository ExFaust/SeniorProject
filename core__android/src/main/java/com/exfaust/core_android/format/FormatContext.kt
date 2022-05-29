package com.exfaust.core_android.format

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.Locale
import javax.annotation.CheckReturnValue

/**
 * Содержит информацию о правилах форматирования разнообразных данных. Предоставляет методы для создания [Formatter].
 */
class FormatContext {
    @CheckReturnValue
    fun dateTimeByPattern(pattern: String): Formatter<TemporalAccessor> =
        DateTimeFormatter
            .ofPattern(pattern, Locale.getDefault())
            .withZone(ZoneId.systemDefault())
            .toFormatter()
}