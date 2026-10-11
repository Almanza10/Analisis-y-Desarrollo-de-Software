package com.finanzapp.data

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDate

/** Conversores de tipos que SQLite no soporta de forma nativa. */
class Converters {

    @TypeConverter
    fun fechaATexto(fecha: LocalDate?): String? = fecha?.toString() // yyyy-MM-dd

    @TypeConverter
    fun textoAFecha(texto: String?): LocalDate? = texto?.let { LocalDate.parse(it) }

    @TypeConverter
    fun decimalATexto(valor: BigDecimal?): String? = valor?.toPlainString()

    @TypeConverter
    fun textoADecimal(texto: String?): BigDecimal? = texto?.let { BigDecimal(it) }
}
