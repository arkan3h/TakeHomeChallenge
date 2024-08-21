package com.arkan.takehomechallenge.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arkan.takehomechallenge.R
import com.arkan.takehomechallenge.databinding.ActivityMainBinding
import com.arkan.takehomechallenge.presentation.favorite.FavoriteActivity
import com.arkan.takehomechallenge.presentation.search.SearchActivity

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
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
    }

    private fun clickListener() {
        binding.btnSearch.setOnClickListener {
            navigateToSearch()
        }
        binding.btnFavorite.setOnClickListener {
            navigateToFavorite()
        }
    }

    private fun navigateToSearch() {
        startActivity(
            Intent(this, SearchActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun navigateToFavorite() {
        startActivity(
            Intent(this, FavoriteActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }
}
