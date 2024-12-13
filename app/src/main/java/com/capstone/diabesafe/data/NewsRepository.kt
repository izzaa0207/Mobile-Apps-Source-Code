package com.capstone.diabesafe.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.capstone.diabesafe.adapter.NewsPagingSource
import com.capstone.diabesafe.api.response.ArticlesItem
import com.capstone.diabesafe.api.retrofit.NewsApiService
import kotlinx.coroutines.flow.Flow

class NewsRepository(private val newsApiService: NewsApiService) {
    fun getArticles(query: String, category: String): Flow<PagingData<ArticlesItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(newsApiService, query, category) }
        ).flow
    }
}

