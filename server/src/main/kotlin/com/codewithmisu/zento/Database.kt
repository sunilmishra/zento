package com.codewithmisu.zento

import com.codewithmisu.zento.auth.AuthTable
import com.codewithmisu.zento.provider.ServiceProviderTable
import com.codewithmisu.zento.user.UsersTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object DatabaseFactory {

    fun init() {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/zento_db"
            driverClassName = "org.postgresql.Driver"
            username = "admin"
            password = "mnbvcxz123"
            maximumPoolSize = 7
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        Database.connect(HikariDataSource(config))

        createTables()
    }
}

/// Create tables if not exists
private fun createTables() {
    transaction {
        SchemaUtils.create(AuthTable, UsersTable, ServiceProviderTable)
    }
}
