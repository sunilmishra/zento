package com.codewithmisu.zento.auth

import com.codewithmisu.zento.api_client.ApiClient
import com.codewithmisu.zento.api_client.TokenStoreProvider
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Authentication Module
 */
fun authModule() = module {
    single<AuthRepository> {
        AuthRepositoryImpl(
            apiClient = get<ApiClient>(),
            tokenStore = get<TokenStoreProvider>()
        )
    }

    viewModel {
        LoginViewModel(authRepository = get<AuthRepository>())
    }

    viewModel {
        SignupViewModel(authRepository = get<AuthRepository>())
    }
}
