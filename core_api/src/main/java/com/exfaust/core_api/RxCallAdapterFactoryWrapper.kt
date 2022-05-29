package com.exfaust.core_api

import com.exfaust.core.rx.ThroughObservableSourceTransformer
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type
import javax.annotation.CheckReturnValue

class RxCallAdapterFactoryWrapper private constructor(
    private val _original: CallAdapter.Factory,
    private val _composer: (Retrofit) -> ThroughObservableSourceTransformer<Any>
) : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        return _original
            .get(returnType, annotations, retrofit)
            ?.let {
                @Suppress("UNCHECKED_CAST")
                (CallAdapterWrapper(
                    retrofit,
                    it as CallAdapter<Any, Any>,
                    _composer(retrofit)
                ))
            }
    }

    private class CallAdapterWrapper(
        private val _retrofit: Retrofit,
        private val _original: CallAdapter<Any, Any>,
        private val _composer: ThroughObservableSourceTransformer<Any>
    ) : CallAdapter<Any, Any> {
        override fun responseType(): Type {
            return _original.responseType()
        }

        override fun adapt(call: Call<Any>): Any {
            val adapted = _original.adapt(call)

            return when (adapted) {
                is Completable -> adapted.compose(_composer)
                is Maybe<*> -> adapted.compose(_composer)
                is Single<*> -> adapted.compose(_composer)
                is Observable<*> -> adapted.compose(_composer)
                is Flowable<*> -> adapted.compose(_composer)
                else -> throw UnsupportedOperationException("Unsupported rx stream type: `${adapted::class}`.")
            }
        }
    }

    companion object {
        @CheckReturnValue
        fun wrap(
            inner: CallAdapter.Factory,
            composer: (Retrofit) -> ThroughObservableSourceTransformer<Any>
        ): RxCallAdapterFactoryWrapper = RxCallAdapterFactoryWrapper(inner, composer)
    }
}