package com.exfaust.serialization

import java.lang.reflect.Type
import javax.annotation.CheckReturnValue

interface ComponentSerializer {
    @CheckReturnValue
    fun serialize(
        serializer: Serializer,
        targetType: Type,
        target: Any?
    ): String?
}