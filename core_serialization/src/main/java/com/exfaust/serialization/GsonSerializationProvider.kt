package com.exfaust.serialization

import com.exfaust.core.lazyGet
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type
import javax.annotation.CheckReturnValue

class GsonSerializationProvider(private val _gson: Gson) : SerializationProvider {
    companion object {
        @CheckReturnValue
        fun build(builder: (GsonBuilder) -> Unit): GsonSerializationProvider {
            return GsonSerializationProvider(
                GsonBuilder().also(builder).create()
            )
        }
    }

    private val _deserializer: Deserializer by lazyGet {
        object : Deserializer {
            override fun deserialize(targetType: Type, source: String?): Any? {
                return _gson.fromJson(source, targetType)
            }
        }
    }

    override val deserializer: Deserializer
        get() = _deserializer

    private val _serializer: Serializer by lazyGet {
        object : Serializer {
            override fun serialize(targetType: Type, target: Any?): String? {
                return _gson.toJson(target, targetType).toString()
            }
        }
    }

    override val serializer: Serializer
        get() = _serializer
}