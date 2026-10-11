package com.finanzapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Entidad `usuarios` (antes @Entity de JPA/MySQL, ahora Room/SQLite).
 *
 * El campo [password] guarda el HASH de la contraseña (PBKDF2), nunca el texto plano.
 */
@Entity(
    tableName = "usuarios",
    indices = [
        Index(value = ["email"], unique = true),
        Index(value = ["cedula"], unique = true)
    ]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombres: String,
    val apellidos: String,
    val cedula: String,
    @ColumnInfo(name = "fecha_nacimiento")
    val fechaNacimiento: LocalDate,
    val email: String,
    val password: String,
    val rol: Rol = Rol.USER,
    val saldo: BigDecimal = BigDecimal.ZERO
) {
    val nombreCompleto: String
        get() = "$nombres $apellidos"
}
