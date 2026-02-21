package com.codewithmisu.zento

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun createDataStore(): DataStore<Preferences> = createDataStore(
    producePath = {
        datastorePath()
    }
)

@OptIn(ExperimentalForeignApi::class)
private fun datastorePath(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return  requireNotNull(documentDirectory).path + "/$dataStoreFileName"
}