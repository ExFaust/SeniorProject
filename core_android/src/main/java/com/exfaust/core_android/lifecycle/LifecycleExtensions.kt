package com.exfaust.core_android.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import javax.annotation.CheckReturnValue

@CheckReturnValue
fun Lifecycle.createResourcesScope(event: Lifecycle.Event): ResourcesScope =
    LifecycleResourceScope(event).apply { addObserver(this) }

@CheckReturnValue
fun ViewModel.createResourcesScope(): ViewModelResourceScope =
    ViewModelResourceScope()