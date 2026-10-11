package com.finanzapp.data

import android.database.sqlite.SQLiteConstraintException
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.finanzapp.model.Rol
import com.finanzapp.model.Usuario
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import androidx.room.Room
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigDecimal
import java.time.LocalDate

/** Prueba instrumentada: Room real (SQLite en memoria) en un emulador o dispositivo. */
@RunWith(AndroidJUnit4::class)
class UsuarioDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: UsuarioDao

    @Before
    fun crearBd() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dao = db.usuarioDao()
    }

    @After
    fun cerrarBd() = db.close()

    private fun usuario(email: String = "ana@correo.com", cedula: String = "123") = Usuario(
        nombres = "Ana",
        apellidos = "Pérez",
        cedula = cedula,
        fechaNacimiento = LocalDate.of(1995, 3, 5),
        email = email,
        password = "hash",
        saldo = BigDecimal("1500000.50")
    )

    @Test
    fun insertarYLeer_conservaFechaSaldoYRol() = runBlocking {
        val id = dao.insertar(usuario())

        val leido = dao.buscarPorId(id)
        assertNotNull(leido)
        assertEquals(LocalDate.of(1995, 3, 5), leido!!.fechaNacimiento)
        assertEquals(BigDecimal("1500000.50"), leido.saldo)
        assertEquals(Rol.USER, leido.rol)
    }

    @Test
    fun buscarPorEmail_ignoraMayusculas() = runBlocking {
        dao.insertar(usuario())
        assertNotNull(dao.buscarPorEmail("ANA@CORREO.COM"))
        assertNull(dao.buscarPorEmail("otro@correo.com"))
    }

    @Test(expected = SQLiteConstraintException::class)
    fun emailDuplicado_lanzaExcepcion(): Unit = runBlocking {
        dao.insertar(usuario())
        dao.insertar(usuario(cedula = "999"))
        Unit
    }

    @Test(expected = SQLiteConstraintException::class)
    fun cedulaDuplicada_lanzaExcepcion(): Unit = runBlocking {
        dao.insertar(usuario())
        dao.insertar(usuario(email = "otro@correo.com"))
        Unit
    }

    @Test
    fun eliminar_yObservarTodos() = runBlocking {
        val id = dao.insertar(usuario())
        assertEquals(1, dao.observarTodos().first().size)

        dao.eliminarPorId(id)

        assertEquals(0, dao.observarTodos().first().size)
    }
}
