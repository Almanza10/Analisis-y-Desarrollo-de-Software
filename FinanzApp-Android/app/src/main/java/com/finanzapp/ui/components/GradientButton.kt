package com.finanzapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finanzapp.ui.theme.AccentPurple
import com.finanzapp.ui.theme.BrandA
import com.finanzapp.ui.theme.TextMain

/** Botón principal con degradado azul → morado (.btn-primary del diseño web). */
@Composable
fun GradientButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    cargando: Boolean = false,
    iconoFinal: ImageVector? = null
) {
    val forma = RoundedCornerShape(12.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(10.dp, forma, ambientColor = BrandA, spotColor = BrandA)
            .clip(forma)
            .background(
                Brush.horizontalGradient(listOf(BrandA, AccentPurple)),
                alpha = if (enabled) 1f else 0.5f
            )
            .clickable(enabled = enabled && !cargando, role = Role.Button, onClick = onClick)
            .padding(vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (cargando) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp,
                color = TextMain
            )
        } else {
            Text(text = texto, color = TextMain, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            if (iconoFinal != null) {
                Spacer(Modifier.width(8.dp))
                Icon(iconoFinal, contentDescription = null, tint = TextMain, modifier = Modifier.size(16.dp))
            }
        }
    }
}
