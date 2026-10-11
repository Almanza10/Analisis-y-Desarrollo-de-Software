package com.finanzapp.repository

import com.finanzapp.data.UsuarioDao
import com.finanzapp.model.Usuario
import kotlinx.coroutines.flow.Flow

/** Implementación del repositorio respaldada por Room. */
class UsuarioRepositoryImpl(private val dao: UsuarioDao) : UsuarioRepository {

    override suspend fun guardar(usuario: Usuario): Long = dao.insertar(usuario)

    override suspend fun actualizar(usuario: Usuario) = dao.actualizar(usuario)

    override suspend fun eliminar(id: Long) = dao.eliminarPorId(id)

    override suspend fun buscarPorId(id: Long): Usuario? = dao.buscarPorId(id)

    override suspend fun buscarPorEmail(email: String): Usuario? = dao.buscarPorEmail(email)

    override suspend fun buscarPorCedula(cedula: String): Usuario? = dao.buscarPorCedula(cedula)

    override fun observarTodos(): Flow<List<Usuario>> = dao.observarTodos()
}
