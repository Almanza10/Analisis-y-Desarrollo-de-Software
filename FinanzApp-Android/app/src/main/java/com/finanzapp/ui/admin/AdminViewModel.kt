package com.finanzapp.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finanzapp.model.Rol
import com.finanzapp.model.Usuario
import com.finanzapp.service.DatosEdicion
import com.finanzapp.service.ResultadoEdicion
import com.finanzapp.service.UsuarioService
import com.finanzapp.session.SessionStore
import com.finanzapp.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

enum class AdminError { CAMPOS_VACIOS, EMAIL_INVALIDO, EMAIL_DUPLICADO, CEDULA_DUPLICADA, NO_ENCONTRADO }

data class AdminUiState(
    val adminNombre: String = "",
    val usuarios: List<Usuario> = emptyList(),
    /** Usuario que se está editando (panel/diálogo de edición abierto). */
    val editando: Usuario? = null,
    /** Usuario pendiente de confirmar eliminación. */
    val porEliminar: Usuario? = null,
    val error: AdminError? = null
)

/** Lógica del panel de administración (equivale a AdminController.java). */
class AdminViewModel(
    private val usuarioService: UsuarioService,
    private val sessionStore: SessionStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AdminUiState(adminNombre = sessionStore.obtener()?.nombre.orEmpty())
    )
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        // La lista se actualiza sola cuando cambia la tabla (Room emite en cada cambio).
        viewModelScope.launch {
            usuarioService.observarTodos().collect { lista ->
                _uiState.update { it.copy(usuarios = lista) }
            }
        }
    }

    /** Equivale a noEsAdmin(session): solo una sesión con rol ADMIN puede ver el panel. */
    fun esAdmin(): Boolean = sessionStore.obtener()?.rol == Rol.ADMIN

    fun iniciarEdicion(usuario: Usuario) = _uiState.update { it.copy(editando = usuario, error = null) }

    fun cancelarEdicion() = _uiState.update { it.copy(editando = null, error = null) }

    fun guardarEdicion(
        nombres: String,
        apellidos: String,
        cedula: String,
        email: String,
        rol: Rol,
        fechaNacimiento: LocalDate?
    ) {
        val editando = _uiState.value.editando ?: return

        if (listOf(nombres, apellidos, cedula, email).any { it.isBlank() }) {
            _uiState.update { it.copy(error = AdminError.CAMPOS_VACIOS) }
            return
        }
        if (!Validators.esEmailValido(email.trim())) {
            _uiState.update { it.copy(error = AdminError.EMAIL_INVALIDO) }
            return
        }

        viewModelScope.launch {
            val resultado = usuarioService.actualizar(
                DatosEdicion(editando.id, nombres, apellidos, cedula, email, rol, fechaNacimiento)
            )
            when (resultado) {
                ResultadoEdicion.Exito -> _uiState.update { it.copy(editando = null, error = null) }
                ResultadoEdicion.NoEncontrado -> _uiState.update { it.copy(error = AdminError.NO_ENCONTRADO) }
                ResultadoEdicion.EmailDuplicado -> _uiState.update { it.copy(error = AdminError.EMAIL_DUPLICADO) }
                ResultadoEdicion.CedulaDuplicada -> _uiState.update { it.copy(error = AdminError.CEDULA_DUPLICADA) }
            }
        }
    }

    fun pedirEliminar(usuario: Usuario) = _uiState.update { it.copy(porEliminar = usuario) }

    fun cancelarEliminar() = _uiState.update { it.copy(porEliminar = null) }

    fun confirmarEliminar() {
        val usuario = _uiState.value.porEliminar ?: return
        _uiState.update { it.copy(porEliminar = null) }
        viewModelScope.launch { usuarioService.eliminar(usuario.id) }
    }

    fun cerrarSesion() = sessionStore.cerrar()
}
