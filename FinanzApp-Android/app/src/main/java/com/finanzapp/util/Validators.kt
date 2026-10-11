package com.finanzapp.util

import java.time.LocalDate

/** Validaciones de formulario (antes dentro de AuthController.java). */
object Validators {

    private val EMAIL_REGEX = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private const val EDAD_MINIMA = 18L

    fun esEmailValido(email: String): Boolean = EMAIL_REGEX.matches(email)

    /** `true` si la persona tiene al menos 18 años cumplidos en [hoy]. */
    fun esMayorDeEdad(fechaNacimiento: LocalDate, hoy: LocalDate = LocalDate.now()): Boolean =
        !fechaNacimiento.isAfter(hoy.minusYears(EDAD_MINIMA))
}
