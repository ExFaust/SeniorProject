package com.exfaust.serialization.adapters

import com.exfaust.serialization.JsonAdapter
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import java.lang.reflect.Type
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class LocalDateTimeAdapter : JsonAdapter<LocalDateTime> {

    override fun serialize(
        src: LocalDateTime,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement = JsonPrimitive(src.toEpochSecond(ZoneOffset.UTC))

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): LocalDateTime =
        Instant.ofEpochSecond(json.asLong).atZone(ZoneId.systemDefault()).toLocalDateTime()
}