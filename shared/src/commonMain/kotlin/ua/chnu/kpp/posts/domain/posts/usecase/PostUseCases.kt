package ua.chnu.kpp.posts.domain.posts.usecase

import ua.chnu.kpp.posts.domain.common.Result
import ua.chnu.kpp.posts.domain.posts.PostRepository
import ua.chnu.kpp.posts.domain.posts.model.NewPost
import ua.chnu.kpp.posts.domain.posts.model.Post

/** Loads the feed. The page size is a business rule, so it belongs here, not in the UI. */
internal class GetPostsUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(): Result<List<Post>> = repository.getPosts(limit = FEED_SIZE)

    private companion object {
        const val FEED_SIZE = 20
    }
}

/** Publishes a post after normalising its text and tags. */
internal class CreatePostUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(title: String, body: String, tags: List<String>, authorId: Int): Result<Post> {
        val post = NewPost(
            title = title.trim(),
            body = body.trim(),
            tags = tags.map { it.trim().lowercase() }.filter { it.isNotEmpty() }.distinct(),
            authorId = authorId,
        )
        return repository.createPost(post)
    }
}

/** Replaces the body of an existing post. */
internal class EditPostBodyUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(post: Post, newBody: String): Result<Post> =
        repository.updatePost(post.copy(body = newBody.trim()))
}

internal class DeletePostUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(postId: Int): Result<Int> = repository.deletePost(postId)
}
