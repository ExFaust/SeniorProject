package com.exfaust.feature__cinema_list

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.rules.ExternalResource
import java.util.concurrent.TimeUnit

class ImmediateSchedulersRule : ExternalResource() {

    private val _immediateScheduler: Scheduler = object : Scheduler() {

        override fun createWorker() = ExecutorScheduler.ExecutorWorker({ it.run() }, true)

        // This prevents errors when scheduling a delay
        override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
            return super.scheduleDirect(run, 0, unit)
        }
    }

    override fun before() {
        RxJavaPlugins.setIoSchedulerHandler { _immediateScheduler }
        RxJavaPlugins.setComputationSchedulerHandler { _immediateScheduler }
        RxJavaPlugins.setNewThreadSchedulerHandler { _immediateScheduler }
        RxJavaPlugins.setSingleSchedulerHandler { _immediateScheduler }

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { _immediateScheduler }
        RxAndroidPlugins.setMainThreadSchedulerHandler { _immediateScheduler }
    }

    override fun after() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }
}