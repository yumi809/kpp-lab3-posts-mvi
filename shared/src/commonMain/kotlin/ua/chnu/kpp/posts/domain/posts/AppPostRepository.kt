package ua.chnu.kpp.posts.domain.posts

import ua.chnu.kpp.posts.data.common.NetworkResult
import ua.chnu.kpp.posts.data.posts.PostApiService
import ua.chnu.kpp.posts.data.posts.model.requests.NewPost
import ua.chnu.kpp.posts.data.posts.model.responses.DeletedPost
import ua.chnu.kpp.posts.data.posts.model.responses.Post
import ua.chnu.kpp.posts.data.posts.model.responses.Posts

internal class AppPostRepository(
    private val postApiService: PostApiService
) : PostRepository {

    override suspend fun getAllPosts(): NetworkResult<Posts> {
        return postApiService.getAllPosts()
    }

    override suspend fun addPost(post: NewPost): NetworkResult<Post> {
        return postApiService.addPost(post)
    }

    override suspend fun updatePost(post: Post): NetworkResult<Post> {
        return postApiService.updatePost(post)
    }

    override suspend fun deletePost(postId: Int): NetworkResult<DeletedPost> {
        return postApiService.deletePost(postId)
    }
}
