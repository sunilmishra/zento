package com.codewithmisu.zento.profile

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val name: String,
)

@Dao
interface UserProfileDao {

    @Upsert
    suspend fun saveUserProfile(userProfile: UserProfileEntity)

    @Query("SELECT * FROM user_profile")
    suspend fun getAll(): List<UserProfileEntity>
}