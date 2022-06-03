package com.exfaust.core_android.di

import com.exfaust.core.AnalyticManager
import com.exfaust.core_android.GlobalAnalyticManager
import com.exfaust.core_android.format.ContextDisplayFormat
import com.exfaust.core_android.format.DisplayFormat
import com.exfaust.core_android.format.FormatContext
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

val commonModule = DI.Module(name = "COMMON") {
    bind<DisplayFormat>() with provider {
        ContextDisplayFormat(FormatContext(), instance())
    }

    bind<AnalyticManager>() with singleton {
        GlobalAnalyticManager(instance() /*instance()*/)
    }

    /*bind<FirebaseCrashlytics>() with singleton {
        FirebaseApp.initializeApp(instance())
        FirebaseCrashlytics.getInstance()
    }*/
}