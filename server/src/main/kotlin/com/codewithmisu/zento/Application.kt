package com.codewithmisu.zento

import com.codewithmisu.zento.auth.AuthRepositoryImpl
import com.codewithmisu.zento.auth.AuthServiceImpl
import com.codewithmisu.zento.auth.authRoute
import com.codewithmisu.zento.provider.ProviderRepositoryImpl
import com.codewithmisu.zento.provider.ServiceProviderService
import com.codewithmisu.zento.provider.providerRoute
import com.codewithmisu.zento.user.UserRepository
import com.codewithmisu.zento.user.UserService
import com.codewithmisu.zento.user.userRoute
import com.codewithmisu.zento.utils.BCryptPasswordHasher
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

/**
 * function that starts a Netty server.
 */
fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    /// TODO(sm): Always get secret key from environment variable
    val secretKey = System.getenv("JWT_SECRET") ?: "thisissecretkeyforjwttokengeneration!"

    configureSerialization()
    configureSecurity(secretKey)

    DatabaseFactory.init()

    /// Authentication & User
    val authService = AuthServiceImpl(repository = AuthRepositoryImpl(), secretKey = secretKey)
    val userService = UserService(UserRepository(), BCryptPasswordHasher())
    val providerService = ServiceProviderService(ProviderRepositoryImpl())

    routing {
        route("/api/v1") {
            get("/app") {
                call.respond("Server is running...")
            }

            // Auth Route
            authRoute(authService = authService, userService = userService)

            // User Route
            userRoute(userService = userService)

            // Provider Route
            providerRoute(service = providerService)
        }
    }
}

