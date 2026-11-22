# PostsMvi — Lab 3: MVVM → MVI, use cases, `Result.map`

Kotlin Multiplatform (Android, iOS, Desktop) REST client for `https://dummyjson.com/posts`
(GET / POST / PUT / DELETE), based on the MVVM client from labs 10–15 of the previous semester
(commit `[0]`).

## What was done

| Task | Where |
|---|---|
| 1. Presentation layer refactored **MVVM → MVI** | `presentation/posts/`: `PostsState` (model), `PostsIntent` (user intents), `PostsEffect` (one-off effects → snackbar), `PostsMutation` + pure `PostsReducer`, `PostsViewModel.onIntent()` as the single entry point, stateless `PostsScreen` |
| 2. **Use cases** in the domain layer | `domain/posts/usecase/PostUseCases.kt`: `GetPostsUseCase`, `CreatePostUseCase` (normalises text/tags), `EditPostBodyUseCase`, `DeletePostUseCase`; domain models `Post`, `NewPost`; `PostRepository` contract |
| 3. **`map` extension for `Result`**, used in the data layer | `domain/common/Result.kt` (`map`, `onSuccess`, `onFailure`, typed `DataError`); `data/posts/RemotePostRepository.kt` maps DTOs → domain models with `.map { it.toDomain() }` |

Data flow: `UI —Intent→ ViewModel → UseCase → Repository (data) → Ktor API`
and back: `DTO —Result.map→ domain model → Mutation → Reducer → new State → UI`.

Unit tests (`shared/src/commonTest`): `ResultMapTest`, `PostsReducerTest`.

## Run

- Android: `./gradlew :androidApp:assembleDebug` (or Run in Android Studio)
- Desktop: `./gradlew :desktopApp:run`
- Tests: `./gradlew :shared:jvmTest`
- iOS: open `iosApp` in Xcode (macOS only)
