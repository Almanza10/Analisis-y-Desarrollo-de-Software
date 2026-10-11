package com.finanzapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finanzapp.ui.theme.AccentCyan
import com.finanzapp.ui.theme.AccentPurple
import com.finanzapp.ui.theme.ErrorRed
import com.finanzapp.ui.theme.InputBg
import com.finanzapp.ui.theme.TextFaint
import com.finanzapp.ui.theme.TextMain
import com.finanzapp.ui.theme.TextMuted

/** Etiqueta del campo; si hay error muestra el aviso en rojo a la derecha (.field-hint). */
@Composable
private fun EtiquetaCampo(etiqueta: String, error: String?) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = etiqueta,
            color = if (error != null) ErrorRed else TextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.4.sp
        )
        if (error != null) {
            Text(text = error, color = ErrorRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

/** Contenedor con borde de neón: morado normal, cian con foco, rojo con error (.input-wrap). */
@Composable
private fun ContenedorNeon(
    hayError: Boolean,
    enFoco: Boolean,
    modifier: Modifier = Modifier,
    contenido: @Composable RowScope.() -> Unit
) {
    val color = when {
        hayError -> ErrorRed
        enFoco -> AccentCyan
        else -> AccentPurple.copy(alpha = 0.55f)
    }
    val forma = RoundedCornerShape(10.dp)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, forma, ambientColor = color, spotColor = color),
        shape = forma,
        color = InputBg,
        border = BorderStroke(1.5.dp, color)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = contenido
        )
    }
}

/** Campo de texto con ícono al inicio. [error] muestra el aviso y pinta el borde en rojo. */
@Composable
fun NeonTextField(
    etiqueta: String,
    valor: String,
    onValorChange: (String) -> Unit,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    error: String? = null,
    esPassword: Boolean = false,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    accionIme: ImeAction = ImeAction.Next,
    accionesTeclado: KeyboardActions = KeyboardActions.Default
) {
    val interaccion = remember { MutableInteractionSource() }
    val enFoco by interaccion.collectIsFocusedAsState()

    Column(modifier = modifier.fillMaxWidth()) {
        EtiquetaCampo(etiqueta, error)
        ContenedorNeon(hayError = error != null, enFoco = enFoco) {
            Icon(icono, contentDescription = null, tint = TextFaint, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            BasicTextField(
                value = valor,
                onValueChange = onValorChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = TextStyle(color = TextMain, fontSize = 14.sp),
                cursorBrush = SolidColor(AccentCyan),
                visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (esPassword) KeyboardType.Password else tipoTeclado,
                    imeAction = accionIme
                ),
                keyboardActions = accionesTeclado,
                interactionSource = interaccion
            )
        }
    }
}

/** Campo de solo lectura que abre un selector de fecha al tocarlo (reemplaza <input type="date">). */
@Composable
fun NeonDateField(
    etiqueta: String,
    textoFecha: String?,
    placeholder: String,
    icono: ImageVector,
    descripcionIcono: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        EtiquetaCampo(etiqueta, error)
        ContenedorNeon(
            hayError = error != null,
            enFoco = false,
            modifier = Modifier.clickable(onClick = onClick)
        ) {
            Text(
                text = textoFecha ?: placeholder,
                color = if (textoFecha != null) TextMain else TextFaint,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(icono, contentDescription = descripcionIcono, tint = TextFaint, modifier = Modifier.size(18.dp))
        }
    }
}
