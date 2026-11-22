package ua.chnu.kpp.posts.presentation.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import ua.chnu.kpp.posts.domain.posts.model.Post

@Composable
fun PostsRoute(viewModel: PostsViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is PostsEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.text)
            }
        }
    }

    PostsScreen(state = state, snackbarHostState = snackbarHostState, onIntent = viewModel::onIntent)
}

/** Stateless screen: renders [PostsState] and reports user actions as [PostsIntent]s. */
@Composable
internal fun PostsScreen(
    state: PostsState,
    snackbarHostState: SnackbarHostState,
    onIntent: (PostsIntent) -> Unit,
) {
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ActionButton("GET", Modifier.weight(1f)) { onIntent(PostsIntent.LoadPosts) }
                ActionButton("POST", Modifier.weight(1f)) { onIntent(PostsIntent.CreatePost) }
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ActionButton("PUT", Modifier.weight(1f)) { onIntent(PostsIntent.EditFirstPost) }
                ActionButton("DELETE", Modifier.weight(1f)) { onIntent(PostsIntent.DeleteFirstPost) }
            }
            Spacer(Modifier.height(8.dp))

            if (state.isLoading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
            }

            state.error?.let { message ->
                ErrorBanner(message = message, onDismiss = { onIntent(PostsIntent.DismissError) })
                Spacer(Modifier.height(8.dp))
            }

            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.posts, key = { it.id }) { post -> PostCard(post) }
            }
        }
    }
}

@Composable
private fun ActionButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    Button(modifier = modifier, onClick = onClick) { Text(text) }
}

@Composable
private fun ErrorBanner(message: String, onDismiss: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
        Row(Modifier.fillMaxWidth().padding(start = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = message,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onErrorContainer,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            TextButton(onClick = onDismiss) { Text("OK") }
        }
    }
}

@Composable
private fun PostCard(post: Post) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text("#${post.id}  ${post.title}", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(post.body, style = MaterialTheme.typography.bodyMedium, maxLines = 3, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(6.dp))
            Text(
                text = "${post.tags.joinToString(" ") { "#$it" }}   ▲${post.likes} ▼${post.dislikes}  views ${post.views}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Preview
@Composable
private fun PostsScreenPreview() {
    MaterialTheme {
        PostsScreen(
            state = PostsState(
                posts = listOf(Post(1, "Preview title", "Preview body", listOf("kmp"), 3, 0, 10, 1)),
            ),
            snackbarHostState = SnackbarHostState(),
            onIntent = {},
        )
    }
}
