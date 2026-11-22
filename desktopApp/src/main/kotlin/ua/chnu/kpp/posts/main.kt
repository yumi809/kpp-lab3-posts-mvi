package ua.chnu.kpp.posts

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ua.chnu.kpp.posts.di.initKoin
import ua.chnu.kpp.posts.presentation.App

fun main() = application {
    initKoin { printLogger() }
    Window(
        onCloseRequest = ::exitApplication,
        title = "PostsMvi",
    ) {
        App()
    }
}
