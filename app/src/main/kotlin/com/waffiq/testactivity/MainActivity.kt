package com.waffiq.testactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.waffiq.testactivity.composables.ArticlesScreen
import com.waffiq.testactivity.ui.theme.InstrumentationTestsHiltTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    setContent {
      InstrumentationTestsHiltTheme {
        ArticlesScreen(hiltViewModel())
      }
    }
  }
}
