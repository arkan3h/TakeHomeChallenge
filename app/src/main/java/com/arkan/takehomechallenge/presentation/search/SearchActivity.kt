package com.arkan.takehomechallenge.presentation.search

import android.os.Bundle
import android.widget.SearchView
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
import com.arkan.takehomechallenge.databinding.ActivitySearchBinding
import com.arkan.takehomechallenge.presentation.detail.DetailActivity
import com.arkan.takehomechallenge.presentation.search.adapter.SearchAdapter
import com.arkan.takehomechallenge.utils.OnItemCLickedListener
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var adapter: SearchAdapter

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
        setupSearchView()
        setupRecyclerView()
    }

    private fun setupSearchView() {
        binding.sbCharacterSearch.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        getCharacterData(it)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
//                    newText?.let {
//                        getCharacterData(it)
//                    }
                    return false
                }
            },
        )
    }

    private fun setupRecyclerView() {
        adapter =
            SearchAdapter(
                object : OnItemCLickedListener<Character> {
                    override fun onItemClicked(item: Character) {
                        navigateToDetail(item)
                    }
                },
            )

        binding.rvSearchCharacter.adapter = adapter
        binding.rvSearchCharacter.layoutManager = LinearLayoutManager(this)

        adapter.addLoadStateListener { loadState ->
            binding.layoutState.pbLoading.isVisible = loadState.source.refresh is LoadState.Loading
            binding.layoutState.tvError.isVisible = loadState.source.refresh is LoadState.Error
            binding.rvSearchCharacter.isVisible = loadState.source.refresh is LoadState.NotLoading

            if (loadState.source.refresh is LoadState.Error) {
                val errorState = loadState.source.refresh as LoadState.Error
                binding.layoutState.tvError.text = errorState.error.localizedMessage
            }
        }

        binding.layoutState.tvError.setOnClickListener {
            adapter.retry()
        }
    }

    private fun getCharacterData(name: String?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchCharacter(name).collectLatest { pagingData ->
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
        binding.btnBackSearch.setOnClickListener {
            backNavigation()
        }
    }

    private fun backNavigation() = onBackPressedDispatcher.onBackPressed()
}
