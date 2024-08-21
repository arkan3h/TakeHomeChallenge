package com.arkan.takehomechallenge.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkan.takehomechallenge.data.model.Character
import com.arkan.takehomechallenge.data.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class MainViewModel(
    private val repository: CharacterRepository,
) : ViewModel() {
    fun getAllCharacter(): Flow<PagingData<Character>> {
        return repository.getAllCharacter().cachedIn(viewModelScope)
    }
}
