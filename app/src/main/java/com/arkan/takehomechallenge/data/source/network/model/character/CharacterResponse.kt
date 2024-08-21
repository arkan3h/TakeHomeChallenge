package com.arkan.takehomechallenge.data.source.network.model.character

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CharacterResponse(
    @SerializedName("info")
    val info: Info?,
    @SerializedName("results")
    val results: List<Result>?,
)
