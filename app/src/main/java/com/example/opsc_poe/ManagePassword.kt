package com.example.opsc_poe

import android.content.Context
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

@OptIn(ExperimentalStdlibApi::class)
class ManagePassword (val upperContext: Context){



    fun generateRandomSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt.toHexString()
    }



    fun ByteArray.toHexString() : String {
        return this.joinToString("") {
            java.lang.String.format(upperContext.getString(R.string.byteArrayFormatter), it)
        }
    }



    fun generateHash(password: String, salt: String): String {

        val combinedSalt = "$salt$SECRET".toByteArray()
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(Companion.ALGORITHM)
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), combinedSalt, ITERATIONS, KEY_LENGTH)
        val key: SecretKey = factory.generateSecret(spec)
        val hash: ByteArray = key.encoded
        return hash.toHexString()

    }



    companion object {
        private const val ALGORITHM = "PBKDF2WithHmacSHA512"
        private const val ITERATIONS = 120_000
        private const val KEY_LENGTH = 256
        private const val SECRET = BuildConfig.TurtleTimeHashingSecret
    }


}