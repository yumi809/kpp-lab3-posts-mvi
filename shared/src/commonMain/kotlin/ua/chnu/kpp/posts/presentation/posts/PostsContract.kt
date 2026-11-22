package ua.chnu.kpp.posts.presentation.posts

import androidx.compose.runtime.Immutable
import ua.chnu.kpp.posts.domain.posts.model.Post

/** Single immutable state rendered by the screen (the "Model" in MVI). */
@Immutable
internal data class PostsState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val error: String? = null,
    /** dummyjson only simulates POST: created posts are not stored on the server. */
    val localPostIds: Set<Int> = emptySet(),
) {
    /** First post that really exists on the server, so PUT/DELETE do not end with 404. */
    val firstRemotePost: Post? get() = posts.firstOrNull { it.id !in localPostIds }
}

/** Everything the user can ask the screen to do (the "Intent" in MVI). */
internal sealed interface PostsIntent {
    data object LoadPosts : PostsIntent
    data object CreatePost : PostsIntent
    data object EditFirstPost : PostsIntent
    data object DeleteFirstPost : PostsIntent
    data object DismissError : PostsIntent
}

/** One-off side effects that must not survive in the state (snackbars, navigation...). */
internal sealed interface PostsEffect {
    data class ShowMessage(val text: String) : PostsEffect
}

/** Result of processing an intent; the reducer turns it into a new state. */
internal sealed interface PostsMutation {
    data object Loading : PostsMutation
    data class PostsLoaded(val posts: List<Post>) : PostsMutation
    data class PostAdded(val post: Post) : PostsMutation
    data class PostUpdated(val post: Post) : PostsMutation
    data class PostRemoved(val postId: Int) : PostsMutation
    data class Failed(val message: String) : PostsMutation
    data object ErrorDismissed : PostsMutation
}
