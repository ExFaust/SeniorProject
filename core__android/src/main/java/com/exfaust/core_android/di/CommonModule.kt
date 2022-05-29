package com.exfaust.core_android.di

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import com.exfaust.core.AnalyticManager
import com.exfaust.core_android.GlobalAnalyticManager
import com.exfaust.core_android.format.ContextDisplayFormat
import com.exfaust.core_android.format.DisplayFormat
import com.exfaust.core_android.format.FormatContext
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

@SuppressLint("HardwareIds")
val commonModule = DI.Module(name = "COMMON") {
    bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(di.direct) }

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