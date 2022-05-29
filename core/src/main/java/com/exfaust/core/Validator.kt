package com.exfaust.core

import javax.annotation.CheckReturnValue

interface Validator<Data, Error> {
    @CheckReturnValue
    fun validate(data: Data): Error?
}