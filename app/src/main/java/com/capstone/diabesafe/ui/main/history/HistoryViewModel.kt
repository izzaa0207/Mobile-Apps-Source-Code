package com.capstone.diabesafe.ui.main.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.diabesafe.utils.loadHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val _historyList = MutableLiveData<List<Map<String, Any>>>()
    val historyList: LiveData<List<Map<String, Any>>>
        get() = _historyList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    init {
        loadUserHistory()
    }

    private fun loadUserHistory() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch(Dispatchers.IO) {
            loadHistory { historyList ->
                if (historyList.isNotEmpty()) {
                    _historyList.postValue(historyList)
                } else {
                    _errorMessage.postValue("Data history tidak ditemukan.")
                }
                _isLoading.postValue(false)
            }
        }
    }
}