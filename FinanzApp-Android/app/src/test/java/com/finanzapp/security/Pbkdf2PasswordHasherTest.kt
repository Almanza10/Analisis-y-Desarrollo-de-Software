package com.finanzapp.security

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class Pbkdf2PasswordHasherTest {

    // Pocas iteraciones: el test debe ser rápido; el algoritmo es el mismo.
    private val hasher = Pbkdf2PasswordHasher(iteraciones = 1_000)

    @Test
    fun verify_aceptaLaContrasenaCorrecta() {
        val almacenado = hasher.hash("admin123")
        assertTrue(hasher.verify("admin123", almacenado))
    }

    @Test
    fun verify_rechazaUnaContrasenaDistinta() {
        val almacenado = hasher.hash("admin123")
        assertFalse(hasher.verify("admin124", almacenado))
    }

    @Test
    fun hash_noGuardaTextoPlano() {
        assertFalse(hasher.hash("admin123").contains("admin123"))
    }

    @Test
    fun hash_usaSalDistintaCadaVez() {
        assertNotEquals(hasher.hash("misma"), hasher.hash("misma"))
    }

    @Test
    fun verify_rechazaFormatosInvalidos() {
        assertFalse(hasher.verify("x", "esto-no-es-un-hash"))
        assertFalse(hasher.verify("x", "abc:def:ghi"))
        assertFalse(hasher.verify("x", "1000:!!!:???"))
    }
}
