package com.codewithmisu.zento.profile

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    ServiceProvider,
    Customer
}

@Serializable
data class UserProfile(
    val id: String? = null,
    val role: UserRole,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    var zipcode: String,
    val phoneNumber: String? = null,
    val address: UserAddress? = null,
    val latLong: UserLatLong? = null
)

@Serializable
data class UserAddress(
    val street: String,
    val city: String,
    val state: String,
    val zipcode: String,
    val country: String
)

@Serializable
data class UserLatLong(
    val latitude: Double,
    val longitude: Double
)