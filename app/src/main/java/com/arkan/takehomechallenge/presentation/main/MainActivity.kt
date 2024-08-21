package com.arkan.takehomechallenge.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.arkan.takehomechallenge.R
import com.arkan.takehomechallenge.data.model.Character
import com.arkan.takehomechallenge.databinding.ActivityMainBinding
import com.arkan.takehomechallenge.presentation.detail.DetailActivity
import com.arkan.takehomechallenge.presentation.favorite.FavoriteActivity
import com.arkan.takehomechallenge.presentation.main.adapter.CharacterAdapter
import com.arkan.takehomechallenge.presentation.search.SearchActivity
import com.arkan.takehomechallenge.utils.OnItemCLickedListener
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: MainViewModel by viewModel()
    private lateinit var adapter: CharacterAdapter

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
        setupRecyclerView()
        getCharacterData()
    }

    private fun setupRecyclerView() {
        adapter =
            CharacterAdapter(
                object : OnItemCLickedListener<Character> {
                    override fun onItemClicked(item: Character) {
                        navigateToDetail(item)
                    }
                },
            )

        binding.rvCharacter.adapter = adapter
        binding.rvCharacter.layoutManager = LinearLayoutManager(this)

        adapter.addLoadStateListener { loadState ->
            binding.layoutState.pbLoading.isVisible = loadState.source.refresh is LoadState.Loading
            binding.layoutState.tvError.isVisible = loadState.source.refresh is LoadState.Error
            binding.rvCharacter.isVisible = loadState.source.refresh is LoadState.NotLoading

            if (loadState.source.refresh is LoadState.Error) {
                val errorState = loadState.source.refresh as LoadState.Error
                binding.layoutState.tvError.text = errorState.error.localizedMessage
            }
        }

        binding.layoutState.tvError.setOnClickListener {
            adapter.retry()
        }
    }

    private fun getCharacterData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAllCharacter().collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }
    }

    private fun navigateToDetail(item: Character) {
        DetailActivity.startActivity(
            this,
            item,
        )
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
