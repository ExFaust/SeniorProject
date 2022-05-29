package com.exfaust.serialization.adapters

import com.exfaust.serialization.JsonAdapter
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import java.lang.reflect.Type

inline fun <reified T : Any> GsonBuilder.registerPrimitiveAdapter(
    crossinline serializer: (T) -> JsonPrimitive,
    crossinline deserializer: (JsonPrimitive) -> T
): GsonBuilder =
    registerTypeAdapter(T::class.java, object : PrimitiveAdapter<T>() {
        override fun serialize(src: T): JsonPrimitive = serializer(src)

        override fun deserialize(json: JsonPrimitive): T = deserializer(json)
    })

inline fun <reified T : Any> GsonBuilder.registerHierarchyPrimitiveAdapter(
    crossinline serializer: (T) -> JsonPrimitive,
    crossinline deserializer: (JsonPrimitive) -> T
): GsonBuilder =
    registerTypeHierarchyAdapter(T::class.java, object : PrimitiveAdapter<T>() {
        override fun serialize(src: T): JsonPrimitive = serializer(src)

        override fun deserialize(json: JsonPrimitive): T = deserializer(json)
    })

abstract class PrimitiveAdapter<T : Any> : JsonAdapter<T> {
    protected abstract fun serialize(src: T): JsonPrimitive
    protected abstract fun deserialize(json: JsonPrimitive): T

    @Throws(JsonParseException::class)
    final override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): T? {
        return if (json.isJsonNull) {
            null
        } else if (json.isJsonPrimitive) {
            deserialize(json as JsonPrimitive)
        } else {
            throw IllegalArgumentException("Primitive adapter can't deserialize ${json::class.simpleName} (value = ${json}).")
        }
    }

    final override fun serialize(
        src: T?,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return if (src == null) JsonNull.INSTANCE else serialize(src)
    }
}
