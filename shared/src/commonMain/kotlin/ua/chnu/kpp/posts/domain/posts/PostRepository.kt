package ua.chnu.kpp.posts.domain.posts

import ua.chnu.kpp.posts.data.common.NetworkResult
import ua.chnu.kpp.posts.data.posts.model.requests.NewPost
import ua.chnu.kpp.posts.data.posts.model.responses.DeletedPost
import ua.chnu.kpp.posts.data.posts.model.responses.Post
import ua.chnu.kpp.posts.data.posts.model.responses.Posts

internal interface PostRepository {
    suspend fun getAllPosts(): NetworkResult<Posts>
    suspend fun addPost(post: NewPost): NetworkResult<Post>
    suspend fun updatePost(post: Post): NetworkResult<Post>
    suspend fun deletePost(postId: Int): NetworkResult<DeletedPost>
}
