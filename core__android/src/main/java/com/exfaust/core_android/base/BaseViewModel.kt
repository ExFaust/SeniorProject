package com.exfaust.core_android.base

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.exfaust.core_android.lifecycle.LifecycleResourcesScopeOwner
import com.exfaust.core_android.lifecycle.ResourcesScope
import com.exfaust.core_android.lifecycle.ResourcesScopeOwner
import com.exfaust.core_android.lifecycle.createResourcesScope
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel(), LifecycleResourcesScopeOwner,
    LifecycleEventObserver, ResourcesScopeOwner {

    protected val compositeDisposable = CompositeDisposable()

    protected val eventListener: MediatorLiveData<Events> =
        MediatorLiveData()

    /**
     * Метод для подписки на базовые события вью
     */
    fun onEvent(): LiveData<Events> {
        return eventListener
    }

    override val onCreateResourcesScope: ResourcesScope
        get() = resourcesScope
    override val onStartResourcesScope: ResourcesScope
        get() = resourcesScope
    override val onResumeResourcesScope: ResourcesScope
        get() = resourcesScope
    override val onPauseResourcesScope: ResourcesScope
        get() = resourcesScope
    override val onStopResourcesScope: ResourcesScope
        get() = resourcesScope
    override val onDestroyResourcesScope: ResourcesScope
        get() = resourcesScope

    override val resourcesScope = createResourcesScope()

    open fun onPause() {
        compositeDisposable.clear()
        resourcesScope.notifyCleared()
    }

    open fun onResume() {}

    open fun onCreate() {}

    @CallSuper
    override fun onCleared() {
        super.onCleared()

        resourcesScope.notifyCleared()
        compositeDisposable.dispose()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_PAUSE -> onPause()
            Lifecycle.Event.ON_RESUME -> onResume()
            Lifecycle.Event.ON_CREATE -> onCreate()
            else -> {}
        }
    }
}