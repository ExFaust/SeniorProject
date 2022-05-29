package com.exfaust.core.rx

import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.MaybeSource
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.SingleSource
import org.reactivestreams.Publisher

class MapErrorTransformer<Upstream>(private val _mapper: (Throwable) -> Throwable) :
    ThroughObservableSourceTransformer<Upstream> {
    override fun apply(upstream: Completable): CompletableSource {
        return upstream.onErrorResumeNext { error: Throwable ->
            Completable.error(_mapper(error))
        }
    }

    override fun apply(upstream: Flowable<Upstream>): Publisher<Upstream> {
        return upstream.onErrorResumeNext { error: Throwable ->
            Flowable.error(_mapper(error))
        }
    }

    override fun apply(upstream: Maybe<Upstream>): MaybeSource<Upstream> {
        return upstream.onErrorResumeNext { error: Throwable ->
            Maybe.error(_mapper(error))
        }
    }

    override fun apply(upstream: Observable<Upstream>): ObservableSource<Upstream> {
        return upstream.onErrorResumeNext { error: Throwable ->
            Observable.error(_mapper(error))
        }
    }

    override fun apply(upstream: Single<Upstream>): SingleSource<Upstream> {
        return upstream.onErrorResumeNext { error: Throwable ->
            Single.error(_mapper(error))
        }
    }
}
