package com.exfaust.serialization.adapters

import com.exfaust.core.wrapped.WrappedCompatValue
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import kotlin.reflect.KClass

inline fun <reified Value : Comparable<Value>, reified Wrapped : WrappedCompatValue<Value, Wrapped>> GsonBuilder.registerWrappedCompatValue(
    noinline restorator: (Value) -> Wrapped
): GsonBuilder = registerWrappedCompatValue(Wrapped::class, Value::class, restorator)

fun <Value : Comparable<Value>, Wrapped : WrappedCompatValue<Value, Wrapped>> GsonBuilder.registerWrappedCompatValue(
    targetType: KClass<Wrapped>,
    valueType: KClass<Value>,
    restorator: (Value) -> Wrapped
): GsonBuilder =
    registerTypeAdapter(targetType.java, WrappedCompatValueAdapter(valueType, restorator))

class WrappedCompatValueAdapter<Value : Comparable<Value>, Wrapped : WrappedCompatValue<Value, Wrapped>>(
    private val _valueType: KClass<Value>,
    private val _restorator: (Value) -> Wrapped
) : JsonSerializer<Wrapped>,
    JsonDeserializer<Wrapped> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Wrapped? {
        return if (json.isJsonNull) {
            null
        } else _restorator(
            context.deserialize(json, _valueType.java)
        )
    }

    override fun serialize(
        src: Wrapped?,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return if (src == null) {
            JsonNull.INSTANCE
        } else {
            context.serialize(src.unwrap(), _valueType.java)
        }
    }
}
