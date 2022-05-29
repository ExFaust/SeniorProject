package com.exfaust.core_android.lifecycle

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

abstract class AbstractResourcesScope : ResourcesScope {
    private val _lock: Lock = ReentrantLock()

    private var _resources: PersistentList<AutoCloseable> = persistentListOf()

    override fun registerResource(resource: AutoCloseable) {
        _lock.withLock {
            _resources = _resources.add(resource)
        }
    }

    protected fun closeResources() {
        _lock.withLock {
            val resources = _resources
            _resources = resources.clear()
            for (resource in resources) {
                resource.close()
            }
        }
    }
}