package ua.chnu.kpp.posts.presentation.posts

/** Pure function (state, mutation) -> state. No coroutines, no I/O, easy to unit test. */
internal object PostsReducer {

    fun reduce(state: PostsState, mutation: PostsMutation): PostsState = when (mutation) {
        PostsMutation.Loading -> state.copy(isLoading = true, error = null)

        is PostsMutation.PostsLoaded -> state.copy(isLoading = false, posts = mutation.posts, localPostIds = emptySet())

        is PostsMutation.PostAdded -> state.copy(
            isLoading = false,
            posts = listOf(mutation.post) + state.posts.filterNot { it.id == mutation.post.id },
            localPostIds = state.localPostIds + mutation.post.id,
        )

        is PostsMutation.PostUpdated -> state.copy(
            isLoading = false,
            posts = state.posts.map { if (it.id == mutation.post.id) mutation.post else it },
        )

        is PostsMutation.PostRemoved -> state.copy(
            isLoading = false,
            posts = state.posts.filterNot { it.id == mutation.postId },
        )

        is PostsMutation.Failed -> state.copy(isLoading = false, error = mutation.message)

        PostsMutation.ErrorDismissed -> state.copy(error = null)
    }
}
