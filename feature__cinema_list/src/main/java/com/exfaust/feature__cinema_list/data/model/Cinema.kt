package com.exfaust.feature__cinema_list.data.model

import com.exfaust.base__cinema.CinemaId

data class Cinema(
    val id: CinemaId,
    val name: String,
    val address: String
)
