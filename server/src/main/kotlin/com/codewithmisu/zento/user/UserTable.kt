package com.codewithmisu.zento.user

import com.codewithmisu.zento.profile.UserAddress
import com.codewithmisu.zento.profile.UserLatLong
import com.codewithmisu.zento.profile.UserProfile
import com.codewithmisu.zento.profile.UserRole
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow

object UsersTable : UUIDTable("users_table") {
    val role = enumeration("role", UserRole::class)
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)
    val zipcode = varchar("zip_code", 12)
    val active = bool("active").default(false)
    val phoneNumber = varchar("phone_number", 20).nullable()
    val address = text("address").nullable()
    val latLong = varchar("lat_long", 255).nullable()
    val avatarUrl = text("avatar_url").nullable()
    val createdAt = long("created_at").default(System.currentTimeMillis())
    val updatedAt = long("updated_at").default(System.currentTimeMillis())
}

/**
 * Convert a [ResultRow] to a [com.codewithmisu.shared.profile.UserProfile].
 */
fun ResultRow.toUserProfile(): UserProfile {
    return UserProfile(
        role = this[UsersTable.role],
        id = this[UsersTable.id].value.toString(),
        email = this[UsersTable.email],
        password = this[UsersTable.password],
        firstName = this[UsersTable.firstName],
        lastName = this[UsersTable.lastName],
        zipcode = this[UsersTable.zipcode],
        phoneNumber = this[UsersTable.phoneNumber],
        address = this[UsersTable.address]?.let {
            Json.decodeFromString(
                UserAddress.serializer(),
                this[UsersTable.address]!!
            )
        },
        latLong = this[UsersTable.latLong]?.let {
            Json.decodeFromString(
                UserLatLong.serializer(),
                this[UsersTable.latLong]!!
            )
        }
    )
}
