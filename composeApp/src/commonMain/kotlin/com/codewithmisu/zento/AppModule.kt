package com.codewithmisu.zento

import com.codewithmisu.zento.api_client.ApiClient
import com.codewithmisu.zento.api_client.TokenStoreProvider
import com.codewithmisu.zento.auth.authModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

/// Platform Module for Room Database
expect fun platformModule(): Module

/**
 * Network Module
 */
fun networkModule() = module {

    single {
        TokenStoreProvider(dataStore = get())
    }

    single {
        ApiClient(
            baseUrl = "http://localhost:8080",
            tokenStore = get<TokenStoreProvider>(),
            onSessionExpired = {
                println("---- Execute Logout ------ ")
            }
        )
    }
}

/**
 * Initialize Koin
 */
fun initKoin(config: KoinAppDeclaration? = null) = startKoin {
    config?.invoke(this)
    modules(
        platformModule(),
        networkModule(),
        authModule()
    )
}