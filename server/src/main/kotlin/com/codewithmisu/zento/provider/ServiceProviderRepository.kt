package com.codewithmisu.zento.provider

import org.jetbrains.exposed.sql.Op
import com.codewithmisu.zento.profile.UserProfile
import com.codewithmisu.zento.user.UsersTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.QueryBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.upsert
import java.util.UUID

interface ServiceProviderRepository {
    fun upsertProvider(providerRequest: ServiceProviderRequest)
    fun findProviders(zipcode: String): List<ServiceProviderResponse>
    fun deleteProvider(id: String)
    fun updateCategories(id: String, categories : List<ServiceCategory>)
}

/**
 * Implementation of [ServiceProviderRepository] interface.
 */
class ProviderRepositoryImpl() : ServiceProviderRepository {
    override fun upsertProvider(providerRequest: ServiceProviderRequest): Unit = transaction {
        ServiceProviderTable.upsert {
            it[userId] = UUID.fromString(providerRequest.userId)
            it[zipcodes] = providerRequest.zipcodes
            it[categories] = providerRequest.categories.map { category -> category.name }
            it[estimatedPricePerHour] = providerRequest.estimatedPricePerHour
            it[responseTimeInMinutes] = providerRequest.responseTimeInMinutes
        }
    }

    override fun findProviders(zipcode: String): List<ServiceProviderResponse> = transaction {
        val query = ServiceProviderTable
            .join(
                UsersTable,
                JoinType.INNER,
                onColumn = ServiceProviderTable.userId,
                otherColumn = UsersTable.id
            )
            .selectAll()
            .where {
                object : Op<Boolean>() {
                    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
                        queryBuilder.append("'" + zipcode.trim() + "' = ANY(")
                        queryBuilder.append(ServiceProviderTable.zipcodes)
                        queryBuilder.append(")")
                    }
                }
            }

        val providers = mutableListOf<ServiceProviderResponse>()
        for (row in query) {
            val provider = ServiceProviderResponse(
                id = row[ServiceProviderTable.userId].toString(),
                profile = UserProfile(
                    id = row[UsersTable.id].toString(),
                    role = row[UsersTable.role],
                    email = row[UsersTable.email],
                    password = row[UsersTable.password],
                    firstName = row[UsersTable.firstName],
                    lastName = row[UsersTable.lastName],
                    zipcode = row[UsersTable.zipcode],
                ),
                categories = row[ServiceProviderTable.categories].map {
                    ServiceCategory.valueOf(it)
                },
                zipcodes = row[ServiceProviderTable.zipcodes],
                estimatedPricePerHour = row[ServiceProviderTable.estimatedPricePerHour],
                responseTimeInMinutes = row[ServiceProviderTable.responseTimeInMinutes],
            )
            providers.add(provider)
        }
        return@transaction providers
    }

    override fun deleteProvider(id: String): Unit = transaction {
        ServiceProviderTable.deleteWhere { ServiceProviderTable.userId eq UUID.fromString(id) }
    }

    override fun updateCategories(id: String, categories : List<ServiceCategory>): Unit = transaction {
        ServiceProviderTable.update({ ServiceProviderTable.userId eq UUID.fromString(id) }) {
            it[ServiceProviderTable.categories] = categories.map { category -> category.name }
        }
    }
}