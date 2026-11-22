package ua.chnu.kpp.posts.data.posts.remote.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class ReactionsDto(
    val likes: Int = 0,
    val dislikes: Int = 0,
)

@Serializable
internal data class PostDto(
    val id: Int,
    val title: String,
    val body: String,
    val tags: List<String> = emptyList(),
    val reactions: ReactionsDto = ReactionsDto(),
    val views: Int = 0,
    val userId: Int,
)

@Serializable
internal data class PostsPageDto(
    val posts: List<PostDto>,
    val total: Int,
    val skip: Int,
    val limit: Int,
)

@Serializable
internal data class NewPostDto(
    val title: String,
    val body: String,
    val tags: List<String>,
    val userId: Int,
)

@Serializable
internal data class DeletedPostDto(
    val id: Int,
    val isDeleted: Boolean,
    val deletedOn: String,
)
