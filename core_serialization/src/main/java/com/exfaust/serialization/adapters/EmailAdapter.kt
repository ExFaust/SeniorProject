package com.exfaust.serialization.adapters

import com.exfaust.core.email.Email
import com.exfaust.core.email.formatDefault
import com.exfaust.core.email.parseDefaultWithCyrillic
import com.exfaust.core.getOrNull
import com.exfaust.serialization.JsonAdapter
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import java.lang.reflect.Type

fun Email.Companion.gsonWithCyrillicAdapter(): JsonAdapter<Email> {
    return EmailWithCyrillicAdapter()
}

class EmailWithCyrillicAdapter : JsonAdapter<Email> {
    override fun serialize(
        src: Email,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.formatDefault())
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Email? {
        return Email.parseDefaultWithCyrillic(json.asString).getOrNull()
    }
}