package com.finanzapp.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Formateo de valores para mostrar en pantalla. */
object Formatters {

    private val LOCALE_CO: Locale = Locale.Builder().setLanguage("es").setRegion("CO").build()
    private val FORMATO_FECHA: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    /** Ej.: 1500000 -> "$1.500.000" (sin decimales, igual que `maxFractionDigits="0"` del JSP). */
    fun moneda(valor: BigDecimal): String {
        val formato = NumberFormat.getIntegerInstance(LOCALE_CO).apply {
            roundingMode = RoundingMode.HALF_UP
        }
        return "$" + formato.format(valor)
    }

    fun fecha(fecha: LocalDate): String = FORMATO_FECHA.format(fecha)
}
