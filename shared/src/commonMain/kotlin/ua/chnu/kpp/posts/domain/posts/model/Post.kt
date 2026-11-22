package ua.chnu.kpp.posts.domain.posts.model

data class Post(
    val id: Int,
    val title: String,
    val body: String,
    val tags: List<String>,
    val likes: Int,
    val dislikes: Int,
    val views: Int,
    val authorId: Int,
)

data class NewPost(
    val title: String,
    val body: String,
    val tags: List<String>,
    val authorId: Int,
)
