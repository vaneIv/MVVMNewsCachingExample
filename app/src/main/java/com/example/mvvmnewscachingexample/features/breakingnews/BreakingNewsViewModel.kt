package com.example.mvvmnewscachingexample.features.breakingnews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmnewscachingexample.data.NewsArticle
import com.example.mvvmnewscachingexample.data.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreakingNewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val breakingNewsFlow = MutableStateFlow<List<NewsArticle>>(emptyList())
    val breakingNews: Flow<List<NewsArticle>> = breakingNewsFlow

    init {
        viewModelScope.launch {
            val news = repository.getBreakingNews()
            breakingNewsFlow.value = news
        }
    }
}