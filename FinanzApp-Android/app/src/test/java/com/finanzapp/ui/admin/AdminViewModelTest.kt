package com.finanzapp.ui.admin

import com.finanzapp.MainDispatcherRule
import com.finanzapp.fakes.FakePasswordHasher
import com.finanzapp.fakes.FakeSessionStore
import com.finanzapp.fakes.FakeUsuarioRepository
import com.finanzapp.model.Rol
import com.finanzapp.model.Usuario
import com.finanzapp.service.UsuarioService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class AdminViewModelTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    private val repo = FakeUsuarioRepository()
    private val sesion = FakeSessionStore()
    private val servicio = UsuarioService(repo, FakePasswordHasher(), Dispatchers.Unconfined)

    private fun usuario(nombres: String, email: String, cedula: String, rol: Rol = Rol.USER) = Usuario(
        nombres = nombres, apellidos = "Prueba", cedula = cedula,
        fechaNacimiento = LocalDate.of(1990, 1, 1), email = email, password = "x", rol = rol
    )

    @Test
    fun esAdmin_soloConSesionDeRolAdmin() = runTest {
        val admin = usuario("Root", "root@x.com", "1", Rol.ADMIN)
        val normal = usuario("Ana", "ana@x.com", "2")
        val vm = AdminViewModel(servicio, sesion)

        assertFalse(vm.esAdmin()) // sin sesión

        sesion.iniciar(normal.copy(id = repo.guardar(normal)))
        assertFalse(vm.esAdmin()) // sesión USER

        sesion.iniciar(admin.copy(id = repo.guardar(admin)))
        assertTrue(vm.esAdmin()) // sesión ADMIN
    }

    @Test
    fun lista_seActualizaConLosCambiosDeLaBaseDeDatos() = runTest {
        val vm = AdminViewModel(servicio, sesion)
        assertTrue(vm.uiState.value.usuarios.isEmpty())

        repo.guardar(usuario("Ana", "ana@x.com", "2"))

        assertEquals(1, vm.uiState.value.usuarios.size)
    }

    @Test
    fun guardarEdicion_actualizaYCierraElDialogo() = runTest {
        val id = repo.guardar(usuario("Ana", "ana@x.com", "2"))
        val vm = AdminViewModel(servicio, sesion)
        vm.iniciarEdicion(repo.buscarPorId(id)!!)

        vm.guardarEdicion("Ana María", "Prueba", "2", "ana@x.com", Rol.ADMIN, null)

        assertNull(vm.uiState.value.editando)
        assertEquals("Ana María", repo.buscarPorId(id)?.nombres)
        assertEquals(Rol.ADMIN, repo.buscarPorId(id)?.rol)
    }

    @Test
    fun guardarEdicion_conCamposVaciosMuestraError() = runTest {
        val id = repo.guardar(usuario("Ana", "ana@x.com", "2"))
        val vm = AdminViewModel(servicio, sesion)
        vm.iniciarEdicion(repo.buscarPorId(id)!!)

        vm.guardarEdicion("", "Prueba", "2", "ana@x.com", Rol.USER, null)

        assertEquals(AdminError.CAMPOS_VACIOS, vm.uiState.value.error)
        assertEquals("Ana", repo.buscarPorId(id)?.nombres)
    }

    @Test
    fun guardarEdicion_conEmailDeOtroUsuarioMuestraError() = runTest {
        repo.guardar(usuario("Ana", "ana@x.com", "2"))
        val idLuis = repo.guardar(usuario("Luis", "luis@x.com", "3"))
        val vm = AdminViewModel(servicio, sesion)
        vm.iniciarEdicion(repo.buscarPorId(idLuis)!!)

        vm.guardarEdicion("Luis", "Prueba", "3", "ana@x.com", Rol.USER, null)

        assertEquals(AdminError.EMAIL_DUPLICADO, vm.uiState.value.error)
    }

    @Test
    fun eliminar_requiereConfirmacion() = runTest {
        val id = repo.guardar(usuario("Ana", "ana@x.com", "2"))
        val vm = AdminViewModel(servicio, sesion)
        val ana = repo.buscarPorId(id)!!

        vm.pedirEliminar(ana)
        vm.cancelarEliminar()
        assertEquals(1, vm.uiState.value.usuarios.size)

        vm.pedirEliminar(ana)
        vm.confirmarEliminar()
        assertTrue(vm.uiState.value.usuarios.isEmpty())
    }
}
