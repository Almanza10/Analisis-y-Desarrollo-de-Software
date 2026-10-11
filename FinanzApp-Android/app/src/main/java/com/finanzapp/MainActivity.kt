package com.finanzapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.finanzapp.model.Rol
import com.finanzapp.ui.navigation.FinanzAppNavHost
import com.finanzapp.ui.navigation.Routes
import com.finanzapp.ui.theme.FinanzAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)

        // Si ya hay una sesión guardada, se entra directo a la pantalla que corresponde al rol.
        val contenedor = (application as FinanzappApplication).container
        val destinoInicial = when (contenedor.sessionStore.obtener()?.rol) {
            Rol.ADMIN -> Routes.ADMIN
            Rol.USER -> Routes.INICIO
            null -> Routes.LOGIN_PATRON
        }

        setContent {
            FinanzAppTheme {
                FinanzAppNavHost(destinoInicial = destinoInicial)
            }
        }
    }
}
