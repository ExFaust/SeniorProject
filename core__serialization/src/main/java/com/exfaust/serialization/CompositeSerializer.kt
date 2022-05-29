package com.exfaust.serialization

import kotlinx.collections.immutable.ImmutableCollection
import java.lang.reflect.Type
import javax.annotation.CheckReturnValue

class CompositeSerializer(
    private val _components: ImmutableCollection<ComponentSerializer>
) : Serializer {
    @CheckReturnValue
    override fun serialize(
        targetType: Type,
        target: Any?
    ): String? {
        for (component in _components) {
            return component.serialize(this, targetType, target) ?: continue
        }

        return null
    }
}