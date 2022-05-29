package com.exfaust.core_android.di

import org.kodein.di.DI

typealias DependencyBuilder<Container> = DI.MainBuilder.(Container) -> Unit