package com.waffiq.testactivity.repository

import com.waffiq.testactivity.models.Article
import kotlinx.coroutines.flow.Flow

interface ArticlesRepository {
  fun getArticles(): Flow<List<Article>>
}
