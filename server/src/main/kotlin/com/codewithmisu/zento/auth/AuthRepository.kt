package com.codewithmisu.zento.auth

import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.upsert
import java.util.UUID

interface AuthRepository {
    fun createRefreshToken(userId: UUID, refreshToken: String, expiresAt: Long)
    fun readRefreshToken(userUuid: UUID): String?
}

/**
 * Implementation of the [AuthRepository] interface.
 */
class AuthRepositoryImpl : AuthRepository {
    override fun createRefreshToken(
        userId: UUID,
        refreshToken: String, expiresAt: Long
    ): Unit =
        transaction {
            AuthTable.upsert {
                it[AuthTable.userId] = userId
                it[AuthTable.refreshToken] = refreshToken
                it[AuthTable.expiresAt] = expiresAt
            }
        }

    override fun readRefreshToken(userUuid: UUID): String? = transaction {
        AuthTable.selectAll().where { AuthTable.userId eq userUuid }.map { row ->
            row[AuthTable.refreshToken]
        }.singleOrNull()
    }
}