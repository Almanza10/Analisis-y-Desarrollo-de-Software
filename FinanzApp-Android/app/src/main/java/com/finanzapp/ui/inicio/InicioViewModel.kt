package com.finanzapp.ui.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finanzapp.model.Usuario
import com.finanzapp.service.UsuarioService
import com.finanzapp.session.SessionStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class InicioUiState(
    val usuario: Usuario? = null,
    val cargando: Boolean = true,
    val sesionInvalida: Boolean = false
)

/** Carga el usuario de la sesión (equivale a InicioController.inicio). */
class InicioViewModel(
    private val usuarioService: UsuarioService,
    private val sessionStore: SessionStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(InicioUiState())
    val uiState: StateFlow<InicioUiState> = _uiState.asStateFlow()

    init {
        cargarUsuario()
    }

    private fun cargarUsuario() {
        viewModelScope.launch {
            val sesion = sessionStore.obtener()
            if (sesion == null) {
                _uiState.update { it.copy(cargando = false, sesionInvalida = true) }
                return@launch
            }
            val usuario = usuarioService.buscarPorId(sesion.usuarioId)
            if (usuario == null) {
                // El usuario fue eliminado desde el panel admin: se invalida la sesión.
                sessionStore.cerrar()
                _uiState.update { it.copy(cargando = false, sesionInvalida = true) }
            } else {
                _uiState.update { it.copy(usuario = usuario, cargando = false) }
            }
        }
    }

    fun cerrarSesion() = sessionStore.cerrar()
}
