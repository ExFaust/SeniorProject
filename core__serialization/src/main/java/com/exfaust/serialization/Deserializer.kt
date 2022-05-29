package com.exfaust.serialization

import java.lang.reflect.Type
import javax.annotation.CheckReturnValue

interface Deserializer {
    @CheckReturnValue
    fun deserialize(
        targetType: Type,
        source: String?
    ): Any?
}