package com.exfaust.feature_cinema_info.di

import com.exfaust.core_android.di.bindViewModel
import com.exfaust.feature_cinema_info.data.api.CinemaInfoApi
import com.exfaust.feature_cinema_info.data.repository.CinemaInfoRepository
import com.exfaust.feature_cinema_info.data.repository.CinemaInfoRepositoryImpl
import com.exfaust.feature_cinema_info.ui.cinema_info.CinemaInfoViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import retrofit2.Retrofit

val cinemaInfoModule = DI.Module(name = "CINEMA_INFO") {
    bindViewModel<CinemaInfoViewModel>() with provider {
        CinemaInfoViewModel(instance())
    }

    bind<CinemaInfoRepository>() with singleton {
        CinemaInfoRepositoryImpl(instance())
    }

    bind<CinemaInfoApi>() with singleton {
        instance<Retrofit>().create(CinemaInfoApi::class.java)
    }
}