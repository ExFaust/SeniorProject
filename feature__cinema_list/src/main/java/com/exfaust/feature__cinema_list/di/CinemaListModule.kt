package com.exfaust.feature__cinema_list.di

import com.exfaust.core_android.di.bindViewModel
import com.exfaust.feature__cinema_list.data.api.CinemaListApi
import com.exfaust.feature__cinema_list.data.repository.CinemaListRepository
import com.exfaust.feature__cinema_list.data.repository.CinemaListRepositoryImpl
import com.exfaust.feature__cinema_list.ui.all_cinemas.CinemaListAllViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import retrofit2.Retrofit

val cinemaListModule = DI.Module(name = "CINEMA_LIST") {
    bind<CinemaListRepository>() with singleton {
        CinemaListRepositoryImpl(instance())
    }

    bind<CinemaListApi>() with singleton {
        instance<Retrofit>().create(CinemaListApi::class.java)
    }

    bindViewModel<CinemaListAllViewModel>() with provider {
        CinemaListAllViewModel(instance())
    }
}