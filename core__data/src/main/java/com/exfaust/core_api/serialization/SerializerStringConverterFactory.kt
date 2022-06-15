package com.exfaust.core_api.serialization

import com.exfaust.serialization.Serializer
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class SerializerStringConverterFactory(
    private val _serializer: Serializer
) : Converter.Factory() {
    override fun stringConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, String> = Converter<Any, String> {
        _serializer.serialize(type, it)
    }
}
