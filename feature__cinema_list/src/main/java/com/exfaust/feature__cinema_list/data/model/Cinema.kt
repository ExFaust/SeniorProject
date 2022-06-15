package com.exfaust.feature__cinema_list.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.exfaust.base__cinema.CinemaId

@Entity
data class Cinema(
    @PrimaryKey
    val id: CinemaId,
    val name: String,
    val address: String
)
