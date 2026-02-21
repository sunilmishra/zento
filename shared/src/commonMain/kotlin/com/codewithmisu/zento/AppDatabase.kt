package com.codewithmisu.zento

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.codewithmisu.zento.profile.UserProfileDao
import com.codewithmisu.zento.profile.UserProfileEntity

@Database(entities = [UserProfileEntity::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getUserProfileDao(): UserProfileDao
}

expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
