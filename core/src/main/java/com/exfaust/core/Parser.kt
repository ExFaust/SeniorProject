package com.exfaust.core

import javax.annotation.CheckReturnValue

interface Parser<Output, Error> : Validator<CharSequence?, Error> {
    @CheckReturnValue
    fun parse(source: CharSequence?): Validated<Error, Output>
}