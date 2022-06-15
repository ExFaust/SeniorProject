package com.exfaust.core_api.serialization

import com.exfaust.serialization.SerializationProvider
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class SerializerBodyConverterFactory(
    private val _serializationProvider: SerializationProvider
) : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> =
        Converter<ResponseBody, Any> {
            _serializationProvider.deserializer.deserialize(type, it.string())
        }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        return Converter<Any, RequestBody> { src ->
            _serializationProvider.serializer
                .serialize(type, src)?.toRequestBody("application/json".toMediaType())
        }
    }
}
