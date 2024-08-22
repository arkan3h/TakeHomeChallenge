package com.arkan.takehomechallenge.data.datasource

import com.arkan.takehomechallenge.data.source.local.dao.FavoriteDao
import com.arkan.takehomechallenge.data.source.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteDataSource {
    fun getAllFavorite(): Flow<List<FavoriteEntity>>

    fun checkFavoriteById(id: Int): Flow<List<FavoriteEntity>>

    suspend fun addFavorite(favorite: FavoriteEntity): Long

    suspend fun deleteFavorite(favorite: FavoriteEntity): Int

    suspend fun removeFavorite(id: Int): Int

    suspend fun deleteAll()
}

class FavoriteDatabaseDataSource(
    private val dao: FavoriteDao,
) : FavoriteDataSource {
    override fun getAllFavorite(): Flow<List<FavoriteEntity>> = dao.getAllFavorite()

    override fun checkFavoriteById(id: Int): Flow<List<FavoriteEntity>> = dao.checkFavoriteById(id)

    override suspend fun addFavorite(favorite: FavoriteEntity): Long = dao.addFavorite(favorite)

    override suspend fun deleteFavorite(favorite: FavoriteEntity): Int = dao.deleteFavorite(favorite)

    override suspend fun removeFavorite(id: Int): Int = dao.removeFavorite(id)

    override suspend fun deleteAll() = dao.deleteAll()
}
