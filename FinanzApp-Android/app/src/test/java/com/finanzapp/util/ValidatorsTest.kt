package com.finanzapp.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ValidatorsTest {

    @Test
    fun emailValido_aceptaFormatoNormal() {
        assertTrue(Validators.esEmailValido("ana@correo.com"))
        assertTrue(Validators.esEmailValido("ana.perez+finanzas@mail.co"))
    }

    @Test
    fun emailInvalido_rechazaFormatosIncorrectos() {
        assertFalse(Validators.esEmailValido(""))
        assertFalse(Validators.esEmailValido("ana"))
        assertFalse(Validators.esEmailValido("ana@"))
        assertFalse(Validators.esEmailValido("ana@correo"))
        assertFalse(Validators.esEmailValido("ana @correo.com"))
    }

    @Test
    fun mayorDeEdad_cumple18HoyEsValido() {
        val hoy = LocalDate.of(2026, 6, 15)
        assertTrue(Validators.esMayorDeEdad(LocalDate.of(2008, 6, 15), hoy))
    }

    @Test
    fun mayorDeEdad_faltaUnDiaEsInvalido() {
        val hoy = LocalDate.of(2026, 6, 15)
        assertFalse(Validators.esMayorDeEdad(LocalDate.of(2008, 6, 16), hoy))
    }

    @Test
    fun mayorDeEdad_fechaFuturaEsInvalida() {
        val hoy = LocalDate.of(2026, 6, 15)
        assertFalse(Validators.esMayorDeEdad(LocalDate.of(2030, 1, 1), hoy))
    }

    @Test
    fun mayorDeEdad_adultoEsValido() {
        val hoy = LocalDate.of(2026, 6, 15)
        assertTrue(Validators.esMayorDeEdad(LocalDate.of(1990, 1, 1), hoy))
    }
}
