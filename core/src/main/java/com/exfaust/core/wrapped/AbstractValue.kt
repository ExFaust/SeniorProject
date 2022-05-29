package com.exfaust.core.wrapped

import com.exfaust.core.lazyGet
import javax.annotation.CheckReturnValue

abstract class AbstractValue<Self : AbstractValue<Self>> : Value<Self> {
    @CheckReturnValue
    final override fun equalTo(other: Self?): Boolean {
        if (other === null) {
            return false
        }

        if (other === this) {
            return true
        }

        if (other::class != this::class) {
            return false
        }

        return internalEquals(other)
    }

    @CheckReturnValue
    final override fun equals(other: Any?): Boolean {
        if (other === null) {
            return false
        }

        if (other === this) {
            return true
        }

        if (other::class != this::class) {
            return false
        }

        @Suppress("UNCHECKED_CAST")
        return internalEquals(other as Self)
    }

    @CheckReturnValue
    protected abstract fun internalEquals(other: Self): Boolean

    private val _hashCode: Int by lazyGet { internalHashCode() }

    @CheckReturnValue
    final override fun hashCode(): Int = _hashCode

    @CheckReturnValue
    protected abstract fun internalHashCode(): Int

    private val _toString: String by lazyGet { internalToString() }

    @CheckReturnValue
    protected abstract fun internalToString(): String

    @CheckReturnValue
    final override fun toString(): String = _toString
}