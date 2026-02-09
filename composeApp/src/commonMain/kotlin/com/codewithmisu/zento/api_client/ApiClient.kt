package com.codewithmisu.zento.api_client

import com.codewithmisu.shared.auth.TokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiClient(
    private val baseUrl: String,
    private val tokenStore: TokenStore,
    private val onSessionExpired: () -> Unit // Callback to trigger Login Screen
) {
    private val publicEndpoints = listOf("/auth/login", "/auth/signup", "/auth/refresh")

    val client = HttpClient {
        // 1. Serialization setup
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true // Handles nulls in JSON safely
                isLenient = true
            })
        }

        // 2. Logging for Debugging
        install(Logging) {
            level = LogLevel.INFO // Change to BODY for full debugging
            logger = Logger.DEFAULT
        }

        // 3. Timeout configurations
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 30000
        }

        // 4. Robust Auth Management
        install(Auth) {
            bearer {
                sendWithoutRequest { request ->
                    publicEndpoints.any { request.url.encodedPath.contains(it) }
                }

                loadTokens {
                    val access = tokenStore.getAccessToken()
                    val refresh = tokenStore.getRefreshToken()
                    if (access != null && refresh != null) {
                        BearerTokens(access, refresh)
                    } else {
                        null
                    }
                }

                refreshTokens {
                    try {
                        // Use a basic request to avoid triggering Auth plugin recursively
                        val response = client.post("$baseUrl/auth/refresh") {
                            markAsRefreshTokenRequest()
                            setBody(mapOf("refreshToken" to oldTokens?.refreshToken))
                        }

                        if (response.status == HttpStatusCode.OK) {
                            val newTokens: TokenResponse = response.body()
                            tokenStore.upsertTokens(
                                newTokens.accessToken, newTokens.refreshToken
                            )
                            BearerTokens(newTokens.accessToken, newTokens.refreshToken)
                        } else {
                            handleLogout()
                            null
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        handleLogout()
                        null
                    }
                }
            }
        }
    }

    private fun handleLogout() {
        tokenStore.clearTokens()
        onSessionExpired()
    }

    // --- Generic REST Methods ---
    suspend inline fun <reified T> get(path: String, params: Map<String, String> = emptyMap()): T {
        return client.get(buildUrl(path)) {
            params.forEach { (k, v) -> parameter(k, v) }
        }.body()
    }

    suspend inline fun <reified T, reified R> post(path: String, body: R): T {
        return client.post(buildUrl(path)) {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    suspend inline fun <reified T, reified R> put(path: String, body: R): T {
        return client.put(buildUrl(path)) {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    suspend inline fun <reified  T, reified R> delete(path: String, body: R): T {
        return client.delete(buildUrl(path)) {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    fun buildUrl(path: String) = if (path.startsWith("http")) path else "$baseUrl$path"

    fun logout() {
        tokenStore.clearTokens()
        onSessionExpired()
    }

    fun close() = client.close()
}