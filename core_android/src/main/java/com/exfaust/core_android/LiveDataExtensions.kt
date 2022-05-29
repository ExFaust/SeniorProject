package com.exfaust.core_android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.exfaust.core.rx.ofSubtype
import io.reactivex.Flowable

fun <T, S> LiveData<T?>.combineWithNulls(other: LiveData<S?>): LiveData<Pair<T?, S?>> =
    MediatorLiveData<Pair<T?, S?>>().apply {
        addSource(this@combineWithNulls) { value = Pair(it, other.value) }
        addSource(other) { value = Pair(this@combineWithNulls.value, it) }
    }

fun <T, S> LiveData<T>.combineWith(other: LiveData<S>): LiveData<Pair<T?, S?>> =
    MediatorLiveData<Pair<T?, S?>>().apply {
        addSource(this@combineWith) { value = Pair(it, other.value) }
        addSource(other) { value = Pair(this@combineWith.value, it) }
    }

inline fun <reified State> MutableLiveData<State>.setValueFlowable(value: State): Flowable<Unit> =
    Flowable.fromCallable { this.value = value }

inline fun <reified State, reified SubState : State> MutableLiveData<State>.getStateFlowable(): Flowable<SubState> =
    Flowable.just(this.value).ofSubtype()
