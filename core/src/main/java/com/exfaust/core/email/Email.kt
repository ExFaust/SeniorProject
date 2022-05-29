package com.exfaust.core.email

import android.net.Uri
import com.exfaust.core.Formattable
import com.exfaust.core.Formatter
import com.exfaust.core.lazyGet
import java.util.Locale
import javax.annotation.CheckReturnValue

class Email(
    val localPart: String,
    val domain: String,
    val caseSensitivity: CaseSensitivity
) : Formattable<Email> {
    companion object;

    enum class CaseSensitivity {
        LocalAndDomain,
        Local,
        Domain,
        None
    }

    val normalizedLocalPart: String
        get() {
            return if (caseSensitivity == CaseSensitivity.LocalAndDomain || caseSensitivity == CaseSensitivity.Local) {
                localPart
            } else {
                localPart.toLowerCase(Locale.ROOT)
            }
        }

    val normalizedDomain: String
        get() {
            return if (caseSensitivity == CaseSensitivity.LocalAndDomain || caseSensitivity == CaseSensitivity.Domain) {
                domain
            } else {
                domain.toLowerCase(Locale.ROOT)
            }
        }

    override fun format(formatter: Formatter<Email>) = formatter.format(this)

    private val _uri: Uri by lazyGet {
        Uri.Builder()
            .scheme("mailto")
            .encodedOpaquePart(formatDefault())
            .build()
    }

    fun toUri(): Uri = _uri

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Email

        if (localPart != other.localPart) return false
        if (domain != other.domain) return false
        if (caseSensitivity != other.caseSensitivity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = localPart.hashCode()
        result = 31 * result + domain.hashCode()
        result = 31 * result + caseSensitivity.hashCode()
        return result
    }
}

@CheckReturnValue
fun Email.formatDefault() = format(EmailDefaultFormatter)