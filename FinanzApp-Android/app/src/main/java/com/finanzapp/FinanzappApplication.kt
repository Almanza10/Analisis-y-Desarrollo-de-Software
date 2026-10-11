package com.finanzapp

import android.app.Application
import com.finanzapp.di.AppContainer

/** Punto de entrada de la app (equivalente a FinanzappApplication.java con @SpringBootApplication). */
class FinanzappApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        container.sembrarDatosIniciales()
    }
}
