package com.codewithmisu.zento.auth

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object LandingRoute : NavKey

@Serializable
data object LoginRoute : NavKey

@Serializable
data object SignupRoute : NavKey

fun EntryProviderScope<NavKey>.authNavigator(
    backStack: NavBackStack<NavKey>
) {
    entry<LandingRoute> {
        LandingScreen(
            onCreateAccount = {
                backStack.add(SignupRoute)
            },
            onLoginClick = {
                backStack.add(LoginRoute)
            }
        )
    }

    entry<LoginRoute> {
        LoginScreen(
            onBackPressed = {
                backStack.removeLastOrNull()
            },
            viewModel = koinViewModel<LoginViewModel>()
        )
    }

    entry<SignupRoute> {
        SignupScreen(
            onBackPressed = {
                backStack.removeLastOrNull()
            },
            viewModel = koinViewModel<SignupViewModel>()
        )
    }
}