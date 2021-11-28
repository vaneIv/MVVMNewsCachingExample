package com.example.mvvmnewscachingexample.features.searchnews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.mvvmnewscachingexample.data.NewsArticle
import com.example.mvvmnewscachingexample.data.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val currentQuery = MutableStateFlow<String?>(null)

    val searchResults = currentQuery.flatMapLatest { query ->
        query?.let {
            repository.getSearchResultPaged(query)
        } ?: emptyFlow()
    }.cachedIn(viewModelScope)

    fun onSearchQuerySubmit(query: String) {
        currentQuery.value = query
    }

    fun onBookmarkClick(article: NewsArticle) {
        val currentlyBookmarked = article.isBookmarked
        val updateArticle = article.copy(isBookmarked = !currentlyBookmarked)
        viewModelScope.launch {
            repository.updateArticle(updateArticle)
        }
    }
}