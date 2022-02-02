package com.procadec.aplicacion_movil.services.repositories

import com.procadec.aplicacion_movil.services.entities.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginService {

    @POST("/api/usuarios/login")
    fun login(@Body usuario: Usuario.Login): Call<Usuario.All>
}