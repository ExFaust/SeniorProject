package com.exfaust.core.debug

import com.exfaust.core.Formatter
import com.exfaust.core.toPersistentList
import kotlinx.collections.immutable.ImmutableCollection
import kotlinx.collections.immutable.toImmutableList

object DebugFormatter : Formatter<DebugData> {
    override fun format(data: DebugData): String = buildString {
        val hasDescription = !data.description.isNullOrEmpty()
        if (hasDescription || data.isCollection) {
            append('{')

            if (data.isCollection) {
                append("size = ")
                append(data.items.size)

                if (hasDescription) {
                    append(" | ")
                }
            }

            if (hasDescription) {
                append(data.description)
            }

            append('}')
        }

        if (data.isCollection) {
            append('[')

            for (indexedItem in data.items.withIndex()) {
                val value = indexedItem.value
                if (value === null) {
                    append("null")
                } else {
                    append('`')

                    append(value.toDebugString(data.representation))

                    append('`')
                }

                if (indexedItem.index < data.items.size - 1) {
                    append(", ")
                }
            }

            append(']')
        }
    }
}

fun Any.formatDebugSingle(
    representation: DebugRepresentation,
    description: String? = null
): String = DebugFormatter.format(
    DebugData.single(
        representation,
        description
    )
)

fun Any.formatDebugCollection(
    items: ImmutableCollection<Any?>,
    representation: DebugRepresentation,
    description: String? = null
): String = DebugFormatter.format(
    DebugData.collection(
        items,
        representation,
        description
    )
)

fun Any.toDebugString(representation: DebugRepresentation = DebugRepresentation.Compact): String {
    return when (this) {
        is DebugRepresentable -> toString(representation)
        is Collection<*> -> formatDebugCollection(this.toImmutableList(), representation)
        is Array<*> -> formatDebugCollection(this.toPersistentList(), representation)
        else -> toString()
    }
}