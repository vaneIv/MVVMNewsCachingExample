package com.example.mvvmnewscachingexample.data

import androidx.room.withTransaction
import com.example.mvvmnewscachingexample.api.NewsApi
import com.example.mvvmnewscachingexample.util.Resource
import com.example.mvvmnewscachingexample.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val newsArticleDb: NewsArticleDatabase
) {
    private val newsArticleDao = newsArticleDb.newsArticleDao()

//    suspend fun getBreakingNews(): List<NewsArticle> {
//        val response = newsApi.getBreakingNews()
//        val serverBreakingNewsArticles = response.articles
//        val breakingNewsArticles = serverBreakingNewsArticles.map { serverBreakingNewsArticles ->
//            NewsArticle(
//                title = serverBreakingNewsArticles.title,
//                url = serverBreakingNewsArticles.url,
//                thumbnailUrl = serverBreakingNewsArticles.urlToImage,
//                isBookmarked = false
//            )
//        }
//        return breakingNewsArticles
//    }

    fun getBreakingNews(): Flow<Resource<List<NewsArticle>>> =
        networkBoundResource(
            query = {
                newsArticleDao.getAllBreakingNewsArticles()
            },
            fetch = {
                val response = newsApi.getBreakingNews()
                response.articles
            },
            saveFetchResult = { serverBreakingNewsArticles ->
                val breakingNewsArticles =
                    serverBreakingNewsArticles.map { serverBreakingNewsArticle ->
                        NewsArticle(
                            title = serverBreakingNewsArticle.title,
                            url = serverBreakingNewsArticle.url,
                            thumbnailUrl = serverBreakingNewsArticle.urlToImage,
                            isBookmarked = false
                        )
                    }

                val breakingNews = breakingNewsArticles.map { article ->
                    BreakingNews(article.url)
                }

                newsArticleDb.withTransaction {
                    newsArticleDao.deleteAllBreakingNews()
                    newsArticleDao.insertArticles(breakingNewsArticles)
                    newsArticleDao.insertBreakingNews(breakingNews)
                }
            }
        )
}