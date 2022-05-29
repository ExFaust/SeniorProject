package com.exfaust.core_android

interface NavigateBackHandler {
    fun onNavigateBack(): Result

    enum class Result {
        Exit,
        Consumed,
        Ignored
    }
}