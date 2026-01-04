package com.codewithmisu.zento

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.codewithmisu.zento.auth.LandingRoute
import com.codewithmisu.zento.auth.LoginRoute
import com.codewithmisu.zento.auth.SignupRoute
import com.codewithmisu.zento.auth.authNavigator
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun AppNavigation() {

    // Creates the required serializing configuration for open polymorphism
     val config = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(LandingRoute::class, LandingRoute.serializer())
                subclass(LoginRoute::class, LoginRoute.serializer())
                subclass(SignupRoute::class, SignupRoute.serializer())
            }
        }
    }

    val backStack = rememberNavBackStack(
        configuration = config,
        LandingRoute
    )

    NavDisplay(
        backStack = backStack,
        onBack = {
            backStack.removeLastOrNull()
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            authNavigator(
                backStack = backStack
            )
        },
    )
}