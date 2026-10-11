package com.finanzapp.ui.navigation

/** Rutas de la app (equivalen a las URL de los @GetMapping de los controladores Spring). */
object Routes {
    const val ARG_REGISTRADO = "registrado"

    /** /login (con el parámetro opcional ?registrado=true para mostrar el mensaje de éxito). */
    const val LOGIN_PATRON = "login?$ARG_REGISTRADO={$ARG_REGISTRADO}"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val INICIO = "inicio"
    const val ADMIN = "admin/usuarios"

    fun loginRegistrado(): String = "login?$ARG_REGISTRADO=true"
}
