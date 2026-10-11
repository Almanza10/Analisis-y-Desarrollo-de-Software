package com.finanzapp.session

import com.finanzapp.model.Rol
import com.finanzapp.model.Usuario

/** Datos de la sesión activa (equivale a los atributos de HttpSession del proyecto Java). */
data class SesionActiva(
    val usuarioId: Long,
    val nombre: String,
    val rol: Rol
)

/** Contrato de la sesión. Interfaz para poder simularla en los tests. */
interface SessionStore {
    fun iniciar(usuario: Usuario)
    fun obtener(): SesionActiva?
    fun cerrar()
}
