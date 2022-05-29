package com.exfaust.serialization

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer

inline fun <reified T> GsonBuilder.registerTypeAdapter(serializer: JsonSerializer<T>): GsonBuilder =
    registerTypeAdapter(T::class.java, serializer)

inline fun <reified T> GsonBuilder.registerTypeAdapter(deserializer: JsonDeserializer<T>): GsonBuilder =
    registerTypeAdapter(T::class.java, deserializer)

inline fun <reified T> GsonBuilder.registerTypeAdapter(adapter: JsonAdapter<T>): GsonBuilder =
    registerTypeAdapter(T::class.java, adapter)

inline fun <reified T> GsonBuilder.registerTypeHierarchyAdapter(adapter: JsonSerializer<T>): GsonBuilder =
    registerTypeHierarchyAdapter(T::class.java, adapter)

inline fun <reified T> GsonBuilder.registerTypeHierarchyAdapter(adapter: JsonDeserializer<T>): GsonBuilder =
    registerTypeHierarchyAdapter(T::class.java, adapter)

inline fun <reified T> GsonBuilder.registerTypeHierarchyAdapter(adapter: JsonAdapter<T>): GsonBuilder =
    registerTypeHierarchyAdapter(T::class.java, adapter)