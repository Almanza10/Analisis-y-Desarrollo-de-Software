package com.finanzapp.security

/** Abstracción para hashear y verificar contraseñas (permite usar un falso en los tests). */
interface PasswordHasher {
    fun hash(plain: String): String
    fun verify(plain: String, stored: String): Boolean
}
