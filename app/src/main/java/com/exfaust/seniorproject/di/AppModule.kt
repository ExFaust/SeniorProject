package com.exfaust.seniorproject.di

import android.app.Application
import android.content.Context
import com.exfaust.base__cinema.CinemaId
import com.exfaust.base__cinema.CinemaRouter
import com.exfaust.core_android.bundler.Bundler
import com.exfaust.core_android.bundler.SerializationBundler
import com.exfaust.core_api.di.ApiSerializationTag
import com.exfaust.feature__cinema_list.data.serialization.registerCinemaListApiSerializationAdapters
import com.exfaust.feature__cinema_list.data.serialization.registerCinemaListSerializationAdapters
import com.exfaust.feature_cinema_info.data.serialization.registerCinemaInfoApiSerializationAdapters
import com.exfaust.feature_cinema_info.data.serialization.registerCinemaInfoSerializationAdapters
import com.exfaust.seniorproject.BuildConfig
import com.exfaust.seniorproject.CinemaRouterImpl
import com.exfaust.serialization.GsonSerializationProvider
import com.exfaust.serialization.SerializationProvider
import com.exfaust.serialization.adapters.registerGlobalAdapters
import com.exfaust.serialization.adapters.registerWrappedCompatValue
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindings.subTypes
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import org.kodein.di.with
import org.kodein.type.jvmType

fun basicApplicationModule(application: Application) = DI.Module("application") {
    bind<Context>().subTypes() with { type ->
        when (type.jvmType) {
            Context::class.java -> provider { application }
            Application::class.java -> provider { application }
            else -> throw NoSuchElementException()
        }
    }

    bind<Bundler>() with singleton {
        SerializationBundler(
            instance(),
            BuildConfig.APPLICATION_ID
        )
    }

    bind<SerializationProvider>() with singleton {
        GsonSerializationProvider.build {
            it
                .registerGlobalAdapters()
                .registerCinemaInfoSerializationAdapters()
                .registerCinemaListSerializationAdapters()

                .registerWrappedCompatValue(CinemaId.Companion::restore)
        }
    }

    bind<SerializationProvider>(ApiSerializationTag) with singleton {
        GsonSerializationProvider.build {
            it
                .registerGlobalAdapters()
                .registerCinemaListApiSerializationAdapters()
                .registerCinemaInfoApiSerializationAdapters()

                .registerWrappedCompatValue(CinemaId.Companion::restore)
        }
    }

    bind<CinemaRouter>() with singleton {
        CinemaRouterImpl(instance())
    }
}