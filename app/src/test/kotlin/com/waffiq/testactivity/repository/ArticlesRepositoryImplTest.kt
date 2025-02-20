package com.waffiq.testactivity.repository

import app.cash.turbine.test
import com.waffiq.testactivity.models.Article
import com.waffiq.testactivity.testutils.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class ArticlesRepositoryImplTest {

  private val repository = ArticlesRepositoryImpl()

  @get:Rule
  val dispatcherRule = MainDispatcherRule()

  @Test
  fun getArticles_emitsExpectedList() = runTest {
    repository.getArticles().test {
      val result = awaitItem() // Collect the first emission

      assertEquals(result.size, 3)
      assertEquals(result[0], Article("category1", "title1", "subtitle1"))
      assertEquals(result[1], Article("category2", "title2", "subtitle2"))
      assertEquals(result[2], Article("category3", "title3", "subtitle3"))

      awaitComplete()
    }
  }
}
