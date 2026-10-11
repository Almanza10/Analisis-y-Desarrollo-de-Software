package com.finanzapp.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.finanzapp.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/** Selector de fecha de Material 3. Devuelve la fecha elegida como [LocalDate]. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FechaPickerDialog(
    fechaInicial: LocalDate?,
    onConfirmar: (LocalDate) -> Unit,
    onCancelar: () -> Unit
) {
    // DatePicker trabaja en milisegundos UTC a medianoche.
    fun LocalDate.aMillis(): Long = atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

    val estado = rememberDatePickerState(
        initialSelectedDateMillis = fechaInicial?.aMillis(),
        initialDisplayedMonthMillis = (fechaInicial ?: LocalDate.of(2000, 1, 1)).aMillis(),
        yearRange = 1900..LocalDate.now().year
    )

    DatePickerDialog(
        onDismissRequest = onCancelar,
        confirmButton = {
            TextButton(
                enabled = estado.selectedDateMillis != null,
                onClick = {
                    estado.selectedDateMillis?.let { millis ->
                        onConfirmar(Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate())
                    }
                }
            ) { Text(stringResource(R.string.aceptar)) }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text(stringResource(R.string.cancelar)) }
        }
    ) {
        DatePicker(state = estado)
    }
}
