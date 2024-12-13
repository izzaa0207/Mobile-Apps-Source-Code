package com.capstone.diabesafe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.diabesafe.api.response.ArticlesItem
import com.capstone.diabesafe.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val article = intent.getSerializableExtra("ARTICLE_DETAIL") as? ArticlesItem
        article?.let {
            // Menampilkan data ke UI
            binding.tvDetailTitle.text = it.title
            binding.tvDetailDescription.text = it.description
            binding.tvDetailDate.text = it.content
            Glide.with(this)
                .load(it.urlToImage)
                .into(binding.imgDetailPhoto)
        }
    }
}
