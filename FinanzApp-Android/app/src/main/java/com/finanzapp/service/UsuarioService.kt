package com.finanzapp.service

import com.finanzapp.model.Rol
import com.finanzapp.model.Usuario
import com.finanzapp.repository.UsuarioRepository
import com.finanzapp.security.PasswordHasher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Lógica de negocio de usuarios (equivalente a UsuarioService.java).
 *
 * El hashing de contraseñas es costoso, por eso se ejecuta en [dispatcher] y no en el hilo principal.
 */
class UsuarioService(
    private val repository: UsuarioRepository,
    private val hasher: PasswordHasher,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    /** Registra un usuario nuevo con rol USER. Informa si el email o la cédula ya existen. */
    suspend fun registrar(datos: DatosRegistro): ResultadoRegistro {
        val email = normalizarEmail(datos.email)
        val cedula = datos.cedula.trim()

        if (repository.buscarPorEmail(email) != null) return ResultadoRegistro.EmailDuplicado
        if (repository.buscarPorCedula(cedula) != null) return ResultadoRegistro.CedulaDuplicada

        val hash = withContext(dispatcher) { hasher.hash(datos.password) }
        val nuevo = Usuario(
            nombres = datos.nombres.trim(),
            apellidos = datos.apellidos.trim(),
            cedula = cedula,
            fechaNacimiento = datos.fechaNacimiento,
            email = email,
            password = hash,
            rol = Rol.USER
        )
        val id = repository.guardar(nuevo)
        return ResultadoRegistro.Exito(nuevo.copy(id = id))
    }

    /** Devuelve el usuario si las credenciales son correctas; `null` en caso contrario. */
    suspend fun validarLogin(email: String, password: String): Usuario? {
        val usuario = repository.buscarPorEmail(normalizarEmail(email)) ?: return null
        val correcto = withContext(dispatcher) { hasher.verify(password, usuario.password) }
        return usuario.takeIf { correcto }
    }

    fun observarTodos(): Flow<List<Usuario>> = repository.observarTodos()

    suspend fun buscarPorId(id: Long): Usuario? = repository.buscarPorId(id)

    /** Actualiza los datos editables (no toca la contraseña ni el saldo). */
    suspend fun actualizar(datos: DatosEdicion): ResultadoEdicion {
        val existente = repository.buscarPorId(datos.id) ?: return ResultadoEdicion.NoEncontrado
        val email = normalizarEmail(datos.email)
        val cedula = datos.cedula.trim()

        val otroConEmail = repository.buscarPorEmail(email)
        if (otroConEmail != null && otroConEmail.id != datos.id) return ResultadoEdicion.EmailDuplicado
        val otroConCedula = repository.buscarPorCedula(cedula)
        if (otroConCedula != null && otroConCedula.id != datos.id) return ResultadoEdicion.CedulaDuplicada

        repository.actualizar(
            existente.copy(
                nombres = datos.nombres.trim(),
                apellidos = datos.apellidos.trim(),
                cedula = cedula,
                email = email,
                rol = datos.rol,
                fechaNacimiento = datos.fechaNacimiento ?: existente.fechaNacimiento
            )
        )
        return ResultadoEdicion.Exito
    }

    suspend fun eliminar(id: Long) = repository.eliminar(id)

    private fun normalizarEmail(email: String): String = email.trim().lowercase()
}
