package ua.chnu.kpp.posts.data.posts.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ua.chnu.kpp.posts.data.common.safeRequest
import ua.chnu.kpp.posts.data.posts.remote.dto.DeletedPostDto
import ua.chnu.kpp.posts.data.posts.remote.dto.NewPostDto
import ua.chnu.kpp.posts.data.posts.remote.dto.PostDto
import ua.chnu.kpp.posts.data.posts.remote.dto.PostsPageDto
import ua.chnu.kpp.posts.domain.common.Result

internal interface PostApiService {
    suspend fun getPosts(limit: Int): Result<PostsPageDto>
    suspend fun addPost(post: NewPostDto): Result<PostDto>
    suspend fun updatePost(id: Int, post: PostDto): Result<PostDto>
    suspend fun deletePost(id: Int): Result<DeletedPostDto>
}

internal class KtorPostApiService(
    private val client: HttpClient,
) : PostApiService {

    override suspend fun getPosts(limit: Int): Result<PostsPageDto> = client.safeRequest {
        get(POSTS_URL) {
            accept(ContentType.Application.Json)
            parameter("limit", limit)
        }
    }

    override suspend fun addPost(post: NewPostDto): Result<PostDto> = client.safeRequest {
        post("$POSTS_URL/add") {
            contentType(ContentType.Application.Json)
            setBody(post)
        }
    }

    override suspend fun updatePost(id: Int, post: PostDto): Result<PostDto> = client.safeRequest {
        put("$POSTS_URL/$id") {
            contentType(ContentType.Application.Json)
            setBody(post)
        }
    }

    override suspend fun deletePost(id: Int): Result<DeletedPostDto> = client.safeRequest {
        delete("$POSTS_URL/$id") {
            accept(ContentType.Application.Json)
        }
    }

    private companion object {
        const val POSTS_URL = "https://dummyjson.com/posts"
    }
}
