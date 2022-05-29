package com.exfaust.serialization.adapters

import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.annotation.CheckReturnValue

@CheckReturnValue
fun Type.assertParametrized(): ParameterizedType {
    if (this !is ParameterizedType) {
        throw JsonParseException("Expected parameterized type, but actual: `$this`.")
    }

    return this
}

@CheckReturnValue
fun <T : Any> T?.requiredField(name: String): T =
    this ?: throw JsonParseException("$name is required.")

val JsonElement?.isNullOrJsonNull get() = this == null || isJsonNull