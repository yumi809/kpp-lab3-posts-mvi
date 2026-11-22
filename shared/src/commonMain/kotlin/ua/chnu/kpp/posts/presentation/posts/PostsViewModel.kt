package ua.chnu.kpp.posts.presentation.posts

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.chnu.kpp.posts.domain.common.Result
import ua.chnu.kpp.posts.domain.posts.model.Post
import ua.chnu.kpp.posts.domain.posts.usecase.CreatePostUseCase
import ua.chnu.kpp.posts.domain.posts.usecase.DeletePostUseCase
import ua.chnu.kpp.posts.domain.posts.usecase.EditPostBodyUseCase
import ua.chnu.kpp.posts.domain.posts.usecase.GetPostsUseCase

/**
 * MVI store: the UI sends [PostsIntent]s to [onIntent], the view model runs use cases,
 * turns their results into [PostsMutation]s and [PostsReducer] produces the next [PostsState].
 */
@Stable
class PostsViewModel internal constructor(
    private val getPosts: GetPostsUseCase,
    private val createPost: CreatePostUseCase,
    private val editPostBody: EditPostBodyUseCase,
    private val deletePost: DeletePostUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(PostsState())
    internal val state: StateFlow<PostsState> = _state.asStateFlow()

    private val _effects = Channel<PostsEffect>(Channel.BUFFERED)
    internal val effects: Flow<PostsEffect> = _effects.receiveAsFlow()

    init {
        onIntent(PostsIntent.LoadPosts)
    }

    internal fun onIntent(intent: PostsIntent) {
        when (intent) {
            PostsIntent.LoadPosts -> execute {
                when (val result = getPosts()) {
                    is Result.Success -> PostsMutation.PostsLoaded(result.data)
                    is Result.Failure -> PostsMutation.Failed(result.error.message)
                }
            }

            PostsIntent.CreatePost -> execute {
                val result = createPost(
                    title = "Lab 3: MVI in Kotlin Multiplatform",
                    body = "Intent -> use case -> mutation -> reducer -> new state.",
                    tags = listOf("Kotlin", "MVI", "kmp"),
                    authorId = 5,
                )
                when (result) {
                    is Result.Success -> {
                        sendEffect(PostsEffect.ShowMessage("Post #${result.data.id} created"))
                        PostsMutation.PostAdded(result.data)
                    }
                    is Result.Failure -> PostsMutation.Failed(result.error.message)
                }
            }

            PostsIntent.EditFirstPost -> withFirstPost { post ->
                execute {
                    when (val result = editPostBody(post, "Edited from the MVI client.")) {
                        is Result.Success -> {
                            sendEffect(PostsEffect.ShowMessage("Post #${post.id} updated"))
                            PostsMutation.PostUpdated(result.data)
                        }
                        is Result.Failure -> PostsMutation.Failed(result.error.message)
                    }
                }
            }

            PostsIntent.DeleteFirstPost -> withFirstPost { post ->
                execute {
                    when (val result = deletePost(post.id)) {
                        is Result.Success -> {
                            sendEffect(PostsEffect.ShowMessage("Post #${result.data} deleted"))
                            PostsMutation.PostRemoved(result.data)
                        }
                        is Result.Failure -> PostsMutation.Failed(result.error.message)
                    }
                }
            }

            PostsIntent.DismissError -> reduce(PostsMutation.ErrorDismissed)
        }
    }

    private fun execute(block: suspend () -> PostsMutation) {
        if (_state.value.isLoading) return
        reduce(PostsMutation.Loading)
        viewModelScope.launch { reduce(block()) }
    }

    private inline fun withFirstPost(action: (Post) -> Unit) {
        val first = _state.value.posts.firstOrNull()
        if (first == null) {
            sendEffect(PostsEffect.ShowMessage("Load posts first"))
        } else {
            action(first)
        }
    }

    private fun reduce(mutation: PostsMutation) {
        _state.update { PostsReducer.reduce(it, mutation) }
    }

    private fun sendEffect(effect: PostsEffect) {
        _effects.trySend(effect)
    }
}
