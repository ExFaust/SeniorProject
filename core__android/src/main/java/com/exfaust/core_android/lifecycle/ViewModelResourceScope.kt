package com.exfaust.core_android.lifecycle

class ViewModelResourceScope : AbstractResourcesScope() {
    fun notifyCleared() {
        closeResources()
    }
}