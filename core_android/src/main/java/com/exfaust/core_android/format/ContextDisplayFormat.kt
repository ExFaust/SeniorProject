package com.exfaust.core_android.format

import android.content.Context
import com.exfaust.core.lazyGet
import java.time.LocalDateTime

class ContextDisplayFormat(
    private val formatContext: FormatContext,
    private val context: Context
) : DisplayFormat {
    private val _fullDateFormatter: Formatter<LocalDateTime> by lazyGet {
        formatContext.dateTimeByPattern("dd.MM.yyyy")
    }

    override fun fullDateFormatter(): Formatter<LocalDateTime> = _fullDateFormatter
}