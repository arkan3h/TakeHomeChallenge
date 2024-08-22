package com.arkan.takehomechallenge.data.repository

import com.arkan.takehomechallenge.data.datasource.FavoriteDataSource
import com.arkan.takehomechallenge.data.mapper.toFavoriteEntity
import com.arkan.takehomechallenge.data.mapper.toFavoriteList
import com.arkan.takehomechallenge.data.model.Character
import com.arkan.takehomechallenge.data.source.local.entity.FavoriteEntity
import com.arkan.takehomechallenge.utils.ResultWrapper
import com.arkan.takehomechallenge.utils.proceed
import com.arkan.takehomechallenge.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface FavoriteRepository {
    fun getAllFavorite(): Flow<ResultWrapper<List<Character>>>

    fun checkFavoriteById(id: Int): Flow<List<FavoriteEntity>>

    fun addFavorite(favorite: Character): Flow<ResultWrapper<Boolean>>

    fun deleteFavorite(favorite: Character): Flow<ResultWrapper<Boolean>>

    fun removeFavorite(id: Int): Flow<ResultWrapper<Boolean>>

    fun deleteAll(): Flow<ResultWrapper<Boolean>>
}

class FavoriteRepositoryImpl(private val datasource: FavoriteDataSource) : FavoriteRepository {
    override fun getAllFavorite(): Flow<ResultWrapper<List<Character>>> {
        return datasource.getAllFavorite()
            .map {
                proceed {
                    val result = it.toFavoriteList()
                    result
                }
            }.map {
                if (it.payload?.isEmpty() == false) return@map it
                ResultWrapper.Empty(it.payload)
            }.catch {
                emit(ResultWrapper.Error(Exception(it)))
            }.onStart {
                emit(ResultWrapper.Loading())
            }
    }

    override fun checkFavoriteById(id: Int): Flow<List<FavoriteEntity>> {
        return datasource.checkFavoriteById(id)
    }

    override fun addFavorite(favorite: Character): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val affectedRow =
                datasource.addFavorite(
                    FavoriteEntity(
                        id = favorite.id,
                        name = favorite.name,
                        status = favorite.status,
                        image = favorite.image,
                        gender = favorite.gender,
                        origin = favorite.origin,
                        location = favorite.location,
                        species = favorite.species,
                        createdAt = System.currentTimeMillis(),
                    ),
                )
            affectedRow > 0
        }
    }

    override fun deleteFavorite(favorite: Character): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { datasource.deleteFavorite(favorite.toFavoriteEntity()) > 0 }
    }

    override fun removeFavorite(id: Int): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { datasource.removeFavorite(id) > 0 }
    }

    override fun deleteAll(): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            datasource.deleteAll()
            true
        }
    }
}
