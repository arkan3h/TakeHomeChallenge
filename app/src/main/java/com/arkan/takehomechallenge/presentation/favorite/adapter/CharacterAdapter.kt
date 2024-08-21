package com.arkan.takehomechallenge.presentation.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arkan.takehomechallenge.data.model.Character
import com.arkan.takehomechallenge.databinding.ItemCharacterBinding
import com.arkan.takehomechallenge.utils.OnItemCLickedListener

class FavoriteAdapter(
    private val listener: OnItemCLickedListener<Character>,
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val asyncDataDiffer =
        AsyncListDiffer(
            this,
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
            },
        )

    fun submitData(items: List<Character>) {
        asyncDataDiffer.submitList(items)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FavoriteViewHolder {
        return FavoriteViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            listener,
        )
    }

    override fun onBindViewHolder(
        holder: FavoriteViewHolder,
        position: Int,
    ) {
        holder.bind(asyncDataDiffer.currentList[position])
    }

    override fun getItemCount(): Int = asyncDataDiffer.currentList.size

    class FavoriteViewHolder(
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
