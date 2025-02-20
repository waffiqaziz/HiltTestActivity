package com.waffiq.testactivity.di

import com.waffiq.testactivity.repository.ArticlesRepository
import com.waffiq.testactivity.repository.ArticlesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

  @Singleton
  @Binds
  internal abstract fun bindArticlesRepository(articlesRepositoryImpl: ArticlesRepositoryImpl): ArticlesRepository
}