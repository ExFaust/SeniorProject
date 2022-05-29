package com.exfaust.core.debug

import com.exfaust.core.exception.ContractException
import com.exfaust.core.lazyGet
import kotlinx.collections.immutable.ImmutableCollection

class DebugData
private constructor(
    val description: String?,
    val isCollection: Boolean,
    private val _items: ImmutableCollection<Any?>?,
    val representation: DebugRepresentation
) {
    val items: ImmutableCollection<Any?> by lazyGet {
        if (isCollection)
            _items!!
        else
            throw ContractException("Only the collection type has elements")
    }

    companion object {
        fun single(
            representation: DebugRepresentation,
            description: String? = null
        ) = DebugData(description, false, null, representation)

        fun collection(
            items: ImmutableCollection<Any?>,
            representation: DebugRepresentation,
            description: String? = null
        ) = DebugData(description, true, items, representation)
    }
}