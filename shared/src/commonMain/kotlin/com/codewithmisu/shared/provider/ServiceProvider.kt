package com.codewithmisu.shared.provider

import com.codewithmisu.shared.profile.UserProfile
import kotlinx.serialization.Serializable

@Serializable
data class ServiceProviderRequest(
    val userId: String,
    val zipcodes: List<String>,
    val categories: List<ServiceCategory>,
    val estimatedPricePerHour: Double,
    val responseTimeInMinutes: Int,
)

@Serializable
data class ServiceProviderResponse(
    val id: String,
    val profile: UserProfile,
    val zipcodes: List<String>,
    val categories: List<ServiceCategory>,
    val estimatedPricePerHour: Double,
    val responseTimeInMinutes: Int,
)
