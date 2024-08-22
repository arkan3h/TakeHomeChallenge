package com.arkan.takehomechallenge.data.datasource

import app.cash.turbine.test
import com.arkan.takehomechallenge.data.source.local.dao.FavoriteDao
import com.arkan.takehomechallenge.data.source.local.entity.FavoriteEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FavoriteDatabaseDataSourceTest {
    @MockK
    lateinit var dao: FavoriteDao

    private lateinit var ds: FavoriteDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        ds = FavoriteDatabaseDataSource(dao)
    }

    private val entity1 = mockk<FavoriteEntity>()
    private val entity2 = mockk<FavoriteEntity>()
    private val listEntity = listOf(entity1, entity2)

    @Test
    fun getAllFavorite() {
        val mockFlow =
            flow {
                emit(listEntity)
            }

        every {
            dao.getAllFavorite()
        } returns mockFlow

        runTest {
            ds.getAllFavorite().test {
                val result = awaitItem()
                assertEquals(listEntity.size, result.size)
                assertEquals(entity1, result[0])
                assertEquals(entity2, result[1])
                awaitComplete()
            }
        }
    }

    @Test
    fun checkFavoriteById() {
        val mockFlow =
            flow {
                emit(listEntity)
            }

        every {
            dao.checkFavoriteById(any())
        } returns mockFlow

        runTest {
            ds.checkFavoriteById(111).test {
                val result = awaitItem()
                assertEquals(listEntity.size, result.size)
                assertEquals(entity1, result[0])
                assertEquals(entity2, result[1])
                awaitComplete()
            }
        }
    }

    @Test
    fun addFavorite() {
        runTest {
            val mockEntity = mockk<FavoriteEntity>()
            coEvery {
                dao.addFavorite(any())
            } returns 1

            val result = ds.addFavorite(mockEntity)
            coVerify {
                dao.addFavorite(any())
                assertEquals(1, result)
            }
        }
    }

    @Test
    fun deleteFavorite() {
        runTest {
            val mockEntity = mockk<FavoriteEntity>()
            coEvery {
                dao.deleteFavorite(any())
            } returns 1

            val result = ds.deleteFavorite(mockEntity)
            coVerify {
                dao.deleteFavorite(any())
                assertEquals(1, result)
            }
        }
    }

    @Test
    fun removeFavorite() {
        runTest {
            coEvery {
                dao.removeFavorite(any())
            } returns 1

            val result = ds.removeFavorite(111)
            coVerify {
                dao.removeFavorite(any())
                assertEquals(1, result)
            }
        }
    }

    @Test
    fun deleteAll() {
        runTest {
            coEvery {
                dao.deleteAll()
            } returns Unit

            val result = ds.deleteAll()
            coVerify {
                dao.deleteAll()
                assertEquals(Unit, result)
            }
        }
    }
}
