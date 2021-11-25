package com.example.mvvmnewscachingexample.data

import com.example.mvvmnewscachingexample.api.NewsApi
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val newsArticleDb: NewsArticleDatabase
) {
    private val newsArticleDao = newsArticleDb.newsArticleDao()

    suspend fun getBreakingNews(): List<NewsArticle> {
        val response = newsApi.getBreakingNews()
        val serverBreakingNewsArticles = response.articles
        val breakingNewsArticles = serverBreakingNewsArticles.map { serverBreakingNewsArticles ->
            NewsArticle(
                title = serverBreakingNewsArticles.title,
                url = serverBreakingNewsArticles.url,
                thumbnailUrl = serverBreakingNewsArticles.urlToImage,
                isBookmarked = false
            )
        }
        return breakingNewsArticles
    }
}