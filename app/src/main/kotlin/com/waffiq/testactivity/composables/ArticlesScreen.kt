package com.waffiq.testactivity.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.waffiq.testactivity.models.Article
import com.waffiq.testactivity.viewModels.ArticlesViewModel

@Composable
fun ArticlesScreen(viewModel: ArticlesViewModel) {
  val articles by viewModel.articles.collectAsState()
  ArticleList(articles = articles)
}

@Composable
fun ArticleList(articles: List<Article>) {
  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier
      .padding(16.dp)
      .testTag("testTagArticlesList")
  ) {
    items(articles) { item -> ArticleCard(item.title, item.subtitle, item.category) }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleCard(title: String, subtitle: String, category: String) {
  Card(
    modifier = Modifier.fillMaxSize(),
    shape = RoundedCornerShape(10.dp),
  ) {
    Column(Modifier.padding(8.dp)) {
      Text(text = category)
      Text(text = title)
      Text(text = subtitle)
    }
  }
}

@Composable
@Preview
fun PreviewArticleCard() {
  ArticleCard("title", "subtitle", "category")
}
