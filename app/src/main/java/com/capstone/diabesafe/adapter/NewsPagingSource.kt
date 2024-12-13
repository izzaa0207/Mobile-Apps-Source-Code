package com.capstone.diabesafe.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.diabesafe.api.response.ArticlesItem
import com.capstone.diabesafe.api.retrofit.NewsApiService

class NewsPagingSource(private val newsApiService: NewsApiService, private val query: String, private val category: String) : PagingSource<Int, ArticlesItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticlesItem> {
        return try {
            val page = params.key ?: 1
            val response = newsApiService.getTopHeadlines(query, category, page, params.loadSize)

            LoadResult.Page(
                data = response.articles, // menggunakan response.articles yang merupakan List<ArticlesItem>
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.articles.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticlesItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}

