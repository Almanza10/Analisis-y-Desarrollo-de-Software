package com.finanzapp.di

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.finanzapp.FinanzappApplication
import com.finanzapp.ui.admin.AdminViewModel
import com.finanzapp.ui.inicio.InicioViewModel
import com.finanzapp.ui.login.LoginViewModel
import com.finanzapp.ui.registro.RegistroViewModel

/** Fábrica única de ViewModels: les inyecta el servicio y la sesión desde [AppContainer]. */
object AppViewModelProvider {

    val Factory = viewModelFactory {
        initializer { LoginViewModel(contenedor().usuarioService, contenedor().sessionStore) }
        initializer { RegistroViewModel(contenedor().usuarioService) }
        initializer { InicioViewModel(contenedor().usuarioService, contenedor().sessionStore) }
        initializer { AdminViewModel(contenedor().usuarioService, contenedor().sessionStore) }
    }
}

private fun CreationExtras.contenedor(): AppContainer =
    (this[APPLICATION_KEY] as FinanzappApplication).container
