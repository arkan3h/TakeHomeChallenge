package com.arkan.takehomechallenge.data.mapper

import com.arkan.takehomechallenge.data.model.Character
import com.arkan.takehomechallenge.data.source.network.model.character.Result

fun Result?.toCharacter() =
    Character(
        id = this?.id ?: 0,
        name = this?.name.orEmpty(),
        status = this?.status.orEmpty(),
        image = this?.image.orEmpty(),
        gender = this?.gender.orEmpty(),
        origin = this?.origin?.name.orEmpty(),
        location = this?.location?.name.orEmpty(),
        species = this?.species.orEmpty(),
    )

fun Collection<Result>?.toCharacter() =
    this?.map {
        it.toCharacter()
    } ?: listOf()
