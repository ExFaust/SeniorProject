package com.exfaust.base__cinema

import com.exfaust.core.wrapped.WrappedCompatValue

class CinemaId
private constructor(value: Long) : WrappedCompatValue<Long, CinemaId>(value) {
    companion object {
        fun restore(value: Long): CinemaId = CinemaId(value)
    }
}