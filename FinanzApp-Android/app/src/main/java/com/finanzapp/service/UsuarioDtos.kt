package com.finanzapp.service

import com.finanzapp.model.Rol
import com.finanzapp.model.Usuario
import java.time.LocalDate

/** Datos de entrada para registrar un usuario (la contraseña viene en texto plano). */
data class DatosRegistro(
    val nombres: String,
    val apellidos: String,
    val cedula: String,
    val fechaNacimiento: LocalDate,
    val email: String,
    val password: String
)

/** Datos que el administrador puede editar de un usuario. */
data class DatosEdicion(
    val id: Long,
    val nombres: String,
    val apellidos: String,
    val cedula: String,
    val email: String,
    val rol: Rol,
    val fechaNacimiento: LocalDate?
)

/** Resultado de registrar (reemplaza las IllegalArgumentException("email"/"cedula") de Java). */
sealed interface ResultadoRegistro {
    data class Exito(val usuario: Usuario) : ResultadoRegistro
    data object EmailDuplicado : ResultadoRegistro
    data object CedulaDuplicada : ResultadoRegistro
}

sealed interface ResultadoEdicion {
    data object Exito : ResultadoEdicion
    data object NoEncontrado : ResultadoEdicion
    data object EmailDuplicado : ResultadoEdicion
    data object CedulaDuplicada : ResultadoEdicion
}
