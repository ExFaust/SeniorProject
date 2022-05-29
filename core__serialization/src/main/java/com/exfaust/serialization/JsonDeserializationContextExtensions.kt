package com.exfaust.serialization

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import javax.annotation.CheckReturnValue

inline fun <reified T> JsonDeserializationContext.deserializeTypedRequired(target: JsonElement): T {
    return deserializeTyped(target)
        ?: throw JsonParseException("Required value is null of type '${T::class}'.")
}

@CheckReturnValue
inline fun <reified T> JsonDeserializationContext.deserializeTyped(target: JsonElement): T? =
    deserialize(target, object : TypeToken<T>() {}.type)

@CheckReturnValue
inline fun <reified T : Any> JsonDeserializationContext.deserializeTypedRequiredField(
    json: JsonObject,
    field: String
): T = deserializeTyped(json.get(field))
    ?: throw JsonParseException("Required field '$field' is null of type '${T::class}'.")