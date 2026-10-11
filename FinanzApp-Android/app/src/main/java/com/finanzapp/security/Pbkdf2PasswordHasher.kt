package com.finanzapp.security

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Hash con PBKDF2-HMAC-SHA256 y sal aleatoria por contraseña.
 * Formato almacenado: `iteraciones:salBase64:hashBase64`.
 */
class Pbkdf2PasswordHasher(
    private val iteraciones: Int = ITERACIONES_POR_DEFECTO
) : PasswordHasher {

    private val random = SecureRandom()

    override fun hash(plain: String): String {
        val sal = ByteArray(TAM_SAL).also(random::nextBytes)
        val hash = derivar(plain, sal, iteraciones)
        val b64 = Base64.getEncoder()
        return "$iteraciones:${b64.encodeToString(sal)}:${b64.encodeToString(hash)}"
    }

    override fun verify(plain: String, stored: String): Boolean {
        val partes = stored.split(":")
        if (partes.size != 3) return false
        val iter = partes[0].toIntOrNull() ?: return false
        val b64 = Base64.getDecoder()
        return try {
            val sal = b64.decode(partes[1])
            val esperado = b64.decode(partes[2])
            MessageDigest.isEqual(esperado, derivar(plain, sal, iter))
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    private fun derivar(plain: String, sal: ByteArray, iter: Int): ByteArray {
        val spec = PBEKeySpec(plain.toCharArray(), sal, iter, LONGITUD_HASH_BITS)
        return SecretKeyFactory.getInstance(ALGORITMO).generateSecret(spec).encoded
    }

    private companion object {
        const val ALGORITMO = "PBKDF2WithHmacSHA256"
        const val ITERACIONES_POR_DEFECTO = 100_000
        const val TAM_SAL = 16
        const val LONGITUD_HASH_BITS = 256
    }
}
