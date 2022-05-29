package com.exfaust.serialization.adapters

import com.exfaust.core.email.Email
import com.exfaust.serialization.registerTypeAdapter
import com.google.gson.GsonBuilder
import javax.annotation.CheckReturnValue

@CheckReturnValue
fun GsonBuilder.registerGlobalAdapters(): GsonBuilder =
    this

        .registerTypeAdapter(LocalDateTimeAdapter())
        .registerPersistentList()
        .registerUri()
        .registerTypeAdapter(Email.gsonWithCyrillicAdapter())

