package ua.chnu.kpp.posts.data.common

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import ua.chnu.kpp.posts.domain.common.DataError
import ua.chnu.kpp.posts.domain.common.Result

/** Executes a Ktor request and wraps the outcome into a domain [Result]. */
internal suspend inline fun <reified T> HttpClient.safeRequest(
    crossinline block: suspend HttpClient.() -> HttpResponse,
): Result<T> {
    val response = try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        return Result.Failure(DataError.Network(e.message ?: "Unknown network error"))
    }
    return response.toResult()
}

@PublishedApi
internal suspend inline fun <reified T> HttpResponse.toResult(): Result<T> {
    if (status.value !in 200..299) {
        return Result.Failure(DataError.Http(status.value, bodyAsText()))
    }
    return try {
        Result.Success(body<T>())
    } catch (e: SerializationException) {
        Result.Failure(DataError.Serialization(e.message ?: "Cannot parse response"))
    }
}
