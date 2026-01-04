package com.codewithmisu.zento

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import kotlinx.serialization.json.Json

/**
 * function that configures the serialization of the application.
 */
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}

/**
 * function that configures the security of the application.
 */
fun Application.configureSecurity(secretKey: String) {
    authentication {
        jwt("auth-jwt") {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secretKey))
                    .withAudience("zepto-users")
                    .withIssuer("zepto")
                    .build()
            )

            validate { credential ->
                val myUserId = credential.payload.getClaim("sub").asString()
                if (myUserId != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { _, _ ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    "Token is invalid or expired"
                )
            }
        }
    }
}

