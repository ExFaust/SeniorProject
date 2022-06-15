package com.exfaust.seniorproject

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.exfaust.base__cinema.CinemaIdConverter
import com.exfaust.feature__cinema_list.data.model.Cinema
import com.exfaust.feature__cinema_list.db.CinemaListItemDao

@Database(
    entities = [Cinema::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CinemaIdConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cinemaListItemDao(): CinemaListItemDao
}