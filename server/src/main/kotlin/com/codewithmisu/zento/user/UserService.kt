package com.codewithmisu.zento.user

import com.codewithmisu.zento.profile.UserAddress
import com.codewithmisu.zento.profile.UserProfile
import com.codewithmisu.zento.utils.PasswordHasher

class UserService(
    private val userRepository: UserRepository,
    private val hasher: PasswordHasher
) {
    fun verifyPassword(raw: String, hash: String) = hasher.verify(raw, hash)

    fun hashPassword(raw: String) = hasher.hash(raw)

    fun saveUser(userProfile: UserProfile): String {
        val updatedProfile = userProfile.copy(
            password = hashPassword(userProfile.password)
        )
        return userRepository.createUser(updatedProfile)
    }

    fun findUserBy(userId: String): UserProfile? = userRepository.findUserBy(userId)

    fun getUser(email: String): UserProfile? = userRepository.readUser(email)

    fun updateUser(userProfile: UserProfile) = userRepository.updateUser(userProfile)

    fun markActive(email: String) = userRepository.markActive(email)

    fun updateZipcode(email: String, zipcode: String) {
        userRepository.updateZipcode(email, zipcode)
    }

    fun updateName(email: String, firstName: String, lastName: String) {
        userRepository.updateName(email, firstName, lastName)
    }

    fun updatePhoneNumber(email: String, phoneNumber: String) {
        userRepository.updatePhoneNumber(email, phoneNumber)
    }

    fun updateUserAddress(email: String, address: UserAddress) {
        userRepository.updateUserAddress(email, address)
    }

    fun updateLatLong(email: String, lat: Double, long: Double) {
        userRepository.updateLatLong(email, lat, long)
    }

    fun updateAvatarUrl(email: String, avatarUrl: String) {
        userRepository.updateAvatarUrl(email, avatarUrl)
    }
}


