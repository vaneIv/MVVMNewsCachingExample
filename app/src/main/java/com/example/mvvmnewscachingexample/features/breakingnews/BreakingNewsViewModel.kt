package com.example.mvvmnewscachingexample.features.breakingnews

import androidx.lifecycle.ViewModel
import com.example.mvvmnewscachingexample.data.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BreakingNewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {
}