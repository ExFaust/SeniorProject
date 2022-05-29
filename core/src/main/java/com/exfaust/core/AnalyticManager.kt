package com.exfaust.core

/**
 * Менеджер для работы с ивентами аналитики
 */
interface AnalyticManager {
    fun setupUserId(userId: String)

    fun reportEvent(eventName: String, isWithAmplitude: Boolean = true)

    fun reportEventWithParam(
        eventName: String,
        property: Any,
        propertyName: String? = null,
        isWithAmplitude: Boolean = true
    )

    fun reportError(errorMessage: String, exception: Throwable)
}