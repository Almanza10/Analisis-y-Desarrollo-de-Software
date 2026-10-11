package com.finanzapp.ui.registro

import com.finanzapp.MainDispatcherRule
import com.finanzapp.fakes.FakePasswordHasher
import com.finanzapp.fakes.FakeUsuarioRepository
import com.finanzapp.service.UsuarioService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class RegistroViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    private val repo = FakeUsuarioRepository()
    private val viewModel = RegistroViewModel(
        UsuarioService(repo, FakePasswordHasher(), Dispatchers.Unconfined)
    )

    private fun llenarFormularioValido(
        email: String = "ana@correo.com",
        cedula: String = "123456",
        fecha: LocalDate = LocalDate.of(1995, 3, 5)
    ) {
        viewModel.onNombresChange("Ana")
        viewModel.onApellidosChange("Pérez")
        viewModel.onCedulaChange(cedula)
        viewModel.onFechaChange(fecha)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange("secreta1")
    }

    @Test
    fun formularioVacio_marcaErrores() {
        var exito = false
        viewModel.registrar { exito = true }

        val estado = viewModel.uiState.value
        assertTrue(estado.camposVacios)
        assertEquals(ErrorFecha.INVALIDA, estado.errorFecha)
        assertTrue(estado.hayErrores)
        assertFalse(exito)
    }

    @Test
    fun menorDeEdad_marcaErrorDeFecha() {
        llenarFormularioValido(fecha = LocalDate.now().minusYears(10))
        var exito = false

        viewModel.registrar { exito = true }

        assertEquals(ErrorFecha.MENOR_DE_EDAD, viewModel.uiState.value.errorFecha)
        assertFalse(exito)
    }

    @Test
    fun emailInvalido_marcaErrorDeEmail() {
        llenarFormularioValido(email = "no-es-un-correo")
        var exito = false

        viewModel.registrar { exito = true }

        assertEquals(ErrorEmail.INVALIDO, viewModel.uiState.value.errorEmail)
        assertFalse(exito)
    }

    @Test
    fun registroValido_guardaElUsuarioYNotificaExito() = runTest {
        llenarFormularioValido()
        var exito = false

        viewModel.registrar { exito = true }

        assertTrue(exito)
        assertNotNull(repo.buscarPorEmail("ana@correo.com"))
        assertFalse(viewModel.uiState.value.hayErrores)
    }

    @Test
    fun emailYaRegistrado_marcaError() {
        llenarFormularioValido()
        viewModel.registrar { }

        llenarFormularioValido(cedula = "999")
        var exito = false
        viewModel.registrar { exito = true }

        assertEquals(ErrorEmail.YA_REGISTRADO, viewModel.uiState.value.errorEmail)
        assertFalse(exito)
    }

    @Test
    fun cedulaYaRegistrada_marcaError() {
        llenarFormularioValido()
        viewModel.registrar { }

        llenarFormularioValido(email = "otra@correo.com")
        viewModel.registrar { }

        assertEquals(ErrorCedula.YA_REGISTRADA, viewModel.uiState.value.errorCedula)
    }

    @Test
    fun corregirUnCampo_limpiaSuError() {
        llenarFormularioValido(email = "malo")
        viewModel.registrar { }
        assertNotNull(viewModel.uiState.value.errorEmail)

        viewModel.onEmailChange("bueno@correo.com")

        assertNull(viewModel.uiState.value.errorEmail)
    }
}
