package com.exfaust.serialization

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import javax.annotation.CheckReturnValue

@CheckReturnValue
inline fun <reified T> typeOf(): Type = object : TypeToken<T>() {}.type

@CheckReturnValue
inline fun <reified Param> SerializationProvider.deserialize(string: String?): Param? =
    deserializer.deserialize(typeOf<Param>(), string) as? Param

@CheckReturnValue
inline fun <reified Param> SerializationProvider.serialize(param: Param): String =
    serializer.serialize(typeOf<Param>(), param)!!