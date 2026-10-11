package com.finanzapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.finanzapp.model.Rol
import com.finanzapp.ui.admin.AdminScreen
import com.finanzapp.ui.inicio.InicioScreen
import com.finanzapp.ui.login.LoginScreen
import com.finanzapp.ui.registro.RegistroScreen

/** Grafo de navegación: Login ⇄ Registro, y desde Login → Inicio (USER) o Admin (ADMIN). */
@Composable
fun FinanzAppNavHost(
    destinoInicial: String,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = destinoInicial) {

        composable(
            route = Routes.LOGIN_PATRON,
            arguments = listOf(
                navArgument(Routes.ARG_REGISTRADO) {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { entrada ->
            LoginScreen(
                registroExitoso = entrada.arguments?.getBoolean(Routes.ARG_REGISTRADO) ?: false,
                onIrARegistro = { navController.navigate(Routes.REGISTRO) },
                onLoginExitoso = { rol ->
                    val destino = if (rol == Rol.ADMIN) Routes.ADMIN else Routes.INICIO
                    navController.navigate(destino) {
                        popUpTo(Routes.LOGIN_PATRON) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.REGISTRO) {
            RegistroScreen(
                onIrALogin = { navController.popBackStack() },
                onRegistroExitoso = {
                    navController.navigate(Routes.loginRegistrado()) {
                        popUpTo(Routes.LOGIN_PATRON) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.INICIO) {
            InicioScreen(
                onCerrarSesion = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.INICIO) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ADMIN) {
            AdminScreen(
                onCerrarSesion = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.ADMIN) { inclusive = true }
                    }
                }
            )
        }
    }
}
