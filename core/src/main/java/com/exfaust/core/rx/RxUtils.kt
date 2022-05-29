package com.exfaust.core.rx

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer
import org.reactivestreams.Subscriber
import javax.annotation.CheckReturnValue

@io.reactivex.annotations.CheckReturnValue
@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <reified T> Observable<in T>.ofSubtype(): Observable<T> = ofType(T::class.java)

@io.reactivex.annotations.CheckReturnValue
@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <reified T> Flowable<in T>.ofSubtype(): Flowable<T> = ofType(T::class.java)

operator fun <T> Subscriber<T>.invoke(item: T) {
    onNext(item)
}

fun Subscriber<Unit>.onNext() {
    onNext(Unit)
}

operator fun Subscriber<Unit>.invoke() {
    onNext()
}

operator fun <T> Observer<T>.invoke(item: T) {
    onNext(item)
}

fun Observer<Unit>.onNext() {
    onNext(Unit)
}

operator fun Observer<Unit>.invoke() {
    onNext()
}

fun Disposable.addTo(container: DisposableContainer): Disposable =
    apply { container.add(this) }