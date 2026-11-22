package ua.chnu.kpp.posts.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import ua.chnu.kpp.posts.presentation.posts.PostsRoute

@Composable
fun App() {
    MaterialTheme {
        PostsRoute()
    }
}
