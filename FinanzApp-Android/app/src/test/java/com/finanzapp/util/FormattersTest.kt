package com.finanzapp.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDate

class FormattersTest {

    @Test
    fun moneda_cero() {
        assertEquals("$0", Formatters.moneda(BigDecimal.ZERO))
    }

    @Test
    fun moneda_empiezaConSimboloYConservaLosDigitos() {
        val texto = Formatters.moneda(BigDecimal("1500000"))
        assertTrue(texto.startsWith("$"))
        assertEquals("1500000", texto.filter { it.isDigit() })
    }

    @Test
    fun moneda_noMuestraDecimales() {
        val texto = Formatters.moneda(BigDecimal("1234.56"))
        assertEquals("1235", texto.filter { it.isDigit() })
    }

    @Test
    fun fecha_formatoDiaMesAnio() {
        assertEquals("05/03/1995", Formatters.fecha(LocalDate.of(1995, 3, 5)))
    }
}
