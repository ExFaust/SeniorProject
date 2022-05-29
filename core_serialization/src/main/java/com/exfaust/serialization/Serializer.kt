package com.exfaust.serialization

import java.lang.reflect.Type
import javax.annotation.CheckReturnValue

interface Serializer {
    @CheckReturnValue
    fun serialize(
        targetType: Type,
        target: Any?
    ): String?
}