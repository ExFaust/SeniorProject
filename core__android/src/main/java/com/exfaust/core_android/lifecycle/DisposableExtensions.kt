package com.exfaust.core_android.lifecycle

import io.reactivex.disposables.Disposable
import javax.annotation.CheckReturnValue

@CheckReturnValue
fun Disposable.asResource(): AutoCloseable =
    AutoCloseable { if (!isDisposed) dispose() }

fun Disposable.scopedBy(scope: ResourcesScope) {
    scope.registerResource(asResource())
}

fun Disposable.scopedBy(scopeOwner: ResourcesScopeOwner) {
    scopedBy(scopeOwner.resourcesScope)
}