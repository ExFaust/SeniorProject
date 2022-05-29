package com.exfaust.core_android

import android.annotation.SuppressLint
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <Item : Any> getRxAsyncDiffer(
    itemCallback: DiffUtil.ItemCallback<Item>
) = AsyncDifferConfig
    .Builder(itemCallback)
    .setMainThreadScheduler()
    .setComputationScheduler()
    .build()

@SuppressLint("RestrictedApi")
fun <T> AsyncDifferConfig.Builder<T>.setMainThreadScheduler(): AsyncDifferConfig.Builder<T> {
    val main = AndroidSchedulers.mainThread()

    return setMainThreadExecutor { main.scheduleDirect(it) }
}

@SuppressLint("RestrictedApi")
fun <T> AsyncDifferConfig.Builder<T>.setComputationScheduler(): AsyncDifferConfig.Builder<T> {
    val main = Schedulers.computation()

    return setBackgroundThreadExecutor { main.scheduleDirect(it) }
}