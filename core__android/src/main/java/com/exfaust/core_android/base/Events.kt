package com.exfaust.core_android.base

import android.content.Context
import com.exfaust.core.exception.SilentException
import com.exfaust.core_android.R
import com.exfaust.core_api.HttpUserVisibleException
import ru.mosgorpass.utils.exception.NoConnectivityException

/**
 * Описывает некоторые типовые состояние вью
 */
sealed class Events {
    object HideLoading : Events()

    data class Error(
        val throwable: Throwable?
    ) : Events()

    object Loading : Events()

    data class ErrorWithMessage(
        val message: String
    ) : Events()

    object Unauthorized : Events()

    object NoConnectivity : Events()

    data class ErrorWithExit(
        val throwable: Throwable?
    ) : Events()

    object Exit : Events()

    fun getMessage(context: Context, throwable: Throwable?): String? =
        when (throwable) {
            is SilentException -> null
            is HttpUserVisibleException -> throwable.errorDescription
            else -> context.getString((throwable as? NoConnectivityException)?.let { R.string.application___error__no_internet_connection }
                ?: R.string.application___error__common)
        }
}