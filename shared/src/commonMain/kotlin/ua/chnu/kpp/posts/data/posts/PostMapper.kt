package ua.chnu.kpp.posts.data.posts

import ua.chnu.kpp.posts.data.posts.remote.dto.NewPostDto
import ua.chnu.kpp.posts.data.posts.remote.dto.PostDto
import ua.chnu.kpp.posts.data.posts.remote.dto.ReactionsDto
import ua.chnu.kpp.posts.domain.posts.model.NewPost
import ua.chnu.kpp.posts.domain.posts.model.Post

internal fun PostDto.toDomain(): Post = Post(
    id = id,
    title = title,
    body = body,
    tags = tags,
    likes = reactions.likes,
    dislikes = reactions.dislikes,
    views = views,
    authorId = userId,
)

internal fun Post.toDto(): PostDto = PostDto(
    id = id,
    title = title,
    body = body,
    tags = tags,
    reactions = ReactionsDto(likes = likes, dislikes = dislikes),
    views = views,
    userId = authorId,
)

internal fun NewPost.toDto(): NewPostDto = NewPostDto(
    title = title,
    body = body,
    tags = tags,
    userId = authorId,
)
