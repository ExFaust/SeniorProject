package com.exfaust.core_android.format

import java.time.LocalDateTime
import javax.annotation.CheckReturnValue

/**
 * Предоставляет "стандартные" или часто используемые в приложении правила форматирования [Formatter].
 */
interface DisplayFormat {
    @CheckReturnValue
    fun fullDateFormatter(): Formatter<LocalDateTime>
}