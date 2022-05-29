package com.exfaust.core_android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import kotlin.reflect.KClass

class ViewModelLazy<VM : ViewModel>(
    private val viewModelClass: KClass<VM>,
    private val storeProducer: () -> ViewModelStore,
    private val factoryProducer: () -> ViewModelProvider.Factory,
    private val lifecycle: Lifecycle
) : Lazy<VM> {
    private var cached: VM? = null

    override val value: VM
        get() {
            val viewModel = cached
            return if (viewModel == null) {
                val factory = factoryProducer()
                val store = storeProducer()
                ViewModelProvider(store, factory)[viewModelClass.java].also { vm ->
                    cached = vm
                    (vm as? LifecycleObserver)?.let { lifecycle.addObserver(it) }
                }
            } else {
                viewModel
            }
        }

    override fun isInitialized(): Boolean = cached != null
}