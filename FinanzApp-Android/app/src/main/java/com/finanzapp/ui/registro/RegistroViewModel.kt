package com.finanzapp.ui.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finanzapp.service.DatosRegistro
import com.finanzapp.service.ResultadoRegistro
import com.finanzapp.service.UsuarioService
import com.finanzapp.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

enum class ErrorFecha { INVALIDA, MENOR_DE_EDAD }
enum class ErrorEmail { INVALIDO, YA_REGISTRADO }
enum class ErrorCedula { YA_REGISTRADA }

data class RegistroUiState(
    val nombres: String = "",
    val apellidos: String = "",
    val cedula: String = "",
    val fechaNacimiento: LocalDate? = null,
    val email: String = "",
    val password: String = "",
    val camposVacios: Boolean = false,
    val errorFecha: ErrorFecha? = null,
    val errorEmail: ErrorEmail? = null,
    val errorCedula: ErrorCedula? = null,
    val cargando: Boolean = false
) {
    /** Hay algún error que mostrar en el banner rojo (la "pantalla de error" del proyecto web). */
    val hayErrores: Boolean
        get() = camposVacios || errorFecha != null || errorEmail != null || errorCedula != null
}

/** Lógica del registro (equivale a AuthController.procesarRegistro + error.jsp). */
class RegistroViewModel(
    private val usuarioService: UsuarioService
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    fun onNombresChange(v: String) = _uiState.update { it.copy(nombres = v, camposVacios = false) }
    fun onApellidosChange(v: String) = _uiState.update { it.copy(apellidos = v, camposVacios = false) }
    fun onCedulaChange(v: String) =
        _uiState.update { it.copy(cedula = v, errorCedula = null, camposVacios = false) }

    fun onFechaChange(v: LocalDate) =
        _uiState.update { it.copy(fechaNacimiento = v, errorFecha = null) }

    fun onEmailChange(v: String) =
        _uiState.update { it.copy(email = v, errorEmail = null, camposVacios = false) }

    fun onPasswordChange(v: String) = _uiState.update { it.copy(password = v, camposVacios = false) }

    /** Valida el formulario y, si todo está bien, registra al usuario y llama a [onExito]. */
    fun registrar(onExito: () -> Unit) {
        val s = _uiState.value
        if (s.cargando) return

        val fecha = s.fechaNacimiento
        val camposVacios = listOf(s.nombres, s.apellidos, s.cedula, s.email, s.password).any { it.isBlank() }
        val errorFecha = when {
            fecha == null -> ErrorFecha.INVALIDA
            !Validators.esMayorDeEdad(fecha) -> ErrorFecha.MENOR_DE_EDAD
            else -> null
        }
        val errorEmail =
            if (s.email.isNotBlank() && !Validators.esEmailValido(s.email.trim())) ErrorEmail.INVALIDO else null

        if (camposVacios || errorFecha != null || errorEmail != null || fecha == null) {
            _uiState.update {
                it.copy(camposVacios = camposVacios, errorFecha = errorFecha, errorEmail = errorEmail)
            }
            return
        }

        _uiState.update { it.copy(cargando = true, camposVacios = false, errorFecha = null, errorEmail = null) }
        viewModelScope.launch {
            val resultado = usuarioService.registrar(
                DatosRegistro(
                    nombres = s.nombres,
                    apellidos = s.apellidos,
                    cedula = s.cedula,
                    fechaNacimiento = fecha,
                    email = s.email,
                    password = s.password
                )
            )
            when (resultado) {
                is ResultadoRegistro.Exito -> {
                    _uiState.update { it.copy(cargando = false) }
                    onExito()
                }
                ResultadoRegistro.EmailDuplicado ->
                    _uiState.update { it.copy(cargando = false, errorEmail = ErrorEmail.YA_REGISTRADO) }
                ResultadoRegistro.CedulaDuplicada ->
                    _uiState.update { it.copy(cargando = false, errorCedula = ErrorCedula.YA_REGISTRADA) }
            }
        }
    }
}
