package ua.chnu.kpp.posts

import ua.chnu.kpp.posts.domain.common.DataError
import ua.chnu.kpp.posts.domain.common.Result
import ua.chnu.kpp.posts.domain.common.map
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class ResultMapTest {

    @Test
    fun mapTransformsSuccessValue() {
        val result: Result<Int> = Result.Success(21)
        assertEquals(Result.Success(42), result.map { it * 2 })
    }

    @Test
    fun mapKeepsFailureAndDoesNotCallTransform() {
        val failure = Result.Failure(DataError.Http(404, "Not found"))
        var called = false
        val mapped = (failure as Result<Int>).map { called = true; it.toString() }
        assertSame(failure, mapped)
        assertEquals(false, called)
    }
}
