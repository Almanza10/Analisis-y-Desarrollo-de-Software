package com.finanzapp.ui.inicio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.finanzapp.R
import com.finanzapp.di.AppViewModelProvider
import com.finanzapp.model.Usuario
import com.finanzapp.ui.components.BrandHeader
import com.finanzapp.ui.components.GradientButton
import com.finanzapp.ui.components.ScreenBackground
import com.finanzapp.ui.theme.AccentCyan
import com.finanzapp.ui.theme.AccentPurple
import com.finanzapp.ui.theme.BgPage
import com.finanzapp.ui.theme.BrandA
import com.finanzapp.ui.theme.FuenteTitulos
import com.finanzapp.ui.theme.NavActive
import com.finanzapp.ui.theme.RedAmount
import com.finanzapp.ui.theme.TextFaint
import com.finanzapp.ui.theme.TextMain
import com.finanzapp.ui.theme.TextMuted
import com.finanzapp.util.Formatters

/** Gasto de ejemplo: igual que el mockup, aún no hay entidad Transaccion. */
private data class GastoEjemplo(val monto: String)

private val gastosEjemplo = listOf(GastoEjemplo("-\$3.500"), GastoEjemplo("-\$13.500"), GastoEjemplo("-\$12.500"))
private val barrasMensuales = listOf(0.35f, 0.55f, 0.80f, 0.45f, 0.65f)
private val mesesAbreviados = listOf("E", "F", "M", "A", "M")

@Composable
fun InicioScreen(
    onCerrarSesion: () -> Unit,
    viewModel: InicioViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(estado.sesionInvalida) {
        if (estado.sesionInvalida) onCerrarSesion()
    }

    ScreenBackground {
        val usuario = estado.usuario
        if (usuario != null) {
            ContenidoInicio(
                usuario = usuario,
                onCerrarSesion = {
                    viewModel.cerrarSesion()
                    onCerrarSesion()
                }
            )
        }
    }
}

@Composable
private fun ContenidoInicio(usuario: Usuario, onCerrarSesion: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .verticalScroll(rememberScrollState())
                .padding(start = 18.dp, end = 18.dp, top = 20.dp, bottom = 90.dp)
        ) {
            BrandHeader(modifier = Modifier.fillMaxWidth(), centrado = true)
            Spacer(Modifier.height(18.dp))

            Saludo(usuario)
            Spacer(Modifier.height(18.dp))

            TarjetaSaldo(Formatters.moneda(usuario.saldo))
            Spacer(Modifier.height(22.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1.15f)) {
                    TituloSeccion(stringResource(R.string.inicio_gastos_recientes))
                    gastosEjemplo.forEach { gasto ->
                        ItemGasto(gasto)
                        Spacer(Modifier.height(8.dp))
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    TituloSeccion(stringResource(R.string.inicio_mensualmente))
                    GraficoMensual()
                }
            }

            Spacer(Modifier.height(20.dp))
            // Igual que el mockup: el botón todavía no tiene flujo (pendiente: entidad Transaccion).
            GradientButton(texto = stringResource(R.string.inicio_anadir_transaccion), onClick = {})
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.cerrar_sesion),
                color = AccentCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onCerrarSesion)
                    .padding(vertical = 6.dp)
            )
        }

        BarraNavegacionInferior(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun Saludo(usuario: Usuario) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Avatar con la inicial (placeholder de la foto de perfil del mockup).
        Box(
            modifier = Modifier
                .size(40.dp)
                .shadow(8.dp, CircleShape, ambientColor = AccentCyan, spotColor = AccentCyan)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(BrandA, AccentPurple))),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = usuario.nombres.take(1).uppercase(),
                color = TextMain,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
        }
        Spacer(Modifier.width(10.dp))
        Text(
            text = stringResource(R.string.inicio_saludo, usuario.nombres),
            color = TextMain,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun TarjetaSaldo(saldoFormateado: String) {
    val forma = RoundedCornerShape(18.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(14.dp, forma, ambientColor = BrandA, spotColor = AccentPurple)
            .clip(forma)
            .background(
                Brush.linearGradient(listOf(BrandA.copy(alpha = 0.35f), AccentPurple.copy(alpha = 0.35f)))
            )
            .padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.inicio_saldo_disponible),
            color = TextMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = saldoFormateado,
            style = TextStyle(
                brush = Brush.horizontalGradient(listOf(Color.White, Color(0xFFCFE0FF))),
                fontFamily = FuenteTitulos,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp
            )
        )
    }
}

@Composable
private fun TituloSeccion(texto: String) {
    Text(
        text = texto,
        color = TextMain,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
private fun ItemGasto(gasto: GastoEjemplo) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color(0x8C0F1028),
        border = BorderStroke(1.5.dp, AccentPurple.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(AccentPurple.copy(alpha = 0.35f))
            )
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.inicio_gasto_titulo),
                    color = TextMain,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = stringResource(R.string.inicio_gasto_subtitulo), color = TextFaint, fontSize = 9.sp)
            }
            Text(text = gasto.monto, color = RedAmount, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun GraficoMensual() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color(0x8C0F1028),
        border = BorderStroke(1.5.dp, AccentCyan.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().height(72.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                barrasMensuales.forEach { proporcion ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(proporcion)
                            .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                            .background(Brush.verticalGradient(listOf(AccentCyan, BrandA)))
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                mesesAbreviados.forEach { mes ->
                    Text(text = mes, color = TextFaint, fontSize = 9.sp)
                }
            }
        }
    }
}

/** Barra inferior del mockup: 4 accesos, el primero activo (naranja). Aún sin navegación. */
@Composable
private fun BarraNavegacionInferior(modifier: Modifier = Modifier) {
    val items: List<Triple<ImageVector, Int, Boolean>> = listOf(
        Triple(Icons.Filled.Home, R.string.nav_inicio, true),
        Triple(Icons.AutoMirrored.Filled.List, R.string.nav_graficos, false),
        Triple(Icons.Filled.Create, R.string.nav_documentos, false),
        Triple(Icons.Filled.Settings, R.string.nav_configuracion, false)
    )
    Column(modifier = modifier.fillMaxWidth().background(BgPage.copy(alpha = 0.85f))) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { (icono, descripcion, activo) ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .then(
                            if (activo) Modifier
                                .shadow(10.dp, CircleShape, ambientColor = NavActive, spotColor = NavActive)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(NavActive, Color(0xFFFF4D6D))))
                            else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = stringResource(descripcion),
                        tint = if (activo) TextMain else TextFaint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}
