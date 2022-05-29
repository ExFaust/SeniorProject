package com.exfaust.serialization

interface SerializationProvider {
    val deserializer: Deserializer
    val serializer: Serializer
}
