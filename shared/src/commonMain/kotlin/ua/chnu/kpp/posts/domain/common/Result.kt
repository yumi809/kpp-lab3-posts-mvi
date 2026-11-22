package ua.chnu.kpp.posts.domain.common

/**
 * Outcome of an operation that can fail.
 * Lives in the domain layer so that neither use cases nor UI depend on networking types.
 */
sealed interface Result<out T> {
    data class Success<out T>(val data: T) : Result<T>
    data class Failure(val error: DataError) : Result<Nothing>
}

/** Reasons why the data layer could not deliver a value. */
sealed interface DataError {
    val message: String

    data class Network(override val message: String) : DataError
    data class Http(val code: Int, override val message: String) : DataError
    data class Serialization(override val message: String) : DataError
}

/**
 * Transforms the value of a successful result and passes a failure through untouched.
 * Used in the data layer to convert DTOs into domain models.
 */
inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Failure -> this
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T> Result<T>.onFailure(action: (DataError) -> Unit): Result<T> {
    if (this is Result.Failure) action(error)
    return this
}
