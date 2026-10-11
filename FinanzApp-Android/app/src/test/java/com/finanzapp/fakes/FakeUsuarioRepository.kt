package com.finanzapp.fakes

import com.finanzapp.model.Usuario
import com.finanzapp.repository.UsuarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/** Repositorio en memoria para probar el servicio y los ViewModels sin Room. */
class FakeUsuarioRepository : UsuarioRepository {

    private val datos = MutableStateFlow<Map<Long, Usuario>>(emptyMap())
    private var siguienteId = 1L

    override suspend fun guardar(usuario: Usuario): Long {
        val id = siguienteId++
        datos.value = datos.value + (id to usuario.copy(id = id))
        return id
    }

    override suspend fun actualizar(usuario: Usuario) {
        datos.value = datos.value + (usuario.id to usuario)
    }

    override suspend fun eliminar(id: Long) {
        datos.value = datos.value - id
    }

    override suspend fun buscarPorId(id: Long): Usuario? = datos.value[id]

    override suspend fun buscarPorEmail(email: String): Usuario? =
        datos.value.values.firstOrNull { it.email.equals(email, ignoreCase = true) }

    override suspend fun buscarPorCedula(cedula: String): Usuario? =
        datos.value.values.firstOrNull { it.cedula == cedula }

    override fun observarTodos(): Flow<List<Usuario>> = datos.map { it.values.sortedBy { u -> u.id } }
}
