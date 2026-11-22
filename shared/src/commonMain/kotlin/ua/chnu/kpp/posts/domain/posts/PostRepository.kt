package ua.chnu.kpp.posts.domain.posts

import ua.chnu.kpp.posts.domain.common.Result
import ua.chnu.kpp.posts.domain.posts.model.NewPost
import ua.chnu.kpp.posts.domain.posts.model.Post

/** Domain contract; implemented in the data layer. Works only with domain models. */
internal interface PostRepository {
    suspend fun getPosts(limit: Int): Result<List<Post>>
    suspend fun createPost(post: NewPost): Result<Post>
    suspend fun updatePost(post: Post): Result<Post>

    /** @return id of the removed post. */
    suspend fun deletePost(id: Int): Result<Int>
}
