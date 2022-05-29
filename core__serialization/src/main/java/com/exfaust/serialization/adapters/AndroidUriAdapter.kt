package com.exfaust.serialization.adapters

import android.net.Uri
import com.exfaust.serialization.registerTypeAdapter
import com.exfaust.serialization.registerTypeHierarchyAdapter
import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import java.net.URI
import java.net.URL

fun GsonBuilder.registerUri(): GsonBuilder =
    this
        .registerTypeHierarchyAdapter(AndroidUriSerializer)
        .registerTypeAdapter(URISerializer)
        .registerTypeAdapter(URLSerializer)

object AndroidUriSerializer : PrimitiveAdapter<Uri>() {
    override fun serialize(src: Uri): JsonPrimitive = JsonPrimitive(src.toString())

    override fun deserialize(json: JsonPrimitive): Uri = Uri.parse(json.asString)
}

object URISerializer : PrimitiveAdapter<URI>() {
    override fun serialize(src: URI): JsonPrimitive = JsonPrimitive(src.toString())

    override fun deserialize(json: JsonPrimitive): URI = URI.create(json.asString)
}

object URLSerializer : PrimitiveAdapter<URL>() {
    override fun serialize(src: URL): JsonPrimitive = JsonPrimitive(src.toString())

    override fun deserialize(json: JsonPrimitive): URL = URL(json.asString)
}