package com.exfaust.core_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun LayoutInflater.inflateInto(
    @LayoutRes layoutId: Int,
    parent: ViewGroup
): View = inflate(layoutId, parent, true)

fun LayoutInflater.inflateBy(
    @LayoutRes layoutId: Int,
    parent: ViewGroup?
): View = inflate(layoutId, parent, false)

fun inflateInto(
    @LayoutRes layoutId: Int,
    parent: ViewGroup
): View = parent.context.inflateInto(layoutId, parent)

fun inflateBy(
    @LayoutRes layoutId: Int,
    parent: ViewGroup
): View = parent.context.inflateBy(layoutId, parent)