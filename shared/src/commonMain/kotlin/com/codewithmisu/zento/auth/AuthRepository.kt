package com.codewithmisu.zento.auth

import com.codewithmisu.shared.auth.LoginRequest
import com.codewithmisu.shared.auth.LoginResponse
import com.codewithmisu.shared.auth.RefreshTokenRequest
import com.codewithmisu.shared.auth.SignupRequest
import com.codewithmisu.shared.auth.SignupResponse
import com.codewithmisu.shared.auth.TokenResponse
import com.codewithmisu.zento.api_client.ApiClient
import com.codewithmisu.zento.api_client.TokenStore

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest)
    suspend fun signup(signupRequest: SignupRequest): SignupResponse
    suspend fun refreshTokens(userId: String, refreshToken: String): TokenResponse
    suspend fun logout()
}

/**
 * Implementation of the [AuthRepository] interface.
 */
class AuthRepositoryImpl(
    val apiClient: ApiClient,
    val tokenStore: TokenStore
) : AuthRepository {
    override suspend fun login(loginRequest: LoginRequest) {
        try {
            val response = apiClient.post<LoginResponse, LoginRequest>(
                "/auth/login",
                loginRequest
            )
            tokenStore.upsertTokens(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun signup(signupRequest: SignupRequest): SignupResponse {
        return apiClient.post<SignupResponse, SignupRequest>(
            "/auth/signup",
            signupRequest
        )
    }

    override suspend fun refreshTokens(userId: String, refreshToken: String): TokenResponse {
        return apiClient.post<TokenResponse, RefreshTokenRequest>(
            "/auth/refresh",
            RefreshTokenRequest(userId = userId, refreshToken = refreshToken)
        )
    }

    override suspend fun logout() {
        apiClient.logout()
    }
}