package com.codewithmisu.zento.api_client

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

interface TokenStore{
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun upsertTokens(accessToken: String, refreshToken: String)
    fun clearTokens() // Call this on Logout
}

/**
 * A simple implementation of TokenStore using DataStore.
 */
class TokenStoreProvider(
    private val dataStore: DataStore<Preferences>
) : TokenStore {

    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")

    // Ktor's loadTokens/refreshTokens are suspend functions,
    // but the bearer plugin requires synchronous access or runBlocking inside loadTokens.
    override fun getAccessToken(): String? = runBlocking {
        dataStore.data.firstOrNull()?.get(accessTokenKey)
    }

    override fun getRefreshToken(): String? = runBlocking {
        dataStore.data.firstOrNull()?.get(refreshTokenKey)
    }

    override fun upsertTokens(accessToken: String, refreshToken: String) {
        runBlocking {
            dataStore.edit { preferences ->
                preferences[accessTokenKey] = accessToken
                preferences[refreshTokenKey] = refreshToken
            }
        }
    }

    override fun clearTokens() {
        runBlocking {
            dataStore.edit { it.clear() }
        }
    }
}