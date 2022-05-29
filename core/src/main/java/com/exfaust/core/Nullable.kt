package com.exfaust.core

import javax.annotation.CheckReturnValue

sealed class Nullable<out A> {
    @CheckReturnValue
    abstract fun unwrap(): A?

    object Null : Nullable<Nothing>() {
        @CheckReturnValue
        override fun unwrap(): Nothing? = null
    }

    data class Some<out A>(val value: A) : Nullable<A>() {
        @CheckReturnValue
        override fun unwrap(): A = value
    }
}

fun <T> T?.toNullable(): Nullable<T> =
    this?.let { Nullable.Some(it) } ?: Nullable.Null