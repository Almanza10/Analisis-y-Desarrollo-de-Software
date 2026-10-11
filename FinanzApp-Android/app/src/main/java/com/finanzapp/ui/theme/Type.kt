package com.finanzapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Las pantallas web usaban Poppins (títulos) y Manrope (texto).
 * Para usarlas en Android descarga los .ttf, ponlos en app/src/main/res/font/ y define aquí:
 *   val Poppins = FontFamily(Font(R.font.poppins_bold, FontWeight.Bold), ...)
 * Mientras tanto se usa la fuente por defecto del sistema.
 */
val FuenteTitulos: FontFamily = FontFamily.Default
val FuenteTexto: FontFamily = FontFamily.Default

val FinanzAppTypography = Typography(
    bodyMedium = TextStyle(fontFamily = FuenteTexto, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    labelLarge = TextStyle(fontFamily = FuenteTexto, fontWeight = FontWeight.Bold, fontSize = 13.sp),
    titleLarge = TextStyle(fontFamily = FuenteTitulos, fontWeight = FontWeight.ExtraBold, fontSize = 26.sp)
)
