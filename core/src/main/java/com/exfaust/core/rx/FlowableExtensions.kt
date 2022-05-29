package com.exfaust.core.rx

import com.exfaust.core.Nullable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.annotations.BackpressureKind
import io.reactivex.annotations.BackpressureSupport
import io.reactivex.annotations.SchedulerSupport
import java.util.concurrent.TimeUnit
import javax.annotation.CheckReturnValue

@CheckReturnValue
fun <Upstream> Flowable<Upstream>.onBackpressureBufferLast(scheduler: Scheduler): Flowable<Upstream> =
    onBackpressureLatest().observeOn(scheduler, false, 1)

@io.reactivex.annotations.CheckReturnValue
@CheckReturnValue
@BackpressureSupport(BackpressureKind.PASS_THROUGH)
@SchedulerSupport(SchedulerSupport.NONE)
fun <Upstream> Flowable<Nullable<Upstream>>.filterDefined(): Flowable<Upstream> =
    filter { it is Nullable.Some }.map { (it as Nullable.Some).value }

@SchedulerSupport(SchedulerSupport.COMPUTATION)
@CheckReturnValue
fun <T> Flowable<T>.throttleUserInput(): Flowable<T> = throttleFirst(500, TimeUnit.MILLISECONDS)

@SchedulerSupport(SchedulerSupport.COMPUTATION)
@CheckReturnValue
fun <T> Flowable<T>.debounceUserInput(): Flowable<T> = debounce(300, TimeUnit.MILLISECONDS)