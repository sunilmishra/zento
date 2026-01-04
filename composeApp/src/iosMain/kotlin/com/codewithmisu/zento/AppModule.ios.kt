package com.codewithmisu.zento

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.dsl.module

actual fun platformModule() = module {
    single<AppDatabase> { getDatabaseBuilder() }

    single<DataStore<Preferences>> { createDataStore(get()) }
}