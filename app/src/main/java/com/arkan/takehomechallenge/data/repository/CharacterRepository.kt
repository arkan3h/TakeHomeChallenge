package com.arkan.takehomechallenge.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.arkan.takehomechallenge.data.datasource.CharacterDataSource
import com.arkan.takehomechallenge.data.mapper.toCharacter
import com.arkan.takehomechallenge.data.model.Character
import com.arkan.takehomechallenge.data.paging.CharacterPagingSource
import com.arkan.takehomechallenge.utils.ResultWrapper
import com.arkan.takehomechallenge.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CharacterRepository {
    fun getAllCharacter(): Flow<PagingData<Character>>

    fun searchCharacter(name: String): Flow<ResultWrapper<List<Character>>>
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

    override fun searchCharacter(name: String): Flow<ResultWrapper<List<Character>>> {
        return proceedFlow {
            dataSource.searchCharacter(name).results.toCharacter()
        }
    }
}
