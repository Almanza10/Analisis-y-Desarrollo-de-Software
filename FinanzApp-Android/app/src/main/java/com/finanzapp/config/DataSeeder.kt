package com.finanzapp.config

import com.finanzapp.model.Rol
import com.finanzapp.model.Usuario
import com.finanzapp.repository.UsuarioRepository
import com.finanzapp.security.PasswordHasher
import java.time.LocalDate

/**
 * Crea el administrador por defecto la primera vez que arranca la app
 * (equivalente a DataSeeder.java con CommandLineRunner).
 *
 * Credenciales por defecto:  admin@finanzapp.com / admin123
 * IMPORTANTE: cámbialas o elimina este seeder antes de publicar la app.
 */
class DataSeeder(
    private val repository: UsuarioRepository,
    private val hasher: PasswordHasher
) {
    suspend fun run() {
        if (repository.buscarPorEmail(ADMIN_EMAIL) != null) return
        repository.guardar(
            Usuario(
                nombres = "Administrador",
                apellidos = "FinanzApp",
                cedula = "0000000000",
                fechaNacimiento = LocalDate.of(1990, 1, 1),
                email = ADMIN_EMAIL,
                password = hasher.hash(ADMIN_PASSWORD),
                rol = Rol.ADMIN
            )
        )
    }

    companion object {
        const val ADMIN_EMAIL = "admin@finanzapp.com"
        const val ADMIN_PASSWORD = "admin123"
    }
}
