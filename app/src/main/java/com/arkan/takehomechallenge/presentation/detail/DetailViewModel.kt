package com.arkan.takehomechallenge.presentation.detail

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arkan.takehomechallenge.data.model.Character
import com.arkan.takehomechallenge.data.repository.FavoriteRepository
import kotlinx.coroutines.Dispatchers

class DetailViewModel(
    extras: Bundle?,
    private val repository: FavoriteRepository,
) : ViewModel() {
    val detail = extras?.getParcelable<Character>(DetailActivity.EXTRA_CHARACTER)

    fun addBookmark(item: Character) = repository.addFavorite(item).asLiveData(Dispatchers.IO)

    fun checkMovieBookmark(id: Int) = repository.checkFavoriteById(id).asLiveData(Dispatchers.IO)

    fun removeBookmark(id: Int) = repository.removeFavorite(id).asLiveData(Dispatchers.IO)
}
