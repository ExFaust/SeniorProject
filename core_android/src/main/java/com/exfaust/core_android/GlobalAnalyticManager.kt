package com.exfaust.core_android

import com.exfaust.core.AnalyticManager
import com.exfaust.serialization.SerializationProvider

//TODO need to create Firebase app in console
class GlobalAnalyticManager(
    private val _serializationProvider: SerializationProvider,
    //private val _crashlytics: FirebaseCrashlytics
) : AnalyticManager {

    override fun setupUserId(userId: String) {
        // _crashlytics.setUserId(userId)
    }

    override fun reportEvent(eventName: String, isWithAmplitude: Boolean) {
        //_crashlytics.log(eventName)
    }

    override fun reportEventWithParam(
        eventName: String,
        property: Any,
        propertyName: String?,
        isWithAmplitude: Boolean
    ) {
        //_crashlytics.log(eventName + " property: " + _serializationProvider.serialize(property))
    }

    override fun reportError(errorMessage: String, exception: Throwable) {
        //_crashlytics.recordException(exception)
    }
}