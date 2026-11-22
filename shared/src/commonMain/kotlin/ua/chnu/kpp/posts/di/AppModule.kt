package ua.chnu.kpp.posts.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ua.chnu.kpp.posts.data.posts.RemotePostRepository
import ua.chnu.kpp.posts.data.posts.remote.KtorPostApiService
import ua.chnu.kpp.posts.data.posts.remote.PostApiService
import ua.chnu.kpp.posts.domain.posts.PostRepository
import ua.chnu.kpp.posts.domain.posts.usecase.CreatePostUseCase
import ua.chnu.kpp.posts.domain.posts.usecase.DeletePostUseCase
import ua.chnu.kpp.posts.domain.posts.usecase.EditPostBodyUseCase
import ua.chnu.kpp.posts.domain.posts.usecase.GetPostsUseCase
import ua.chnu.kpp.posts.presentation.posts.PostsViewModel

/** Data layer: HTTP client, API service and repository implementation. */
val dataModule = module {
    single {
        HttpClient {
            install(Logging) {
                level = LogLevel.INFO
                logger = object : Logger {
                    override fun log(message: String) = println("Ktor: $message")
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }
    singleOf(::KtorPostApiService) { bind<PostApiService>() }
    singleOf(::RemotePostRepository) { bind<PostRepository>() }
}

/** Domain layer: use cases are cheap and stateless, so a new instance per injection is fine. */
val domainModule = module {
    factoryOf(::GetPostsUseCase)
    factoryOf(::CreatePostUseCase)
    factoryOf(::EditPostBodyUseCase)
    factoryOf(::DeletePostUseCase)
}

val presentationModule = module {
    viewModelOf(::PostsViewModel)
}

val appModule = module {
    includes(dataModule, domainModule, presentationModule)
}
