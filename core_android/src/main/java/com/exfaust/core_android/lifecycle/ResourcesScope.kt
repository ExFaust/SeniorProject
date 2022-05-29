package com.exfaust.core_android.lifecycle

import io.reactivex.disposables.Disposable

interface ResourcesScope {
    fun registerResource(resource: AutoCloseable)
    fun AutoCloseable.scoped(): Unit = registerResource(this)

    fun registerResource(resource: Disposable): Unit = registerResource(resource.asResource())
    fun Disposable.scoped(): Unit = registerResource(this)
}