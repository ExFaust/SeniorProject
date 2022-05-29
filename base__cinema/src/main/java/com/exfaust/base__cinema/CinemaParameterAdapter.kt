package com.exfaust.base__cinema

import com.exfaust.serialization.ComponentSerializer
import com.exfaust.serialization.Serializer
import java.lang.reflect.Type
import javax.annotation.CheckReturnValue

object CinemaParameterAdapter : ComponentSerializer {
    @CheckReturnValue
    override fun serialize(
        serializer: Serializer,
        targetType: Type,
        target: Any?
    ): String? =
        when (targetType) {
            CinemaId::class.java -> (target as CinemaId).unwrap()
            else -> null
        }?.toString()
}