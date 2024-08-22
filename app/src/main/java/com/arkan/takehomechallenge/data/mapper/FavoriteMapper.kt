package com.arkan.takehomechallenge.data.mapper

import com.arkan.takehomechallenge.data.model.Character
import com.arkan.takehomechallenge.data.source.local.entity.FavoriteEntity

fun Character.toFavoriteEntity() =
    FavoriteEntity(
        id = this.id,
        name = this.name,
        status = this.status,
        image = this.image,
        gender = this.gender,
        origin = this.origin,
        location = this.location,
        species = this.species,
        createdAt = System.currentTimeMillis(),
    )

fun FavoriteEntity.toCharacter() =
    Character(
        id = this.id,
        name = this.name,
        status = this.status,
        image = this.image,
        gender = this.gender,
        origin = this.origin,
        location = this.location,
        species = this.species,
    )

fun List<FavoriteEntity>.toFavoriteList() = this.map { it.toCharacter() }
