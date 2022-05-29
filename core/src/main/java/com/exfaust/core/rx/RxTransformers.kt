package com.exfaust.core.rx

import javax.annotation.CheckReturnValue

object RxTransformers {
    @CheckReturnValue
    fun <Upstream> mapError(mapper: (Throwable) -> Throwable): ThroughObservableSourceTransformer<Upstream> =
        MapErrorTransformer(mapper)
}