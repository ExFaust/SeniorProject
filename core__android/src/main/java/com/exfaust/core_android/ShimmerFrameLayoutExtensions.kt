package com.exfaust.core_android

import com.facebook.shimmer.ShimmerFrameLayout

var ShimmerFrameLayout.isShimmerAnimated
    get() = isShimmerStarted
    set(value) = if (value) {
        startShimmer()
    } else {
        stopShimmer()
    }