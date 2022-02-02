package com.procadec.aplicacion_movil.application

import android.app.Application
import android.content.Context
import com.procadec.aplicacion_movil.services.entities.Usuario
import com.procadec.aplicacion_movil.utils.Constantes

class App : Application() {
    init {
        instancia = this
    }

    companion object {
        private var instancia: App? = null

        fun guardarUsuario(usuario: Usuario.All) {
            val prefs = instancia?.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val edit = prefs?.edit()
            edit?.putLong(Constantes.USUARIO_ID, usuario.id)
            edit?.putString(Constantes.USUARIO_EMAIL, usuario.email)
            edit?.putString(Constantes.USUARIO_NOMBRE, usuario.nombre)
            edit?.putString(Constantes.USUARIO_CONTRASENIA, usuario.contrasenia)
            edit?.apply()
        }

        fun obtenerUsuario(): Usuario.All? {
            val prefs = instancia?.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val id = prefs?.getLong(Constantes.USUARIO_ID, 0)
            val nombre = prefs?.getString(Constantes.USUARIO_NOMBRE, "")
            val email = prefs?.getString(Constantes.USUARIO_EMAIL, "")
            val contra = prefs?.getString(Constantes.USUARIO_CONTRASENIA, "")
            return Usuario.All(id!!, nombre!!, email!!, contra!!)
        }

        fun limpiarPreferencias() {
            val prefs = instancia?.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val edit = prefs?.edit()
            edit?.clear()?.apply()
        }

        fun cerrarSesion() {
            val prefs = instancia?.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val edit = prefs?.edit()
            edit?.putLong("", 0)
            edit?.putString("","")
            edit?.putString("", "")
            edit?.putString("", "")
            edit?.apply()
        }
    }
}