plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    application
}

group = "com.codewithmisu.zento"
version = "1.0.0"
application {
    mainClass.set("com.codewithmisu.zento.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(libs.ktor.server.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)

    implementation(libs.ktor.server.content)
    implementation(libs.ktor.serialization.json)

    implementation(libs.ktor.server.postgresSql)
    implementation(libs.ktor.server.hikari)

    implementation(libs.ktor.server.exposed.core)
    implementation(libs.ktor.server.exposed.dao)
    implementation(libs.ktor.server.exposed.jdbc)

    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)

    implementation(libs.bcrypt.hashing)

    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
    testImplementation(libs.kotlin.mockk)

    implementation(projects.shared)
}