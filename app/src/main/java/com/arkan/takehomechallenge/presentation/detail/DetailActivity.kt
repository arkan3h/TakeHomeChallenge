package com.arkan.takehomechallenge.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import coil.load
import com.arkan.takehomechallenge.R
import com.arkan.takehomechallenge.data.model.Character
import com.arkan.takehomechallenge.databinding.ActivityDetailBinding
import com.arkan.takehomechallenge.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailActivity : AppCompatActivity() {
    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    private val viewModel: DetailViewModel by viewModel {
        parametersOf(intent.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        clickListener()
        bindCharacter(viewModel.detail)
    }

    private fun bindCharacter(item: Character?) {
        item?.let {
            binding.ivCharacterImage.load(it.image) {
                crossfade(true)
            }
            binding.tvCharacterName.text = it.name
            binding.tvCharacterDesc.text =
                buildString {
                    append("Species : ")
                    append(it.species)
                    append("\nGender : ")
                    append(it.gender)
                    append("\nStatus : ")
                    append(it.status)
                    append("\nOrigin : ")
                    append(it.origin)
                    append("\nLocation : ")
                    append(it.location)
                }
            checkFavorite(it)
        }
    }

    private fun checkFavorite(data: Character) {
        viewModel.checkMovieBookmark(data.id).observe(
            this,
        ) { isFavorite ->
            if (isFavorite.isEmpty()) {
                binding.btnDetailFavorite.load(R.drawable.ic_favorite_outline)
                setClickAddFavorite(data)
            } else {
                binding.btnDetailFavorite.load(R.drawable.ic_favorite)
                setClickRemoveFavorite(data)
            }
        }
    }

    private fun setClickAddFavorite(data: Character) {
        binding.btnDetailFavorite.setOnClickListener {
            addBookmark(data)
        }
    }

    private fun setClickRemoveFavorite(data: Character) {
        binding.btnDetailFavorite.setOnClickListener {
            removeBookmark(data)
        }
    }

    private fun clickListener() {
        binding.btnDetailBack.setOnClickListener {
            backNavigation()
        }
    }

    private fun addBookmark(data: Character) {
        viewModel.addBookmark(data).observe(this) {
            it.proceedWhen(
                doOnLoading = {
                    binding.pbFavoriteLoading.isVisible = true
                    binding.btnDetailFavorite.isVisible = false
                },
                doOnSuccess = {
                    binding.pbFavoriteLoading.isVisible = false
                    binding.btnDetailFavorite.isVisible = true
                    Toast.makeText(
                        this,
                        "Successfully add to favorite",
                        Toast.LENGTH_SHORT,
                    ).show()
                    checkFavorite(data)
                },
                doOnError = {
                    binding.pbFavoriteLoading.isVisible = false
                    binding.btnDetailFavorite.isVisible = true
                    Toast.makeText(
                        this,
                        "Failed add to favorite",
                        Toast.LENGTH_SHORT,
                    ).show()
                    checkFavorite(data)
                },
            )
        }
    }

    private fun removeBookmark(data: Character) {
        viewModel.removeBookmark(data.id).observe(this) {
            it.proceedWhen(
                doOnLoading = {
                    binding.pbFavoriteLoading.isVisible = true
                    binding.btnDetailFavorite.isVisible = false
                },
                doOnSuccess = {
                    binding.pbFavoriteLoading.isVisible = false
                    binding.btnDetailFavorite.isVisible = true
                    Toast.makeText(
                        this,
                        "Successfully remove from favorite",
                        Toast.LENGTH_SHORT,
                    ).show()
                    checkFavorite(data)
                },
                doOnError = {
                    binding.pbFavoriteLoading.isVisible = false
                    binding.btnDetailFavorite.isVisible = true
                    Toast.makeText(
                        this,
                        "Failed remove from favorite",
                        Toast.LENGTH_SHORT,
                    ).show()
                    checkFavorite(data)
                },
            )
        }
    }

    private fun backNavigation() = onBackPressedDispatcher.onBackPressed()

    companion object {
        const val EXTRA_CHARACTER = "EXTRA_CHARACTER"

        fun startActivity(
            context: Context,
            item: Character,
        ) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_CHARACTER, item)
            context.startActivity(intent)
        }
    }
}
