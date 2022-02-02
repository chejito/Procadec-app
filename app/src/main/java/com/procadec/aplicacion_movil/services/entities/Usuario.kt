package com.procadec.aplicacion_movil.services.entities

sealed class Usuario {
    data class All(val id: Long, val nombre: String, val email: String, val contrasenia: String)
    data class Register(val nombre: String, val email: String, val contrasenia: String)
    data class Login(val nombre: String, val email: String = "", val contrasenia: String)
    data class Delete(val msg: String)
}


