@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm {
        binaries {
            executable {
                mainClass.set("com.codewithmisu.zento.MainKt")
            }
        }
    }

    sourceSets.jvmMain.dependencies {
        implementation(projects.composeApp)
        implementation(compose.desktop.currentOs)
        implementation(libs.koin.core)
        implementation(libs.koin.compose)
    }
}

compose.desktop {
    application {
        mainClass = "com.codewithmisu.zento.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.codewithmisu.zento"
            packageVersion = "1.0.0"
        }
    }
}