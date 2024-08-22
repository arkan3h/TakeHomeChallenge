package com.arkan.takehomechallenge.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arkan.takehomechallenge.data.datasource.CharacterDataSource
import com.arkan.takehomechallenge.data.mapper.toCharacter
import com.arkan.takehomechallenge.data.model.Character

class SearchCharacterPagingSource(
    private val dataSource: CharacterDataSource,
    private val name: String?,
) : PagingSource<Int, Character>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val currentPage = params.key ?: 1
            val response = dataSource.searchCharacter(currentPage, name)
            val data = response.results.toCharacter()

            LoadResult.Page(
                data = data,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (data.isEmpty()) null else currentPage + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.nextKey
        }
    }
}
