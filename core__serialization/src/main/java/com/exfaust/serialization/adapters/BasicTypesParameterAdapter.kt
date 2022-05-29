package com.exfaust.serialization.adapters

import com.exfaust.core.isSuperclassOf
import com.exfaust.serialization.ComponentSerializer
import com.exfaust.serialization.Serializer
import java.lang.reflect.Type

object BasicTypesParameterAdapter : ComponentSerializer {
    override fun serialize(
        serializer: Serializer,
        targetType: Type,
        target: Any?
    ): String? =
        if (targetType is Class<*>) {
            when {
                String::class.java == targetType -> (target as String)
                Number::class.isSuperclassOf(targetType) -> (target as Number).toString()
                Boolean::class.isSuperclassOf(targetType) -> (target as Boolean).toString()
                CharSequence::class.isSuperclassOf(targetType) -> (target as CharSequence).toString()

                else -> null
            }
        } else {
            null
        }
}