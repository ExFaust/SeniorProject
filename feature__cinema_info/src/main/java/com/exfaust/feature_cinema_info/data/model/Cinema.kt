package com.exfaust.feature_cinema_info.data.model

import android.net.Uri
import com.exfaust.base__cinema.CinemaId
import com.exfaust.core.email.Email

data class Cinema(
    val id: CinemaId,
    val name: String,
    val address: String,
    val site: Uri,
    val email: Email?,
    val image: Uri
)
