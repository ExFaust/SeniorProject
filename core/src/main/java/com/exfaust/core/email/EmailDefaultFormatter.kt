package com.exfaust.core.email

import com.exfaust.core.Formatter
import javax.annotation.CheckReturnValue

object EmailDefaultFormatter : Formatter<Email> {
    @CheckReturnValue
    override fun format(data: Email): String = "${data.localPart}@${data.domain}"
}