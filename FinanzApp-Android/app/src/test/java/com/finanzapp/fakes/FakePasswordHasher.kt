package com.finanzapp.fakes

import com.finanzapp.security.PasswordHasher

/** Hasher trivial y rápido para los tests (no usar fuera de pruebas). */
class FakePasswordHasher : PasswordHasher {
    override fun hash(plain: String): String = "hash($plain)"
    override fun verify(plain: String, stored: String): Boolean = stored == hash(plain)
}
