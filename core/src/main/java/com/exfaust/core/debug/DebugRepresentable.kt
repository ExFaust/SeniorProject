package com.exfaust.core.debug

interface DebugRepresentable {
    fun toString(representation: DebugRepresentation): String

    override fun toString(): String
}