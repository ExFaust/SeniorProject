package com.exfaust.seniorproject

import android.app.Application
import com.exfaust.base__cinema.CinemaParameterAdapter
import com.exfaust.core.rx.RxUnhandledExceptionHandler
import com.exfaust.core_android.di.commonModule
import com.exfaust.core_api.di.apiModule
import com.exfaust.feature__cinema_list.di.cinemaListModule
import com.exfaust.feature_cinema_info.di.cinemaInfoModule
import com.exfaust.seniorproject.di.basicApplicationModule
import io.reactivex.plugins.RxJavaPlugins
import kotlinx.collections.immutable.persistentListOf
import org.kodein.di.DI
import org.kodein.di.DIAware
import timber.log.Timber

class App : Application(), DIAware {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        RxJavaPlugins.setErrorHandler(RxUnhandledExceptionHandler(false))
    }

    override val di: DI by DI.lazy {
        importOnce(basicApplicationModule(this@App))

        import(commonModule)

        import(
            apiModule(
                persistentListOf(
                    CinemaParameterAdapter
                )
            )
        )

        import(cinemaListModule)
        import(cinemaInfoModule)
    }
}