package com.exfaust.core

import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSuperclassOf

fun <T : Any> KClass<T>.isSuperclassOf(type: Type) =
    type is Class<*> && isSuperclassOf(type.kotlin)

fun <T : Any> KClass<T>.isSubclassOf(type: Type) =
    type is Class<*> && isSubclassOf(type.kotlin)