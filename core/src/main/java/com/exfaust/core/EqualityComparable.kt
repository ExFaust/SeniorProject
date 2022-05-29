package iss.digital.mgp.metrorouting.core

interface EqualityComparable<T> {

    fun equalTo(other: T?): Boolean

    override operator fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}