package com.finanzapp.fakes

import com.finanzapp.model.Usuario
import com.finanzapp.session.SesionActiva
import com.finanzapp.session.SessionStore

class FakeSessionStore : SessionStore {
    private var sesion: SesionActiva? = null

    override fun iniciar(usuario: Usuario) {
        sesion = SesionActiva(usuario.id, usuario.nombreCompleto, usuario.rol)
    }

    override fun obtener(): SesionActiva? = sesion

    override fun cerrar() {
        sesion = null
    }
}
