package com.finanzapp.di

import android.content.Context
import com.finanzapp.config.DataSeeder
import com.finanzapp.data.AppDatabase
import com.finanzapp.repository.UsuarioRepository
import com.finanzapp.repository.UsuarioRepositoryImpl
import com.finanzapp.security.PasswordHasher
import com.finanzapp.security.Pbkdf2PasswordHasher
import com.finanzapp.service.UsuarioService
import com.finanzapp.session.SessionStore
import com.finanzapp.session.SharedPreferencesSessionStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Contenedor de dependencias manual (hace el papel del contexto de Spring / @Autowired).
 * Mantiene un único grafo de objetos para toda la app.
 */
class AppContainer(context: Context) {

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val database: AppDatabase = AppDatabase.obtener(context)
    private val passwordHasher: PasswordHasher = Pbkdf2PasswordHasher()

    val usuarioRepository: UsuarioRepository = UsuarioRepositoryImpl(database.usuarioDao())
    val usuarioService: UsuarioService = UsuarioService(usuarioRepository, passwordHasher)
    val sessionStore: SessionStore = SharedPreferencesSessionStore(context)

    private val dataSeeder = DataSeeder(usuarioRepository, passwordHasher)

    fun sembrarDatosIniciales() {
        appScope.launch { dataSeeder.run() }
    }
}
