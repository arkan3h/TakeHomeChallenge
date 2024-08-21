package com.arkan.takehomechallenge.presentation.search

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.arkan.takehomechallenge.R
import com.arkan.takehomechallenge.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
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
        binding.btnBackSearch.setOnClickListener {
            backNavigation()
        }
    }

    private fun backNavigation() = onBackPressedDispatcher.onBackPressed()
}
