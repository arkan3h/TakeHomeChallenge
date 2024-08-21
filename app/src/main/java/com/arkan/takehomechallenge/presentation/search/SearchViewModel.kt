package com.arkan.takehomechallenge.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkan.takehomechallenge.data.model.Character
import com.arkan.takehomechallenge.data.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class SearchViewModel(
    private val repository: CharacterRepository,
) : ViewModel() {
    fun searchCharacter(name: String?): Flow<PagingData<Character>> {
        return repository.searchCharacter(name).cachedIn(viewModelScope)
    }
}
