package com.finanzapp.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.finanzapp.R
import com.finanzapp.di.AppViewModelProvider
import com.finanzapp.model.Rol
import com.finanzapp.model.Usuario
import com.finanzapp.ui.components.BrandHeader
import com.finanzapp.ui.components.FechaPickerDialog
import com.finanzapp.ui.components.GradientTitle
import com.finanzapp.ui.components.NeonDateField
import com.finanzapp.ui.components.NeonTextField
import com.finanzapp.ui.components.ScreenBackground
import com.finanzapp.ui.theme.AccentCyan
import com.finanzapp.ui.theme.AccentPurple
import com.finanzapp.ui.theme.CardBg
import com.finanzapp.ui.theme.ErrorRed
import com.finanzapp.ui.theme.TextMain
import com.finanzapp.ui.theme.TextMuted
import com.finanzapp.util.Formatters
import java.time.LocalDate

@Composable
fun AdminScreen(
    onCerrarSesion: () -> Unit,
    viewModel: AdminViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    // Protección de ruta: sin sesión ADMIN se vuelve al login.
    LaunchedEffect(Unit) {
        if (!viewModel.esAdmin()) onCerrarSesion()
    }

    ScreenBackground {
        Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {
            BarraSuperior(
                nombre = estado.adminNombre,
                onCerrarSesion = {
                    viewModel.cerrarSesion()
                    onCerrarSesion()
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Column {
                        GradientTitle(stringResource(R.string.admin_titulo))
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.admin_subtitulo),
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    }
                }
                if (estado.usuarios.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.admin_vacio),
                            color = TextMuted,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 20.dp)
                        )
                    }
                }
                items(items = estado.usuarios, key = { it.id }) { usuario ->
                    TarjetaUsuario(
                        usuario = usuario,
                        onEditar = { viewModel.iniciarEdicion(usuario) },
                        onEliminar = { viewModel.pedirEliminar(usuario) }
                    )
                }
            }
        }
    }

    estado.editando?.let { usuario ->
        DialogoEdicion(
            usuario = usuario,
            error = estado.error,
            onGuardar = viewModel::guardarEdicion,
            onCancelar = viewModel::cancelarEdicion
        )
    }

    estado.porEliminar?.let { usuario ->
        AlertDialog(
            onDismissRequest = viewModel::cancelarEliminar,
            title = { Text(stringResource(R.string.admin_eliminar_titulo)) },
            text = { Text(stringResource(R.string.admin_eliminar_mensaje, usuario.nombreCompleto)) },
            confirmButton = {
                TextButton(onClick = viewModel::confirmarEliminar) {
                    Text(stringResource(R.string.admin_eliminar), color = ErrorRed)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::cancelarEliminar) {
                    Text(stringResource(R.string.cancelar))
                }
            }
        )
    }
}

@Composable
private fun BarraSuperior(nombre: String, onCerrarSesion: () -> Unit) {
    Surface(color = Color(0xB30C0E24), modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                BrandHeader(etiqueta = stringResource(R.string.admin_etiqueta), tamanoLogo = 30.dp)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.admin_sesion, nombre),
                    color = TextMuted,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.cerrar_sesion),
                color = AccentCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onCerrarSesion)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}

/** Una fila de la tabla de usuarios del panel web, convertida en tarjeta para pantallas pequeñas. */
@Composable
private fun TarjetaUsuario(usuario: Usuario, onEditar: () -> Unit, onEliminar: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = CardBg,
        border = BorderStroke(1.dp, AccentPurple.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = usuario.nombreCompleto,
                    color = TextMain,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f)
                )
                InsigniaRol(usuario.rol)
            }
            Spacer(Modifier.height(6.dp))
            Text(text = usuario.email, color = TextMuted, fontSize = 13.sp)
            Text(text = "C.C. ${usuario.cedula}", color = TextMuted, fontSize = 12.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                text = Formatters.moneda(usuario.saldo),
                color = TextMain,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                BotonAccion(stringResource(R.string.admin_editar), AccentCyan, onEditar)
                BotonAccion(stringResource(R.string.admin_eliminar), Color(0xFFFF8FA3), onEliminar)
            }
        }
    }
}

@Composable
private fun InsigniaRol(rol: Rol) {
    val (color, fondo) = when (rol) {
        Rol.ADMIN -> Color(0xFFC9B3FF) to AccentPurple.copy(alpha = 0.18f)
        Rol.USER -> Color(0xFFA9E4FF) to AccentCyan.copy(alpha = 0.14f)
    }
    Text(
        text = rol.name,
        color = color,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(fondo)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

@Composable
private fun BotonAccion(texto: String, color: Color, onClick: () -> Unit) {
    Text(
        text = texto,
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.12f))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    )
}

/** Panel "Editando: ..." del proyecto web, como diálogo. Los campos se inicializan con el usuario. */
@Composable
private fun DialogoEdicion(
    usuario: Usuario,
    error: AdminError?,
    onGuardar: (String, String, String, String, Rol, LocalDate?) -> Unit,
    onCancelar: () -> Unit
) {
    var nombres by remember(usuario.id) { mutableStateOf(usuario.nombres) }
    var apellidos by remember(usuario.id) { mutableStateOf(usuario.apellidos) }
    var cedula by remember(usuario.id) { mutableStateOf(usuario.cedula) }
    var email by remember(usuario.id) { mutableStateOf(usuario.email) }
    var rol by remember(usuario.id) { mutableStateOf(usuario.rol) }
    var fecha by remember(usuario.id) { mutableStateOf<LocalDate?>(usuario.fechaNacimiento) }
    var mostrarFecha by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(stringResource(R.string.admin_editando, usuario.nombreCompleto)) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                error?.let {
                    val mensaje = when (it) {
                        AdminError.CAMPOS_VACIOS -> R.string.admin_error_campos_vacios
                        AdminError.EMAIL_INVALIDO -> R.string.admin_error_email_invalido
                        AdminError.EMAIL_DUPLICADO -> R.string.admin_error_email_duplicado
                        AdminError.CEDULA_DUPLICADA -> R.string.admin_error_cedula_duplicada
                        AdminError.NO_ENCONTRADO -> R.string.admin_error_no_encontrado
                    }
                    Text(text = stringResource(mensaje), color = ErrorRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                NeonTextField(stringResource(R.string.label_nombres), nombres, { nombres = it }, Icons.Filled.Person)
                NeonTextField(stringResource(R.string.label_apellidos), apellidos, { apellidos = it }, Icons.Filled.Person)
                NeonTextField(
                    stringResource(R.string.label_cedula), cedula, { cedula = it },
                    Icons.Filled.AccountBox, tipoTeclado = KeyboardType.Number
                )
                NeonDateField(
                    etiqueta = stringResource(R.string.label_fecha_nacimiento),
                    textoFecha = fecha?.let { Formatters.fecha(it) },
                    placeholder = stringResource(R.string.fecha_placeholder),
                    icono = Icons.Filled.DateRange,
                    descripcionIcono = stringResource(R.string.cd_seleccionar_fecha),
                    onClick = { mostrarFecha = true }
                )
                NeonTextField(
                    stringResource(R.string.label_email), email, { email = it },
                    Icons.Filled.Email, tipoTeclado = KeyboardType.Email
                )
                Text(
                    text = stringResource(R.string.admin_label_rol),
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Rol.entries.forEach { opcion ->
                        FilterChip(
                            selected = rol == opcion,
                            onClick = { rol = opcion },
                            label = { Text(opcion.name) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onGuardar(nombres, apellidos, cedula, email, rol, fecha) }) {
                Text(stringResource(R.string.admin_guardar))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text(stringResource(R.string.cancelar)) }
        }
    )

    if (mostrarFecha) {
        FechaPickerDialog(
            fechaInicial = fecha,
            onConfirmar = {
                fecha = it
                mostrarFecha = false
            },
            onCancelar = { mostrarFecha = false }
        )
    }
}
