package com.arkan.takehomechallenge.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arkan.takehomechallenge.data.repository.FavoriteRepository
import kotlinx.coroutines.Dispatchers

class FavoriteViewModel(
    private val repository: FavoriteRepository,
) : ViewModel() {
    fun getAllFavorite() = repository.getAllFavorite().asLiveData(Dispatchers.IO)

    fun removeFavorite() = repository.deleteAll().asLiveData(Dispatchers.IO)
}
