package com.exfaust.base__cinema

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter

@ProvidedTypeConverter
class CinemaIdConverter {
    @TypeConverter
    fun from(value: CinemaId): String = value.unwrap().toString()

    @TypeConverter
    fun to(data: String): CinemaId = CinemaId.restore(data.toLong())
}