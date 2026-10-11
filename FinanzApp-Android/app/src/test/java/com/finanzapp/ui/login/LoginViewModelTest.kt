package com.finanzapp.ui.login

import com.finanzapp.MainDispatcherRule
import com.finanzapp.fakes.FakePasswordHasher
import com.finanzapp.fakes.FakeSessionStore
import com.finanzapp.fakes.FakeUsuarioRepository
import com.finanzapp.model.Rol
import com.finanzapp.service.DatosRegistro
import com.finanzapp.service.UsuarioService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class LoginViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    private val sesion = FakeSessionStore()
    private lateinit var viewModel: LoginViewModel

    @Before
    fun preparar() = runTest {
        val servicio = UsuarioService(FakeUsuarioRepository(), FakePasswordHasher(), Dispatchers.Unconfined)
        servicio.registrar(
            DatosRegistro("Ana", "Pérez", "123", LocalDate.of(1995, 3, 5), "ana@correo.com", "secreta1")
        )
        viewModel = LoginViewModel(servicio, sesion)
    }

    @Test
    fun camposVacios_muestraErrorYNoNavega() {
        var navego = false
        viewModel.iniciarSesion { navego = true }

        assertEquals(LoginError.CAMPOS_VACIOS, viewModel.uiState.value.error)
        assertFalse(navego)
    }

    @Test
    fun credencialesIncorrectas_muestraErrorYLimpiaLaContrasena() {
        viewModel.onEmailChange("ana@correo.com")
        viewModel.onPasswordChange("incorrecta")
        var navego = false

        viewModel.iniciarSesion { navego = true }

        val estado = viewModel.uiState.value
        assertEquals(LoginError.CREDENCIALES_INVALIDAS, estado.error)
        assertEquals("", estado.password)
        assertEquals("ana@correo.com", estado.email)
        assertFalse(estado.cargando)
        assertFalse(navego)
        assertNull(sesion.obtener())
    }

    @Test
    fun credencialesCorrectas_guardaSesionYNotificaElRol() {
        viewModel.onEmailChange("ana@correo.com")
        viewModel.onPasswordChange("secreta1")
        var rolRecibido: Rol? = null

        viewModel.iniciarSesion { rolRecibido = it }

        assertEquals(Rol.USER, rolRecibido)
        assertNotNull(sesion.obtener())
        assertEquals("Ana Pérez", sesion.obtener()?.nombre)
    }

    @Test
    fun editarCampos_borraElErrorPrevio() {
        viewModel.iniciarSesion { }
        assertNotNull(viewModel.uiState.value.error)

        viewModel.onEmailChange("a")

        assertNull(viewModel.uiState.value.error)
    }
}
