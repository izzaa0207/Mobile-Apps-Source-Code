package com.capstone.diabesafe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.diabesafe.api.response.ArticlesItem
import com.capstone.diabesafe.databinding.ItemCardBinding

class NewsPagingAdapter(private val onItemClick: (ArticlesItem) -> Unit) : PagingDataAdapter<ArticlesItem, NewsPagingAdapter.ListItemViewHolder>(NewsDiffCallback()) {

    inner class ListItemViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticlesItem) { // Menggunakan ArticlesItem
            Glide.with(binding.imgItemPhoto.context)
                .load(article.urlToImage)
                .into(binding.imgItemPhoto)

            binding.tvItemName.text = article.title
            binding.tvItemDescription.text = article.description

            itemView.setOnClickListener {
                onItemClick(article)
            }
        }
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val article = getItem(position) // Ambil data berdasarkan posisi dari PagingDataAdapter
        article?.let { holder.bind(it) }
    }

    // Fungsi onCreateViewHolder tidak perlu diimplementasikan karena PagingDataAdapter sudah menangani ini
    // Jika Anda ingin menyesuaikan, Anda bisa menggunakan binding layout yang ada
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListItemViewHolder(binding)
    }

    private class NewsDiffCallback : DiffUtil.ItemCallback<ArticlesItem>() {
        override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
            return oldItem.url == newItem.url // Anda bisa sesuaikan dengan ID unik lainnya
        }

        override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
            return oldItem == newItem
        }
    }
}
