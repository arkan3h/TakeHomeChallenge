package com.arkan.takehomechallenge.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.arkan.takehomechallenge.data.datasource.CharacterDataSource
import com.arkan.takehomechallenge.data.model.Character
import com.arkan.takehomechallenge.data.paging.CharacterPagingSource
import com.arkan.takehomechallenge.data.paging.SearchCharacterPagingSource
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getAllCharacter(): Flow<PagingData<Character>>

    fun searchCharacter(name: String?): Flow<PagingData<Character>>
}

class CharacterRepositoryImpl(
    private val dataSource: CharacterDataSource,
) : CharacterRepository {
    override fun getAllCharacter(): Flow<PagingData<Character>> {
        return Pager(
            config =
                PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false,
                ),
            pagingSourceFactory = { CharacterPagingSource(dataSource) },
        ).flow
    }

    override fun searchCharacter(name: String?): Flow<PagingData<Character>> {
        return Pager(
            config =
                PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false,
                ),
            pagingSourceFactory = { SearchCharacterPagingSource(dataSource, name) },
        ).flow
    }
}
