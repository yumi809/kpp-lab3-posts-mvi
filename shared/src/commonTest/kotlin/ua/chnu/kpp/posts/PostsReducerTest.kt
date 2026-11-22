package ua.chnu.kpp.posts

import ua.chnu.kpp.posts.domain.posts.model.Post
import ua.chnu.kpp.posts.presentation.posts.PostsMutation
import ua.chnu.kpp.posts.presentation.posts.PostsReducer
import ua.chnu.kpp.posts.presentation.posts.PostsState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PostsReducerTest {

    private fun post(id: Int, body: String = "body") = Post(id, "title $id", body, emptyList(), 0, 0, 0, 1)

    @Test
    fun loadingClearsErrorAndShowsProgress() {
        val state = PostsReducer.reduce(PostsState(error = "boom"), PostsMutation.Loading)
        assertTrue(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun addedPostGoesToTheTop() {
        val state = PostsReducer.reduce(PostsState(posts = listOf(post(1))), PostsMutation.PostAdded(post(2)))
        assertEquals(listOf(2, 1), state.posts.map { it.id })
        assertFalse(state.isLoading)
    }

    @Test
    fun updatedPostReplacesOldOne() {
        val state = PostsReducer.reduce(
            PostsState(posts = listOf(post(1), post(2))),
            PostsMutation.PostUpdated(post(2, body = "new")),
        )
        assertEquals("new", state.posts.single { it.id == 2 }.body)
    }

    @Test
    fun removedPostDisappears() {
        val state = PostsReducer.reduce(PostsState(posts = listOf(post(1), post(2))), PostsMutation.PostRemoved(1))
        assertEquals(listOf(2), state.posts.map { it.id })
    }

    @Test
    fun failureStoresMessage() {
        val state = PostsReducer.reduce(PostsState(isLoading = true), PostsMutation.Failed("offline"))
        assertEquals("offline", state.error)
        assertFalse(state.isLoading)
    }
}
