package com.capstone.diabesafe.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.capstone.diabesafe.data.NewsRepository

class HomeViewModel(newsRepository: NewsRepository, query: String, category: String) : ViewModel() {
    val articles = newsRepository.getArticles(query, category).cachedIn(viewModelScope).asLiveData()
}

