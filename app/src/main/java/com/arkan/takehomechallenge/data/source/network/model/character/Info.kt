package com.arkan.takehomechallenge.data.source.network.model.character

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Info(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("next")
    val next: String?,
    @SerializedName("pages")
    val pages: Int?,
    @SerializedName("prev")
    val prev: Any?,
)
