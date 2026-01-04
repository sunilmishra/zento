package com.codewithmisu.zento.auth

import com.codewithmisu.zento.user.UsersTable
import org.jetbrains.exposed.sql.Table

object AuthTable: Table("token_table") {
    val userId = uuid("user_id").references(UsersTable.id)
    val refreshToken = text("refresh_token")
    val expiresAt = long("expires_at")
    val createdAt = long("created_at").default(System.currentTimeMillis())

    override val primaryKey = PrimaryKey(userId, name = "pk_token_user_id")
}
