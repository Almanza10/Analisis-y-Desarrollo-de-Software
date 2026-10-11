package com.finanzapp.service

import com.finanzapp.fakes.FakePasswordHasher
import com.finanzapp.fakes.FakeUsuarioRepository
import com.finanzapp.model.Rol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class UsuarioServiceTest {

    private val repo = FakeUsuarioRepository()
    private val servicio = UsuarioService(repo, FakePasswordHasher(), Dispatchers.Unconfined)

    private fun datos(
        email: String = "ana@correo.com",
        cedula: String = "123456"
    ) = DatosRegistro(
        nombres = "Ana",
        apellidos = "Pérez",
        cedula = cedula,
        fechaNacimiento = LocalDate.of(1995, 3, 5),
        email = email,
        password = "secreta1"
    )

    @Test
    fun registrar_creaUsuarioConRolUserYContrasenaHasheada() = runTest {
        val resultado = servicio.registrar(datos())

        assertTrue(resultado is ResultadoRegistro.Exito)
        val usuario = (resultado as ResultadoRegistro.Exito).usuario
        assertEquals(Rol.USER, usuario.rol)
        assertNotEquals("secreta1", usuario.password)
        assertNotNull(repo.buscarPorId(usuario.id))
    }

    @Test
    fun registrar_normalizaElEmail() = runTest {
        servicio.registrar(datos(email = "  ANA@Correo.COM "))
        assertNotNull(repo.buscarPorEmail("ana@correo.com"))
    }

    @Test
    fun registrar_rechazaEmailDuplicado() = runTest {
        servicio.registrar(datos())
        val resultado = servicio.registrar(datos(email = "ANA@correo.com", cedula = "999"))
        assertEquals(ResultadoRegistro.EmailDuplicado, resultado)
    }

    @Test
    fun registrar_rechazaCedulaDuplicada() = runTest {
        servicio.registrar(datos())
        val resultado = servicio.registrar(datos(email = "otra@correo.com"))
        assertEquals(ResultadoRegistro.CedulaDuplicada, resultado)
    }

    @Test
    fun validarLogin_credencialesCorrectas() = runTest {
        servicio.registrar(datos())
        val usuario = servicio.validarLogin("ana@correo.com", "secreta1")
        assertEquals("Ana", usuario?.nombres)
    }

    @Test
    fun validarLogin_ignoraMayusculasEnElEmail() = runTest {
        servicio.registrar(datos())
        assertNotNull(servicio.validarLogin("ANA@CORREO.COM", "secreta1"))
    }

    @Test
    fun validarLogin_contrasenaIncorrectaDevuelveNull() = runTest {
        servicio.registrar(datos())
        assertNull(servicio.validarLogin("ana@correo.com", "mala"))
    }

    @Test
    fun validarLogin_usuarioInexistenteDevuelveNull() = runTest {
        assertNull(servicio.validarLogin("nadie@correo.com", "secreta1"))
    }

    @Test
    fun actualizar_cambiaLosDatosEditablesYConservaLaContrasena() = runTest {
        val creado = (servicio.registrar(datos()) as ResultadoRegistro.Exito).usuario

        val resultado = servicio.actualizar(
            DatosEdicion(creado.id, "Ana María", "Gómez", "123456", "ana@correo.com", Rol.ADMIN, null)
        )

        assertEquals(ResultadoEdicion.Exito, resultado)
        val guardado = repo.buscarPorId(creado.id)!!
        assertEquals("Ana María", guardado.nombres)
        assertEquals(Rol.ADMIN, guardado.rol)
        assertEquals(creado.password, guardado.password)
        assertEquals(creado.fechaNacimiento, guardado.fechaNacimiento)
    }

    @Test
    fun actualizar_rechazaEmailDeOtroUsuario() = runTest {
        servicio.registrar(datos())
        val otro = (servicio.registrar(datos(email = "luis@correo.com", cedula = "777")) as ResultadoRegistro.Exito).usuario

        val resultado = servicio.actualizar(
            DatosEdicion(otro.id, "Luis", "Ruiz", "777", "ana@correo.com", Rol.USER, null)
        )
        assertEquals(ResultadoEdicion.EmailDuplicado, resultado)
    }

    @Test
    fun actualizar_rechazaCedulaDeOtroUsuario() = runTest {
        servicio.registrar(datos())
        val otro = (servicio.registrar(datos(email = "luis@correo.com", cedula = "777")) as ResultadoRegistro.Exito).usuario

        val resultado = servicio.actualizar(
            DatosEdicion(otro.id, "Luis", "Ruiz", "123456", "luis@correo.com", Rol.USER, null)
        )
        assertEquals(ResultadoEdicion.CedulaDuplicada, resultado)
    }

    @Test
    fun actualizar_usuarioInexistente() = runTest {
        val resultado = servicio.actualizar(
            DatosEdicion(99, "X", "Y", "1", "x@y.com", Rol.USER, null)
        )
        assertEquals(ResultadoEdicion.NoEncontrado, resultado)
    }

    @Test
    fun eliminar_quitaAlUsuarioDeLaLista() = runTest {
        val creado = (servicio.registrar(datos()) as ResultadoRegistro.Exito).usuario
        servicio.eliminar(creado.id)
        assertTrue(servicio.observarTodos().first().isEmpty())
    }
}
