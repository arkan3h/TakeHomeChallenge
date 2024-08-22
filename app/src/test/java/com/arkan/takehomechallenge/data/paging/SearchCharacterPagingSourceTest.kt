package com.arkan.takehomechallenge.data.paging

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arkan.takehomechallenge.data.datasource.CharacterDataSource
import com.arkan.takehomechallenge.data.model.Character
import com.arkan.takehomechallenge.data.source.network.model.character.Location
import com.arkan.takehomechallenge.data.source.network.model.character.Origin
import com.arkan.takehomechallenge.data.source.network.model.character.Result
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SearchCharacterPagingSourceTest {
    @MockK
    lateinit var ds: CharacterDataSource

    private lateinit var paging: SearchCharacterPagingSource

    private val name = "Arkan"
    private val character1 =
        Character(
            id = 1,
            name = "Arkan",
            gender = "Male",
            origin = "East Java",
            species = "Human",
            location = "East Java",
            status = "Alive",
            image = "aaa",
        )
    private val character2 =
        Character(
            id = 2,
            name = "Rhyma",
            gender = "Female",
            origin = "East Java",
            species = "Human",
            location = "East Java",
            status = "Alive",
            image = "bbb",
        )
    private val characterList = listOf(character1, character2)
    private val originResponse =
        Origin(
            name = "East Java",
            url = "",
        )
    private val locationResponse =
        Location(
            name = "East Java",
            url = "",
        )
    private val characterResponse1 =
        Result(
            id = 1,
            name = "Arkan",
            gender = "Male",
            origin = originResponse,
            species = "Human",
            location = locationResponse,
            status = "Alive",
            image = "aaa",
            type = "",
            url = "",
            created = "",
            episode = listOf("", ""),
        )
    private val characterResponse2 =
        Result(
            id = 2,
            name = "Rhyma",
            gender = "Female",
            origin = originResponse,
            species = "Human",
            location = locationResponse,
            status = "Alive",
            image = "bbb",
            type = "",
            url = "",
            created = "",
            episode = listOf("", ""),
        )
    private val characterListResponse = listOf(characterResponse1, characterResponse2)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        paging = SearchCharacterPagingSource(ds, name)
    }

    @Test
    fun loadSuccess() {
        coEvery { ds.searchCharacter(1, name) } returns
            mockk {
                every { results } returns characterListResponse
            }

        runTest {
            val result =
                paging.load(
                    PagingSource.LoadParams.Refresh(
                        key = null,
                        loadSize = 20,
                        placeholdersEnabled = false,
                    ),
                )

            assertEquals(
                PagingSource.LoadResult.Page(
                    data = characterList,
                    prevKey = null,
                    nextKey = 2,
                ),
                result,
            )
        }
    }

    @Test
    fun loadError() {
        coEvery { ds.searchCharacter(any(), any()) } throws Exception("Mock Error")

        runTest {
            val result =
                paging.load(
                    PagingSource.LoadParams.Refresh(
                        key = 1,
                        loadSize = 20,
                        placeholdersEnabled = false,
                    ),
                )
            assertTrue(result is PagingSource.LoadResult.Error)
        }
    }

    @Test
    fun getRefreshKeySuccess() {
        val state =
            PagingState(
                pages =
                    listOf(
                        PagingSource.LoadResult.Page(
                            data = characterList,
                            prevKey = null,
                            nextKey = 2,
                        ),
                    ),
                anchorPosition = 1,
                config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                leadingPlaceholderCount = 1,
            )

        val refreshKey = paging.getRefreshKey(state)
        assertEquals(2, refreshKey)
    }

    @Test
    fun getRefreshKeyError() {
        val state =
            PagingState(
                pages =
                    listOf(
                        PagingSource.LoadResult.Page(
                            data = characterList,
                            prevKey = null,
                            nextKey = 2,
                        ),
                    ),
                anchorPosition = null,
                config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                leadingPlaceholderCount = 1,
            )

        val refreshKey = paging.getRefreshKey(state)
        assertEquals(null, refreshKey)
    }
}
