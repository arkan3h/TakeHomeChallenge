package com.arkan.takehomechallenge.data.repository

import app.cash.turbine.test
import com.arkan.takehomechallenge.data.datasource.FavoriteDataSource
import com.arkan.takehomechallenge.data.model.Character
import com.arkan.takehomechallenge.data.source.local.entity.FavoriteEntity
import com.arkan.takehomechallenge.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FavoriteRepositoryImplTest {
    @MockK
    lateinit var ds: FavoriteDataSource

    private lateinit var repo: FavoriteRepository

    private val entity1 =
        FavoriteEntity(
            id = 1,
            name = "Arkan",
            gender = "Male",
            origin = "East Java",
            species = "Human",
            location = "East Java",
            status = "Alive",
            image = "aaa",
            createdAt = 1,
        )
    private val entity2 =
        FavoriteEntity(
            id = 2,
            name = "Rhyma",
            gender = "Female",
            origin = "East Java",
            species = "Human",
            location = "East Java",
            status = "Alive",
            image = "bbb",
            createdAt = 2,
        )
    private val mockFavoriteList = listOf(entity1, entity2)
    private val mockFavorite =
        Character(
            id = 3,
            name = "Mari",
            gender = "Female",
            origin = "East Java",
            species = "Human",
            location = "East Java",
            status = "Alive",
            image = "ccc",
        )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repo = FavoriteRepositoryImpl(ds)
    }

    @Test
    fun getAllFavoriteLoading() {
        val mockFlow =
            flow {
                emit(mockFavoriteList)
            }
        every {
            ds.getAllFavorite()
        } returns mockFlow

        runTest {
            repo.getAllFavorite().map {
                delay(100)
                it
            }.test {
                delay(150)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                assertEquals(null, actualData.payload?.size)
                verify { ds.getAllFavorite() }
            }
        }
    }

    @Test
    fun getAllFavoriteSuccess() {
        val mockFlow =
            flow {
                emit(mockFavoriteList)
            }
        every {
            ds.getAllFavorite()
        } returns mockFlow

        runTest {
            repo.getAllFavorite().map {
                delay(100)
                it
            }.test {
                delay(250)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(mockFavoriteList.size, actualData.payload?.size)
                verify { ds.getAllFavorite() }
            }
        }
    }

    @Test
    fun getAllFavoriteError() {
        every {
            ds.getAllFavorite()
        } returns
            flow {
                throw IllegalStateException("Mock Error")
            }

        runTest {
            repo.getAllFavorite().map {
                delay(100)
                it
            }.test {
                delay(250)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                verify { ds.getAllFavorite() }
            }
        }
    }

    @Test
    fun getAllFavoriteEmpty() {
        val mockFavoriteList = listOf<FavoriteEntity>()
        val mockFlow =
            flow {
                emit(mockFavoriteList)
            }
        every {
            ds.getAllFavorite()
        } returns mockFlow

        runTest {
            repo.getAllFavorite().map {
                delay(100)
                it
            }.test {
                delay(250)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Empty)
                assertEquals(0, actualData.payload?.size)
                verify { ds.getAllFavorite() }
            }
        }
    }

    @Test
    fun checkFavoriteById() {
        val mockFlow =
            flow {
                emit(mockFavoriteList)
            }

        every {
            ds.checkFavoriteById(any())
        } returns mockFlow

        runTest {
            repo.checkFavoriteById(111).test {
                val result = awaitItem()
                assertEquals(mockFavoriteList.size, result.size)
                assertEquals(entity1, result[0])
                assertEquals(entity2, result[1])
                awaitComplete()
            }
        }
    }

    @Test
    fun addFavoriteLoading() {
        val mockProduct = mockk<Character>(relaxed = true)
        every {
            mockProduct.id
        } returns 1

        runTest {
            coEvery {
                ds.addFavorite(any())
            } returns 1
            repo.addFavorite(mockFavorite).map {
                delay(100)
                it
            }.test {
                delay(150)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                assertEquals(null, actualData.payload)
                coVerify(atLeast = 1) { ds.addFavorite(any()) }
            }
        }
    }

    @Test
    fun addFavoriteSuccess() {
        val mockProduct = mockk<Character>(relaxed = true)
        every {
            mockProduct.id
        } returns 1

        runTest {
            coEvery {
                ds.addFavorite(any())
            } returns 1
            repo.addFavorite(mockFavorite).map {
                delay(100)
                it
            }.test {
                delay(250)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify(atLeast = 1) { ds.addFavorite(any()) }
            }
        }
    }

    @Test
    fun addFavoriteError() {
        val mockProduct = mockk<Character>(relaxed = true)
        every {
            mockProduct.id
        } returns 1

        runTest {
            coEvery {
                ds.addFavorite(any())
            } throws IllegalStateException("Mock Error")
            repo.addFavorite(mockFavorite).map {
                delay(100)
                it
            }.test {
                delay(250)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify(atLeast = 1) { ds.addFavorite(any()) }
            }
        }
    }

    @Test
    fun deleteFavoriteLoading() {
        coEvery {
            ds.deleteFavorite(any())
        } returns 1

        runTest {
            repo.deleteFavorite(mockFavorite).map {
                delay(100)
                it
            }.test {
                delay(150)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                coVerify(atLeast = 1) { ds.deleteFavorite(any()) }
            }
        }
    }

    @Test
    fun deleteFavoriteSuccess() {
        coEvery {
            ds.deleteFavorite(any())
        } returns 1

        runTest {
            repo.deleteFavorite(mockFavorite).map {
                delay(100)
                it
            }.test {
                delay(250)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                coVerify(atLeast = 1) { ds.deleteFavorite(any()) }
            }
        }
    }

    @Test
    fun deleteFavoriteError() {
        coEvery {
            ds.deleteFavorite(any())
        } throws IllegalStateException("Mock Error")

        runTest {
            repo.deleteFavorite(mockFavorite).map {
                delay(100)
                it
            }.test {
                delay(250)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify(atLeast = 1) { ds.deleteFavorite(any()) }
            }
        }
    }

    @Test
    fun removeFavoriteLoading() {
        coEvery {
            ds.removeFavorite(any())
        } returns 1

        runTest {
            repo.removeFavorite(mockFavorite.id).map {
                delay(100)
                it
            }.test {
                delay(150)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                coVerify(atLeast = 1) { ds.removeFavorite(any()) }
            }
        }
    }

    @Test
    fun removeFavoriteSuccess() {
        coEvery {
            ds.removeFavorite(any())
        } returns 1

        runTest {
            repo.removeFavorite(mockFavorite.id).map {
                delay(100)
                it
            }.test {
                delay(250)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                coVerify(atLeast = 1) { ds.removeFavorite(any()) }
            }
        }
    }

    @Test
    fun removeFavoriteError() {
        coEvery {
            ds.removeFavorite(any())
        } throws IllegalStateException("Mock Error")

        runTest {
            repo.removeFavorite(mockFavorite.id).map {
                delay(100)
                it
            }.test {
                delay(250)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify(atLeast = 1) { ds.removeFavorite(any()) }
            }
        }
    }

    @Test
    fun deleteAllLoading() {
        coEvery {
            ds.deleteAll()
        } returns Unit

        runTest {
            repo.deleteAll().map {
                delay(100)
                it
            }.test {
                delay(150)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                coVerify(atLeast = 1) { ds.deleteAll() }
            }
        }
    }

    @Test
    fun deleteAllSuccess() {
        coEvery {
            ds.deleteAll()
        } returns Unit

        runTest {
            repo.deleteAll().map {
                delay(100)
                it
            }.test {
                delay(250)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                coVerify(atLeast = 1) { ds.deleteAll() }
            }
        }
    }

    @Test
    fun deleteAllError() {
        coEvery {
            ds.deleteAll()
        } throws IllegalStateException("Mock Error")

        runTest {
            repo.deleteAll().map {
                delay(100)
                it
            }.test {
                delay(250)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify(atLeast = 1) { ds.deleteAll() }
            }
        }
    }
}
