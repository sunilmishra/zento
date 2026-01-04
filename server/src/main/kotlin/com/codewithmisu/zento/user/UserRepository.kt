package com.codewithmisu.zento.user

import com.codewithmisu.zento.profile.UserAddress
import com.codewithmisu.zento.profile.UserProfile
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.updateReturning
import java.util.UUID

class UserRepository {

    /**
     * Create a new user in the database.
     */
    fun createUser(profile: UserProfile): String = transaction {
        UsersTable.insertAndGetId {
            it[role] = profile.role
            it[email] = profile.email
            it[password] = profile.password
            it[firstName] = profile.firstName
            it[lastName] = profile.lastName
            it[zipcode] = profile.zipcode
            it[phoneNumber] = profile.phoneNumber
            it[address] = profile.address?.let { Json.encodeToString(profile.address) }
            it[latLong] = profile.latLong?.let { Json.encodeToString(profile.latLong) }
        }.value.toString()
    }

    /**
     * Update a user in the database.
     */
    fun updateUser(profile: UserProfile): UserProfile = transaction {
        val updatedRows = UsersTable.updateReturning {
            it[firstName] = profile.firstName
            it[lastName] = profile.lastName
            it[zipcode] = profile.zipcode
            it[phoneNumber] = profile.phoneNumber
            it[address] = profile.address?.let { Json.encodeToString(profile.address) }
            it[latLong] = profile.latLong?.let { it1 -> "${it1.latitude},${it1.longitude}" }
        }
        updatedRows.firstOrNull()?.toUserProfile() ?: throw Exception("User not found")
    }

    /**
     * Read a user from the database by their email.
     */
    fun readUser(email: String): UserProfile? = transaction {
        UsersTable.selectAll().where { UsersTable.email eq email }
            .map { row ->
                row.toUserProfile()
            }
            .singleOrNull()
    }

    /**
     * Find a user by their ID in the database.
     */
    fun findUserBy(userId: String): UserProfile? = transaction {
        val userUuid = UUID.fromString(userId)
        UsersTable.selectAll().where { UsersTable.id eq  userUuid}
            .map { row ->
                row.toUserProfile()
            }
            .singleOrNull()
    }

    /**
     * Mark a user as active in the database.
     */
    fun markActive(email: String) = transaction {
        UsersTable.update({ UsersTable.email eq email }) {
            it[active] = true
        }
    }

    /**
     * Update a user's zipcode in the database.
     */
    fun updateZipcode(email: String, zipcode: String) = transaction {
        UsersTable.update({ UsersTable.email eq email }) {
            it[UsersTable.zipcode] = zipcode
        }
    }

    /**
     * Update a user's name(firstName & lastName) in the database.
     */
    fun updateName(email: String, firstName: String, lastName: String) = transaction {
        UsersTable.update({ UsersTable.email eq email }) {
            it[UsersTable.firstName] = firstName
            it[UsersTable.lastName] = lastName
        }
    }

    /**
     * Update a user's phone number in the database.
     */
    fun updatePhoneNumber(email: String, phoneNumber: String) = transaction {
        UsersTable.update({ UsersTable.email eq email }) {
            it[UsersTable.phoneNumber] = phoneNumber
        }
    }

    /**
     * Update a user's address in the database.
     */
    fun updateUserAddress(email: String, address: UserAddress) = transaction {
        UsersTable.update({ UsersTable.email eq email }) {
            it[UsersTable.address] = Json.encodeToString(address)
        }
    }

    /**
     * Update a user's location in the database.
     */
    fun updateLatLong(email: String, lat: Double, long: Double) = transaction {
        UsersTable.update({ UsersTable.email eq email }) {
            it[UsersTable.latLong] = "$lat,$long"
        }
    }

    /**
     * Update a user's avatar URL in the database.
     */
    fun updateAvatarUrl(email: String, avatarUrl: String) = transaction {
        UsersTable.update({ UsersTable.email eq email }) {
            it[UsersTable.avatarUrl] = avatarUrl
        }
    }
}