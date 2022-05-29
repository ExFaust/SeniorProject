package com.exfaust.core

sealed class Validated<out E, out A> {

    data class Valid<out A>(val a: A) : Validated<Nothing, A>()

    data class Invalid<out E>(val e: E) : Validated<E, Nothing>()

    inline fun <B> fold(fe: (E) -> B, fa: (A) -> B): B =
        when (this) {
            is Valid -> fa(a)
            is Invalid -> (fe(e))
        }

    val isValid =
        fold({ false }, { true })
    val isInvalid =
        fold({ true }, { false })
}

fun <A> A.valid(): Validated<Nothing, A> =
    Validated.Valid(this)

fun <E> E.invalid(): Validated<E, Nothing> =
    Validated.Invalid(this)

fun <E, A> Validated<E, A>.getOrNull(): A? =
    if (this is Validated.Valid) this.a else null