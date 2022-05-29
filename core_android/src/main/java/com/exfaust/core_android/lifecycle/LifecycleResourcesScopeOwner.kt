package com.exfaust.core_android.lifecycle

import io.reactivex.disposables.Disposable

interface LifecycleResourcesScopeOwner {
    val onCreateResourcesScope: ResourcesScope
    fun Disposable.scopedByCreate(): Unit = scopedBy(onCreateResourcesScope)
    fun AutoCloseable.scopedByCreate(): Unit = scopedBy(onCreateResourcesScope)

    val onStartResourcesScope: ResourcesScope
    fun Disposable.scopedByStart(): Unit = scopedBy(onStartResourcesScope)
    fun AutoCloseable.scopedByStart(): Unit = scopedBy(onStartResourcesScope)

    val onResumeResourcesScope: ResourcesScope
    fun Disposable.scopedByResume(): Unit = scopedBy(onResumeResourcesScope)
    fun AutoCloseable.scopedByResume(): Unit = scopedBy(onResumeResourcesScope)

    val onPauseResourcesScope: ResourcesScope
    fun Disposable.scopedByPause(): Unit = scopedBy(onPauseResourcesScope)
    fun AutoCloseable.scopedByPause(): Unit = scopedBy(onPauseResourcesScope)

    val onStopResourcesScope: ResourcesScope
    fun Disposable.scopedByStop(): Unit = scopedBy(onStopResourcesScope)
    fun AutoCloseable.scopedByStop(): Unit = scopedBy(onStopResourcesScope)

    val onDestroyResourcesScope: ResourcesScope
    fun Disposable.scopedByDestroy(): Unit = scopedBy(onDestroyResourcesScope)
    fun AutoCloseable.scopedByDestroy(): Unit = scopedBy(onDestroyResourcesScope)
}