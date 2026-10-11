package com.finanzapp.repository

import com.finanzapp.model.Usuario
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de acceso a datos de usuarios. Al ser una interfaz, el servicio y los tests
 * no dependen de Room (patrón Repository).
 */
interface UsuarioRepository {
    suspend fun guardar(usuario: Usuario): Long
    suspend fun actualizar(usuario: Usuario)
    suspend fun eliminar(id: Long)
    suspend fun buscarPorId(id: Long): Usuario?
    suspend fun buscarPorEmail(email: String): Usuario?
    suspend fun buscarPorCedula(cedula: String): Usuario?
    fun observarTodos(): Flow<List<Usuario>>
}
