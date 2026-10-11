package com.finanzapp.session

import android.content.Context
import com.finanzapp.model.Rol
import com.finanzapp.model.Usuario

/** Sesión persistida en SharedPreferences: sobrevive al cierre de la app hasta "Cerrar sesión". */
class SharedPreferencesSessionStore(context: Context) : SessionStore {

    private val prefs = context.applicationContext
        .getSharedPreferences(NOMBRE_ARCHIVO, Context.MODE_PRIVATE)

    override fun iniciar(usuario: Usuario) {
        prefs.edit()
            .putLong(KEY_ID, usuario.id)
            .putString(KEY_NOMBRE, usuario.nombreCompleto)
            .putString(KEY_ROL, usuario.rol.name)
            .apply()
    }

    override fun obtener(): SesionActiva? {
        val id = prefs.getLong(KEY_ID, NO_ID)
        val rol = prefs.getString(KEY_ROL, null)?.let { runCatching { Rol.valueOf(it) }.getOrNull() }
        if (id == NO_ID || rol == null) return null
        return SesionActiva(id, prefs.getString(KEY_NOMBRE, "").orEmpty(), rol)
    }

    override fun cerrar() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val NOMBRE_ARCHIVO = "finanzapp_session"
        const val KEY_ID = "usuarioId"
        const val KEY_NOMBRE = "usuarioNombre"
        const val KEY_ROL = "usuarioRol"
        const val NO_ID = -1L
    }
}
