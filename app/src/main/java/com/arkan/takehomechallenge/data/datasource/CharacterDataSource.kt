package com.arkan.takehomechallenge.data.datasource

import com.arkan.takehomechallenge.data.source.network.model.character.CharacterResponse
import com.arkan.takehomechallenge.data.source.network.services.THCApiService

interface CharacterDataSource {
    suspend fun getAllCharacter(page: Int): CharacterResponse

    suspend fun searchCharacter(name: String): CharacterResponse
}

class CharacterApiDataSource(
    private val service: THCApiService,
) : CharacterDataSource {
    override suspend fun getAllCharacter(page: Int): CharacterResponse {
        return service.getAllCharacter(page)
    }

    override suspend fun searchCharacter(name: String): CharacterResponse {
        return service.searchCharacter(name)
    }
}
