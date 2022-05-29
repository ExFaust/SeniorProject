package com.exfaust.core.rx

import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.CompletableTransformer
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Maybe
import io.reactivex.MaybeSource
import io.reactivex.MaybeTransformer
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import org.reactivestreams.Publisher
import javax.annotation.CheckReturnValue

interface ObservableSourceTransformer<Upstream, Downstream> :
    ObservableTransformer<Upstream, Downstream>,
    FlowableTransformer<Upstream, Downstream>,
    SingleTransformer<Upstream, Downstream>,
    MaybeTransformer<Upstream, Downstream>,
    CompletableTransformer {
    @CheckReturnValue
    override fun apply(upstream: Completable): CompletableSource

    @CheckReturnValue
    override fun apply(upstream: Flowable<Upstream>): Publisher<Downstream>

    @CheckReturnValue
    override fun apply(upstream: Maybe<Upstream>): MaybeSource<Downstream>

    @CheckReturnValue
    override fun apply(upstream: Observable<Upstream>): ObservableSource<Downstream>

    @CheckReturnValue
    override fun apply(upstream: Single<Upstream>): SingleSource<Downstream>
}
