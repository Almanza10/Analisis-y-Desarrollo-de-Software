package com.finanzapp.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.finanzapp.ui.theme.FuenteTitulos

/** Título con degradado blanco → lila (.screen-title del diseño web). */
@Composable
fun GradientTitle(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 26.sp
) {
    Text(
        text = text,
        modifier = modifier,
        style = TextStyle(
            brush = Brush.horizontalGradient(listOf(Color.White, Color(0xFFC9D2FF))),
            fontFamily = FuenteTitulos,
            fontWeight = FontWeight.ExtraBold,
            fontSize = fontSize
        )
    )
}
