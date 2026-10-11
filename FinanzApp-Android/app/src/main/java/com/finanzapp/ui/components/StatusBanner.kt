package com.finanzapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finanzapp.ui.theme.AccentCyan
import com.finanzapp.ui.theme.ErrorRed
import com.finanzapp.ui.theme.TextMuted

/** Mensaje de éxito (cian) o de error (rojo): .msg-ok / .msg-error / .error-banner del diseño web. */
@Composable
fun StatusBanner(
    titulo: String,
    modifier: Modifier = Modifier,
    detalle: String? = null,
    esError: Boolean = false
) {
    val acento = if (esError) ErrorRed else AccentCyan
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = acento.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, acento.copy(alpha = 0.55f))
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Text(
                text = titulo,
                color = if (esError) ErrorRed else Color(0xFFBFEAFF),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            if (detalle != null) {
                Text(text = detalle, color = TextMuted, fontSize = 11.sp)
            }
        }
    }
}
