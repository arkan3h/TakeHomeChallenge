package com.arkan.takehomechallenge.presentation.detail

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.arkan.takehomechallenge.data.model.Character

class DetailViewModel(
    extras: Bundle?,
) : ViewModel() {
    val detail = extras?.getParcelable<Character>(DetailActivity.EXTRA_CHARACTER)
}
