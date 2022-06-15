package com.exfaust.feature__cinema_list.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.exfaust.feature__cinema_list.data.model.Cinema
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CinemaListItemDao {
    @Query("SELECT * FROM Cinema")
    fun getAllCinema(): Single<List<Cinema>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cinemas: List<Cinema>): Completable

    @Query("DELETE FROM Cinema")
    fun clear(): Completable
}