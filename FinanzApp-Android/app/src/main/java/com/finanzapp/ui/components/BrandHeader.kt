package com.finanzapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finanzapp.R
import com.finanzapp.ui.theme.AccentPurple
import com.finanzapp.ui.theme.BrandA
import com.finanzapp.ui.theme.BrandB
import com.finanzapp.ui.theme.FuenteTitulos
import com.finanzapp.ui.theme.TextFaint
import com.finanzapp.ui.theme.TextMain

/** Logo + nombre de la app. [etiqueta] agrega un texto pequeño al lado (ej. "/ ADMIN"). */
@Composable
fun BrandHeader(
    modifier: Modifier = Modifier,
    centrado: Boolean = false,
    etiqueta: String? = null,
    tamanoLogo: Dp = 26.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (centrado) Arrangement.Center else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .size(tamanoLogo)
                .clip(RoundedCornerShape(8.dp))
                .background(Brush.linearGradient(listOf(BrandA, AccentPurple))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "FA",
                color = TextMain,
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FuenteTitulos
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.app_name),
            style = TextStyle(
                brush = Brush.horizontalGradient(listOf(BrandA, BrandB)),
                fontFamily = FuenteTitulos,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        )
        if (etiqueta != null) {
            Spacer(Modifier.width(6.dp))
            Text(text = etiqueta, color = TextFaint, fontSize = 11.sp)
        }
    }
}
