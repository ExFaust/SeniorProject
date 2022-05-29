package com.exfaust.core_android.lifecycle

fun AutoCloseable.scopedBy(scope: ResourcesScope) {
    scope.registerResource(this)
}

fun AutoCloseable.scopedBy(scopeOwner: ResourcesScopeOwner) = scopedBy(scopeOwner.resourcesScope)