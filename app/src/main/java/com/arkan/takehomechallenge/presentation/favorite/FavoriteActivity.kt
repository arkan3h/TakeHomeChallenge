package com.arkan.takehomechallenge.presentation.favorite

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.arkan.takehomechallenge.R
import com.arkan.takehomechallenge.data.model.Character
import com.arkan.takehomechallenge.databinding.ActivityFavoriteBinding
import com.arkan.takehomechallenge.databinding.DialogDeleteConfirmationBinding
import com.arkan.takehomechallenge.presentation.detail.DetailActivity
import com.arkan.takehomechallenge.presentation.search.adapter.FavoriteAdapter
import com.arkan.takehomechallenge.utils.OnItemCLickedListener
import com.arkan.takehomechallenge.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteActivity : AppCompatActivity() {
    private val binding: ActivityFavoriteBinding by lazy {
        ActivityFavoriteBinding.inflate(layoutInflater)
    }
    private val viewModel: FavoriteViewModel by viewModel()
    private var adapter: FavoriteAdapter? = null

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
        getFavoriteData()
    }

    private fun getFavoriteData() {
        viewModel.getAllFavorite().observe(this) { it ->
            it.proceedWhen(
                doOnLoading = {
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.rvFavoriteCharacter.isVisible = false
                },
                doOnSuccess = {
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.rvFavoriteCharacter.isVisible = true
                    it.payload?.let { data ->
                        bindCharacterList(data)
                    }
                },
                doOnEmpty = {
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.rvFavoriteCharacter.isVisible = false
                },
                doOnError = {
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.rvFavoriteCharacter.isVisible = false
                    binding.layoutState.tvError.text = it.exception?.message.orEmpty()
                },
            )
        }
    }

    private fun bindCharacterList(data: List<Character>) {
        adapter =
            FavoriteAdapter(
                listener =
                    object : OnItemCLickedListener<Character> {
                        override fun onItemClicked(item: Character) {
                            navigateToDetail(item)
                        }
                    },
            )
        binding.rvFavoriteCharacter.adapter = this.adapter
        adapter?.submitData(data)
    }

    private fun navigateToDetail(item: Character) {
        DetailActivity.startActivity(
            this,
            item,
        )
    }

    private fun clickListener() {
        binding.btnBackFavorite.setOnClickListener {
            backNavigation()
        }
        binding.btnDelete.setOnClickListener {
            deleteFavoriteDialog()
        }
    }

    private fun backNavigation() = onBackPressedDispatcher.onBackPressed()

    private fun deleteFavoriteDialog() {
        val dialogBinding = DialogDeleteConfirmationBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnDelete.setOnClickListener {
            removeFavorite(dialogBinding)
        }
        dialogBinding.btnBackDialog.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun removeFavorite(dialogBinding: DialogDeleteConfirmationBinding) {
        viewModel.removeFavorite().observe(this) {
            it.proceedWhen(
                doOnLoading = {
                    dialogBinding.pbDialogLoading.isVisible = true
                    dialogBinding.btnCancel.isVisible = false
                    dialogBinding.btnDelete.isVisible = false
                    dialogBinding.btnBackDialog.isVisible = false
                    dialogBinding.tvTitleDelete.isVisible = false
                },
                doOnSuccess = {
                    dialogBinding.pbDialogLoading.isVisible = false
                    dialogBinding.btnCancel.isVisible = false
                    dialogBinding.btnDelete.isVisible = false
                    dialogBinding.btnBackDialog.isVisible = true
                    dialogBinding.tvTitleDelete.isVisible = true
                    dialogBinding.tvTitleDelete.text =
                        getString(R.string.text_success_delete_favorites)
                },
                doOnError = {
                    dialogBinding.pbDialogLoading.isVisible = false
                    dialogBinding.btnCancel.isVisible = false
                    dialogBinding.btnDelete.isVisible = false
                    dialogBinding.btnBackDialog.isVisible = true
                    dialogBinding.tvTitleDelete.isVisible = true
                    dialogBinding.tvTitleDelete.text =
                        getString(R.string.text_failed_delete_favorites)
                },
            )
        }
    }
}
