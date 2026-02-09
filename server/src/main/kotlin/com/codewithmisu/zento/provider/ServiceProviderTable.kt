package com.codewithmisu.zento.provider

import com.codewithmisu.zento.user.UsersTable
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.TextColumnType
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
object ServiceProviderTable : Table("service_providers_table") {
    val userId = uuid("user_id")
    val zipcodes = array<String>("service_zipcodes", TextColumnType())
    val categories = array("service_categories", TextColumnType())
    val responseTimeInMinutes = integer("response_time_in_minutes")
    val estimatedPricePerHour = double("estimated_price_per_hour")
    val createdAt = long("created_at").default(System.currentTimeMillis())
    val updatedAt = long("updated_at").default(System.currentTimeMillis())

    override val primaryKey = PrimaryKey(userId, name = "pk_provider_id")
}