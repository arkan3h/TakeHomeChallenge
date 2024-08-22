package com.arkan.takehomechallenge.data.datasource

import com.arkan.takehomechallenge.data.source.network.model.character.CharacterResponse
import com.arkan.takehomechallenge.data.source.network.services.THCApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CharacterApiDataSourceTest {
    @MockK
    lateinit var service: THCApiService

    private lateinit var dataSource: CharacterDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = CharacterApiDataSource(service)
    }

    @Test
    fun getAllCharacter() {
        runTest {
            val mockResponse = mockk<CharacterResponse>(relaxed = true)
            coEvery {
                service.getAllCharacter(any())
            } returns mockResponse

            val actualResult = dataSource.getAllCharacter(1)
            coVerify {
                service.getAllCharacter(any())
            }
            assertEquals(actualResult, mockResponse)
        }
    }

    @Test
    fun searchCharacter() {
        runTest {
            val mockResponse = mockk<CharacterResponse>(relaxed = true)
            coEvery {
                service.searchCharacter(any(), any())
            } returns mockResponse

            val actualResult = dataSource.searchCharacter(1, "Arkan")
            coVerify {
                service.searchCharacter(any(), any())
            }
            assertEquals(actualResult, mockResponse)
        }
    }
}
