package com.codewithmisu.shared.auth

import com.codewithmisu.shared.profile.UserRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
)

@Serializable
data class SignupRequest(
    val role: UserRole,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val zipcode: String
)

@Serializable
data class SignupResponse(
    val userId: String,
    val message: String = "User created successfully, check yor email to verify your account."
)

@Serializable
data class ForgotPasswordRequest(
    val email: String
)

@Serializable
data class ForgotPasswordResponse(
    val message: String = "Password reset link sent to your email."
)

@Serializable
data class ResetPasswordRequest(
    val email: String,
    val newPassword: String
)

@Serializable
data class ResetPasswordResponse(
    val message: String = "Password reset successfully."
)

@Serializable
data class RefreshTokenRequest(
    val userId: String,
    val refreshToken: String
)

@Serializable
data class TokenResponse(
    @SerialName("access_token")
    val accessToken: String,

    @SerialName("refresh_token")
    val refreshToken: String,

    @SerialName("expires_in")
    val expiresIn: Long? = null,

    @SerialName("token_type")
    val tokenType: String? = "Bearer"
)

