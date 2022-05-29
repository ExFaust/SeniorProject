package com.exfaust.core.rx

import android.accounts.OperationCanceledException
import com.exfaust.core.exception.ContractException
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.functions.Consumer
import ru.mosgorpass.utils.exception.NoConnectivityException
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException

class RxUnhandledExceptionHandler constructor(
    private val _showSwallowedExceptions: Boolean
) : Consumer<Throwable> {
    private fun swallow(error: Throwable) {
        if (_showSwallowedExceptions) {
            Timber.e(error, "Swallow uncaught exception:")
        }
    }

    private fun rethrow(error: Throwable) {
        Thread
            .currentThread()
            .uncaughtExceptionHandler!!
            .uncaughtException(Thread.currentThread(), error)
    }

    private tailrec fun unwrap(error: Throwable): Throwable {
        return when (error) {
            is UndeliverableException -> unwrap(error.cause!!)
            is RuntimeException -> error.cause?.let(this::unwrap) ?: error
            else -> error
        }
    }

    override fun accept(error: Throwable?) {
        if (error == null) {
            // No error.
            return
        }

        if (error is UnknownHostException ||
            error is SocketTimeoutException ||
            error is ConnectException
        ) {
            rethrow(NoConnectivityException)
            return
        }

        val internalError = unwrap(error)

        if (internalError is IOException) {
            // Irrelevant network problem or API that throws on cancellation.
            swallow(error)
            return
        }

        if (internalError is InterruptedException
            || internalError is CancellationException
            || internalError is OperationCanceledException
        ) {
            // Some blocking code was interrupted by a dispose call.
            swallow(error)
            return
        }

        if (internalError is NullPointerException
            || internalError is IllegalArgumentException
            || internalError is ContractException
        ) {
            // That's likely a bug in the application.
            rethrow(internalError)
            return
        }

        if (internalError is IllegalStateException) {
            // That's a bug in RxJava or in a custom operator.
            rethrow(internalError)
            return
        }

        Timber.e(internalError, "Unhandled error detected.")

        rethrow(internalError)
    }
}
