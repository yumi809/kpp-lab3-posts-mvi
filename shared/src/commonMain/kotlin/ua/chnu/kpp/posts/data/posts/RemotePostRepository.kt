package ua.chnu.kpp.posts.data.posts

import ua.chnu.kpp.posts.data.posts.remote.PostApiService
import ua.chnu.kpp.posts.domain.common.Result
import ua.chnu.kpp.posts.domain.common.map
import ua.chnu.kpp.posts.domain.posts.PostRepository
import ua.chnu.kpp.posts.domain.posts.model.NewPost
import ua.chnu.kpp.posts.domain.posts.model.Post

/**
 * Data layer implementation of [PostRepository].
 * Every network result is converted from DTOs to domain models with [map].
 */
internal class RemotePostRepository(
    private val api: PostApiService,
) : PostRepository {

    override suspend fun getPosts(limit: Int): Result<List<Post>> =
        api.getPosts(limit).map { page -> page.posts.map { it.toDomain() } }

    override suspend fun createPost(post: NewPost): Result<Post> =
        api.addPost(post.toDto()).map { it.toDomain() }

    override suspend fun updatePost(post: Post): Result<Post> =
        api.updatePost(post.id, post.toDto()).map { it.toDomain() }

    override suspend fun deletePost(id: Int): Result<Int> =
        api.deletePost(id).map { it.id }
}
