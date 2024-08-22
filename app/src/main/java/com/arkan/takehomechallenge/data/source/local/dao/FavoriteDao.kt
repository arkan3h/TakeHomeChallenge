package com.arkan.takehomechallenge.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arkan.takehomechallenge.data.source.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM FAVORITE ORDER BY createdAt DESC")
    fun getAllFavorite(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM FAVORITE WHERE id = :id ")
    fun checkFavoriteById(id: Int): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(bookmark: FavoriteEntity): Long

    @Delete
    suspend fun deleteFavorite(bookmark: FavoriteEntity): Int

    @Query("DELETE FROM FAVORITE WHERE id = :id")
    suspend fun removeFavorite(id: Int): Int

    @Query("DELETE FROM FAVORITE")
    suspend fun deleteAll()
}
