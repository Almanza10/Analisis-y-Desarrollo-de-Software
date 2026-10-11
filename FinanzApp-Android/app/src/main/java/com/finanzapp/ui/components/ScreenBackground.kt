package com.finanzapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.finanzapp.ui.theme.BgPage
import com.finanzapp.ui.theme.BrandA
import com.finanzapp.ui.theme.ScreenBg

/**
 * Fondo de pantalla completa (.bg-slot + .bg-overlay del diseño web).
 * Si más adelante tienes la ilustración final, pásala en [imagen] (un recurso de res/drawable).
 */
@Composable
fun ScreenBackground(
    modifier: Modifier = Modifier,
    @DrawableRes imagen: Int? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize().background(ScreenBg)) {
        if (imagen != null) {
            Image(
                painter = painterResource(imagen),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.radialGradient(
                        colors = listOf(BrandA.copy(alpha = 0.22f), Color.Transparent),
                        center = Offset(250f, 250f),
                        radius = 1000f
                    )
                )
            )
        }
        // Capa oscura para que el texto siempre se lea bien sobre la imagen.
        Box(
            modifier = Modifier.fillMaxSize().background(
                Brush.verticalGradient(
                    listOf(
                        BgPage.copy(alpha = 0.45f),
                        BgPage.copy(alpha = 0.55f),
                        BgPage.copy(alpha = 0.88f)
                    )
                )
            )
        )
        content()
    }
}
