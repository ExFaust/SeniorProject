package com.exfaust.serialization.adapters

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import java.lang.reflect.Type
import javax.annotation.CheckReturnValue

fun GsonBuilder.registerPersistentList(): GsonBuilder =
    registerTypeHierarchyAdapter(PersistentList::class.java, PersistentListAdapter())

class PersistentListAdapter :
    JsonDeserializer<PersistentList<*>>,
    JsonSerializer<PersistentList<*>> {
    @CheckReturnValue
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): PersistentList<*>? {
        if (json.isJsonNull) {
            return null
        }

        val itemType = typeOfT.assertParametrized().actualTypeArguments.first()

        var result = persistentListOf<Any?>()
        for (item in json.asJsonArray) {
            result = result.add(context.deserialize(item, itemType))
        }
        return result
    }

    @CheckReturnValue
    override fun serialize(
        src: PersistentList<*>?,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return if (src == null) {
            JsonNull.INSTANCE
        } else {
            val itemType = typeOfSrc.assertParametrized().actualTypeArguments.first()

            val result = JsonArray(src.size)

            for (item in src) {
                result.add(context.serialize(item, itemType))
            }

            result
        }
    }
}
