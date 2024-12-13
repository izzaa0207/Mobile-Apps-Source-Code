package com.capstone.diabesafe.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.diabesafe.data.NewsRepository

class HomeViewModelFactory(private val newsRepository: NewsRepository, private val query: String, private val category: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(newsRepository, query, category) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}