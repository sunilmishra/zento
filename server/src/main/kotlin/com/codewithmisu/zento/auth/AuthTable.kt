package com.codewithmisu.zento.auth

import com.codewithmisu.zento.user.UsersTable
import org.jetbrains.exposed.v1.core.Table
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object AuthTable: Table("token_table") {
    val userId = uuid("user_id")
    val refreshToken = text("refresh_token")
    val expiresAt = long("expires_at")
    val createdAt = long("created_at").default(System.currentTimeMillis())

    override val primaryKey = PrimaryKey(userId, name = "pk_token_user_id")
}
