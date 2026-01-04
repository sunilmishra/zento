package com.codewithmisu.zento.utils

import at.favre.lib.crypto.bcrypt.BCrypt

interface PasswordHasher {
    fun hash(raw: String): String
    fun verify(raw: String, hash: String): Boolean
}

class BCryptPasswordHasher : PasswordHasher {
    override fun hash(raw: String): String {
       return BCrypt.withDefaults().hashToString(12, raw.toCharArray())
    }

    override fun verify(raw: String, hash: String): Boolean {
        val result = BCrypt.verifyer().verify(raw.toCharArray(), hash)
        return result.verified
    }
}
