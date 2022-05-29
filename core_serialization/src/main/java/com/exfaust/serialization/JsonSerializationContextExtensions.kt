package com.exfaust.serialization

import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.reflect.TypeToken
import javax.annotation.CheckReturnValue

@CheckReturnValue
inline fun <reified T> JsonSerializationContext.serializeTyped(target: T?): JsonElement =
    serialize(target, object : TypeToken<T>() {}.type)