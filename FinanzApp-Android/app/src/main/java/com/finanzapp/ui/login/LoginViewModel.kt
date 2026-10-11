package com.finanzapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finanzapp.model.Rol
import com.finanzapp.service.UsuarioService
import com.finanzapp.session.SessionStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class LoginError { CAMPOS_VACIOS, CREDENCIALES_INVALIDAS }

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val cargando: Boolean = false,
    val error: LoginError? = null
)

/** Lógica de la pantalla de login (equivale a AuthController.procesarLogin). */
class LoginViewModel(
    private val usuarioService: UsuarioService,
    private val sessionStore: SessionStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(valor: String) = _uiState.update { it.copy(email = valor, error = null) }

    fun onPasswordChange(valor: String) = _uiState.update { it.copy(password = valor, error = null) }

    /** Valida las credenciales; si son correctas guarda la sesión y avisa el rol para navegar. */
    fun iniciarSesion(onExito: (Rol) -> Unit) {
        val actual = _uiState.value
        if (actual.cargando) return

        if (actual.email.isBlank() || actual.password.isEmpty()) {
            _uiState.update { it.copy(error = LoginError.CAMPOS_VACIOS) }
            return
        }

        _uiState.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            val usuario = usuarioService.validarLogin(actual.email, actual.password)
            if (usuario == null) {
                _uiState.update {
                    it.copy(cargando = false, password = "", error = LoginError.CREDENCIALES_INVALIDAS)
                }
            } else {
                sessionStore.iniciar(usuario)
                _uiState.update { it.copy(cargando = false) }
                onExito(usuario.rol)
            }
        }
    }
}
