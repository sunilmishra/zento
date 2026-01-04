package com.codewithmisu.zento.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date
import java.util.UUID

interface AuthService {
    fun createAccessToken(userId: String): String
    fun createRefreshToken(userId: String): String
    fun verifyRefreshToken(userId: String, refreshToken: String): Boolean
}

class AuthServiceImpl(
    private val repository: AuthRepository,
    private val secretKey: String
) : AuthService {

    override fun createAccessToken(userId: String): String {
        val algorithm = Algorithm.HMAC256(secretKey)
        val now = System.currentTimeMillis()
        // 1 hour in millis
        val expiresAt = Date(now + 60 * 60 * 1000)
        return JWT.create()
            .withSubject(userId)
            .withIssuer("zepto")
            .withAudience("zepto-users")
            .withIssuedAt(Date(now))
            .withExpiresAt(expiresAt)
            .sign(algorithm)
    }

    override fun createRefreshToken(userId: String): String {
        val userUuid = UUID.fromString(userId)
        val token = UUID.randomUUID().toString()
        // 30 days in millis
        val expiresAt = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000
        repository.createRefreshToken(
            userUuid,
            token,
            expiresAt
        )
        return token
    }

    override fun verifyRefreshToken(userId: String, refreshToken: String): Boolean {
        val userUuid = UUID.fromString(userId)
        val storedRefreshToken = repository.readRefreshToken(userUuid)
        return storedRefreshToken == refreshToken
    }
}
