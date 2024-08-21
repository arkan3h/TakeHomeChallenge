package com.arkan.takehomechallenge.presentation.favorite

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arkan.takehomechallenge.R
import com.arkan.takehomechallenge.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private val binding: ActivityFavoriteBinding by lazy {
        ActivityFavoriteBinding.inflate(layoutInflater)
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
        binding.btnBackFavorite.setOnClickListener {
            backNavigation()
        }
    }

    private fun backNavigation() = onBackPressedDispatcher.onBackPressed()
}
