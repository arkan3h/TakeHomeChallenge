package com.arkan.takehomechallenge.presentation.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arkan.takehomechallenge.data.model.Character
import com.arkan.takehomechallenge.databinding.ItemCharacterBinding
import com.arkan.takehomechallenge.utils.OnItemCLickedListener

class SearchAdapter(
    private val listener: OnItemCLickedListener<Character>,
) : PagingDataAdapter<Character, SearchAdapter.SearchViewHolder>(
        DIFF_CALLBACK,
    ) {
    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<Character>() {
                override fun areItemsTheSame(
                    oldItem: Character,
                    newItem: Character,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Character,
                    newItem: Character,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchViewHolder {
        return SearchViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            listener,
        )
    }

    override fun onBindViewHolder(
        holder: SearchViewHolder,
        position: Int,
    ) {
        val character = getItem(position)
        if (character != null) {
            holder.bind(character)
        }
    }

    class SearchViewHolder(
        private val binding: ItemCharacterBinding,
        private val listener: OnItemCLickedListener<Character>,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Character) {
            binding.ivCharacterImage.load(item.image)
            binding.tvCharacterName.text = item.name
            binding.tvCharacterDesc.text =
                buildString {
                    append(item.species)
                    append(" - ")
                    append(item.gender)
                }
            itemView.setOnClickListener {
                listener.onItemClicked(item)
            }
        }
    }
}
