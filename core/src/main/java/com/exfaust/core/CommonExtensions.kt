package com.exfaust.core

import com.exfaust.core.exception.UnreachableException

fun unreachable(): Nothing = throw UnreachableException()

fun <T> lazyGet(initializer: () -> T) = lazy(LazyThreadSafetyMode.PUBLICATION, initializer)