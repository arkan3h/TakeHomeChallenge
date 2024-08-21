package com.arkan.takehomechallenge.data.source.network.model.character

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Location(
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?,
)
