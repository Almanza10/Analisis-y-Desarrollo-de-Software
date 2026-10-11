package com.finanzapp.ui.registro

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.finanzapp.R
import com.finanzapp.di.AppViewModelProvider
import com.finanzapp.ui.components.BrandHeader
import com.finanzapp.ui.components.FechaPickerDialog
import com.finanzapp.ui.components.GradientButton
import com.finanzapp.ui.components.GradientTitle
import com.finanzapp.ui.components.NeonDateField
import com.finanzapp.ui.components.NeonTextField
import com.finanzapp.ui.components.ScreenBackground
import com.finanzapp.ui.components.StatusBanner
import com.finanzapp.ui.theme.AccentCyan
import com.finanzapp.ui.theme.BrandA
import com.finanzapp.ui.theme.BrandB
import com.finanzapp.ui.theme.TextMain
import com.finanzapp.ui.theme.TextMuted
import com.finanzapp.util.Formatters

@Composable
fun RegistroScreen(
    onIrALogin: () -> Unit,
    onRegistroExitoso: () -> Unit,
    viewModel: RegistroViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()
    var mostrarSelectorFecha by rememberSaveable { mutableStateOf(false) }

    val errorFecha = when (estado.errorFecha) {
        ErrorFecha.INVALIDA -> stringResource(R.string.error_fecha_invalida)
        ErrorFecha.MENOR_DE_EDAD -> stringResource(R.string.error_menor_de_edad)
        null -> null
    }
    val errorEmail = when (estado.errorEmail) {
        ErrorEmail.INVALIDO -> stringResource(R.string.error_email_invalido)
        ErrorEmail.YA_REGISTRADO -> stringResource(R.string.error_email_registrado)
        null -> null
    }
    val errorCedula = when (estado.errorCedula) {
        ErrorCedula.YA_REGISTRADA -> stringResource(R.string.error_cedula_registrada)
        null -> null
    }

    ScreenBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            GradientTitle(stringResource(R.string.registro_titulo))
            Spacer(Modifier.height(16.dp))

            if (estado.hayErrores) {
                StatusBanner(
                    titulo = stringResource(R.string.registro_banner_titulo),
                    detalle = stringResource(R.string.registro_banner_detalle),
                    esError = true
                )
                Spacer(Modifier.height(16.dp))
            }

            NeonTextField(
                etiqueta = stringResource(R.string.label_nombres),
                valor = estado.nombres,
                onValorChange = viewModel::onNombresChange,
                icono = Icons.Filled.Person
            )
            Spacer(Modifier.height(12.dp))
            NeonTextField(
                etiqueta = stringResource(R.string.label_apellidos),
                valor = estado.apellidos,
                onValorChange = viewModel::onApellidosChange,
                icono = Icons.Filled.Person
            )
            Spacer(Modifier.height(12.dp))
            NeonTextField(
                etiqueta = stringResource(R.string.label_cedula),
                valor = estado.cedula,
                onValorChange = viewModel::onCedulaChange,
                icono = Icons.Filled.AccountBox,
                tipoTeclado = KeyboardType.Number,
                error = errorCedula
            )
            Spacer(Modifier.height(12.dp))
            NeonDateField(
                etiqueta = stringResource(R.string.label_fecha_nacimiento),
                textoFecha = estado.fechaNacimiento?.let(Formatters::fecha),
                placeholder = stringResource(R.string.fecha_placeholder),
                icono = Icons.Filled.DateRange,
                descripcionIcono = stringResource(R.string.cd_seleccionar_fecha),
                onClick = { mostrarSelectorFecha = true },
                error = errorFecha
            )
            Spacer(Modifier.height(12.dp))
            NeonTextField(
                etiqueta = stringResource(R.string.label_email),
                valor = estado.email,
                onValorChange = viewModel::onEmailChange,
                icono = Icons.Filled.Email,
                tipoTeclado = KeyboardType.Email,
                error = errorEmail
            )
            Spacer(Modifier.height(12.dp))
            NeonTextField(
                etiqueta = stringResource(R.string.label_password),
                valor = estado.password,
                onValorChange = viewModel::onPasswordChange,
                icono = Icons.Filled.Lock,
                esPassword = true,
                accionIme = ImeAction.Done
            )
            Spacer(Modifier.height(20.dp))

            GradientButton(
                texto = stringResource(R.string.registro_boton),
                onClick = { viewModel.registrar(onRegistroExitoso) },
                cargando = estado.cargando
            )

            Spacer(Modifier.height(14.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(R.string.registro_ya_tiene_cuenta) + " ",
                    color = TextMuted,
                    fontSize = 12.sp
                )
                Text(
                    text = stringResource(R.string.registro_inicia_sesion),
                    color = AccentCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(onClick = onIrALogin)
                )
            }

            Spacer(Modifier.height(18.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                PestanaAuth(stringResource(R.string.tab_registro), activa = true, onClick = {}, modifier = Modifier.weight(1f))
                PestanaAuth(stringResource(R.string.tab_login), activa = false, onClick = onIrALogin, modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(18.dp))
            BrandHeader(modifier = Modifier.align(Alignment.CenterHorizontally), centrado = true)
        }
    }

    if (mostrarSelectorFecha) {
        FechaPickerDialog(
            fechaInicial = estado.fechaNacimiento,
            onConfirmar = {
                viewModel.onFechaChange(it)
                mostrarSelectorFecha = false
            },
            onCancelar = { mostrarSelectorFecha = false }
        )
    }
}

/** Pestaña "Registro | Login" (.tabs / .tab del diseño web). */
@Composable
private fun PestanaAuth(
    texto: String,
    activa: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(10.dp)
    Box(
        modifier = modifier
            .clip(forma)
            .then(
                if (activa) Modifier.background(Brush.horizontalGradient(listOf(BrandA, BrandB)))
                else Modifier.background(Color(0x660A0A19))
            )
            .clickable(onClick = onClick)
            .padding(vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = if (activa) TextMain else TextMuted,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
