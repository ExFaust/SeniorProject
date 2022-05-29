package com.exfaust.feature_cinema_info.data.serialization

import android.net.Uri
import com.exfaust.base__cinema.CinemaId
import com.exfaust.core.email.Email
import com.exfaust.feature_cinema_info.data.model.Cinema
import com.exfaust.serialization.deserializeTypedRequired
import com.exfaust.serialization.registerTypeAdapter
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import kotlinx.collections.immutable.PersistentList
import java.lang.reflect.Type
import javax.annotation.CheckReturnValue

fun GsonBuilder.registerCinemaInfoApiSerializationAdapters(): GsonBuilder =
    this
        .registerTypeAdapter(CinemaDeserializer)

private data class CinemaBody(
    @SerializedName("global_id")
    val id: CinemaId,
    @SerializedName("Cells")
    val cells: Cells
) {
    data class Cells(
        @SerializedName("CommonName")
        val name: String,
        @SerializedName("ObjectAddress")
        val addressObject: PersistentList<Address>,
        @SerializedName("Email")
        val emails: PersistentList<EmailObject>,
        @SerializedName("WebSite")
        val site: Uri
    )

    data class EmailObject(
        @SerializedName("Email")
        val email: Email
    )

    data class Address(
        @SerializedName("Address")
        val address: String
    )
}

private object CinemaDeserializer : JsonDeserializer<Cinema> {
    @CheckReturnValue
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Cinema {
        val body: CinemaBody = context.deserializeTypedRequired(json)

        return Cinema(
            id = body.id,
            address = body.cells.addressObject.firstOrNull()?.address.orEmpty(),
            name = body.cells.name,
            email = body.cells.emails.firstOrNull()?.email,
            site = body.cells.site,
            //Картинок не оказалось в апи
            image = Uri.parse("https://travel-or-die.ru/wp-content/uploads/2019/11/Ekskursiya-na-plato-Bermamyt-iz-Kislovodska-otzyvy-tsena.jpg")
        )
    }
}

