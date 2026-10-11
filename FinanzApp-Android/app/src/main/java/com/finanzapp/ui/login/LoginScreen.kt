package com.finanzapp.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.finanzapp.model.Rol
import com.finanzapp.ui.components.BrandHeader
import com.finanzapp.ui.components.GradientButton
import com.finanzapp.ui.components.GradientTitle
import com.finanzapp.ui.components.NeonTextField
import com.finanzapp.ui.components.ScreenBackground
import com.finanzapp.ui.components.StatusBanner
import com.finanzapp.ui.theme.AccentCyan
import com.finanzapp.ui.theme.TextMuted

@Composable
fun LoginScreen(
    registroExitoso: Boolean,
    onIrARegistro: () -> Unit,
    onLoginExitoso: (Rol) -> Unit,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    ScreenBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            BrandHeader()
            Spacer(Modifier.height(24.dp))

            GradientTitle(stringResource(R.string.login_titulo))
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.login_subtitulo),
                color = TextMuted,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                modifier = Modifier.widthIn(max = 280.dp)
            )
            Spacer(Modifier.height(22.dp))

            if (registroExitoso) {
                StatusBanner(titulo = stringResource(R.string.login_registro_exitoso))
                Spacer(Modifier.height(14.dp))
            }
            estado.error?.let { error ->
                val mensaje = when (error) {
                    LoginError.CAMPOS_VACIOS -> R.string.login_error_campos_vacios
                    LoginError.CREDENCIALES_INVALIDAS -> R.string.login_error_credenciales
                }
                StatusBanner(titulo = stringResource(mensaje), esError = true)
                Spacer(Modifier.height(14.dp))
            }

            NeonTextField(
                etiqueta = stringResource(R.string.login_label_usuario),
                valor = estado.email,
                onValorChange = viewModel::onEmailChange,
                icono = Icons.Filled.Email,
                tipoTeclado = KeyboardType.Email
            )
            Spacer(Modifier.height(14.dp))
            NeonTextField(
                etiqueta = stringResource(R.string.login_label_password),
                valor = estado.password,
                onValorChange = viewModel::onPasswordChange,
                icono = Icons.Filled.Lock,
                esPassword = true,
                accionIme = ImeAction.Done,
                accionesTeclado = KeyboardActions(onDone = { viewModel.iniciarSesion(onLoginExitoso) })
            )
            Spacer(Modifier.height(20.dp))

            GradientButton(
                texto = stringResource(R.string.login_boton),
                onClick = { viewModel.iniciarSesion(onLoginExitoso) },
                cargando = estado.cargando,
                iconoFinal = Icons.AutoMirrored.Filled.ArrowForward
            )

            Spacer(Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.login_sin_cuenta) + " ",
                    color = TextMuted,
                    fontSize = 12.sp
                )
                Text(
                    text = stringResource(R.string.login_registrese),
                    color = AccentCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(onClick = onIrARegistro)
                )
            }
            Spacer(Modifier.height(10.dp))
            // Igual que el original (href="#"): aún no tiene flujo de recuperación implementado.
            Text(
                text = "🔒 " + stringResource(R.string.login_olvido_password),
                color = AccentCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
