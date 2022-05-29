package com.exfaust.core.email

import android.util.Patterns
import com.exfaust.core.Parser
import com.exfaust.core.Validated
import com.exfaust.core.invalid
import com.exfaust.core.valid
import java.util.regex.Pattern
import javax.annotation.CheckReturnValue

@CheckReturnValue
fun Email.Companion.parseDefault(
    source: CharSequence?,
    caseSensitivity: Email.CaseSensitivity = Email.CaseSensitivity.None
): Validated<EmailDefaultParser.Error, Email> = EmailDefaultParser(caseSensitivity).parse(source)

@CheckReturnValue
fun Email.Companion.validateDefault(
    data: CharSequence?,
    caseSensitivity: Email.CaseSensitivity = Email.CaseSensitivity.None
): EmailDefaultParser.Error? = EmailDefaultParser(caseSensitivity).validate(data)

@CheckReturnValue
fun Email.Companion.parseDefaultWithCyrillic(
    source: CharSequence?,
    caseSensitivity: Email.CaseSensitivity = Email.CaseSensitivity.None
): Validated<EmailDefaultWithCyrillicParser.Error, Email> =
    EmailDefaultWithCyrillicParser(caseSensitivity).parse(source)

class EmailDefaultParser(private val caseSensitivity: Email.CaseSensitivity = Email.CaseSensitivity.None) :
    Parser<Email, EmailDefaultParser.Error> {
    @CheckReturnValue
    override fun parse(source: CharSequence?): Validated<Error, Email> =
        validate(source)?.invalid()
            ?: source!!.split('@', limit = 2)
                .let { parts -> Email(parts[0], parts[1], caseSensitivity).valid() }

    @CheckReturnValue
    override fun validate(data: CharSequence?): Error? =
        if (data.isNullOrEmpty()) {
            Error.Empty
        } else if (!Patterns.EMAIL_ADDRESS.matcher(data).matches()) {
            Error.InvalidFormat
        } else {
            null
        }

    enum class Error {
        Empty,
        InvalidFormat
    }
}

class EmailDefaultWithCyrillicParser(val caseSensitivity: Email.CaseSensitivity) :
    Parser<Email, EmailDefaultWithCyrillicParser.Error> {
    companion object {
        private val _pattern: Pattern = Pattern.compile(
            "[a-zA-ZА-Яа-я0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[А-Яа-яa-zA-Z0-9][А-Яа-яa-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[А-Яа-яa-zA-Z0-9][А-Яа-яa-zA-Z0-9\\-]{0,25}" +
                ")+"
        )
    }

    @CheckReturnValue
    override fun parse(source: CharSequence?): Validated<Error, Email> =
        validate(source)?.invalid()
            ?: source!!.split('@', limit = 2)
                .let { parts -> Email(parts[0], parts[1], caseSensitivity).valid() }

    @CheckReturnValue
    override fun validate(data: CharSequence?): Error? =
        if (data.isNullOrEmpty()) {
            Error.Empty
        } else if (!_pattern.matcher(data).matches()) {
            Error.InvalidFormat
        } else {
            null
        }

    enum class Error {
        Empty,
        InvalidFormat
    }
}