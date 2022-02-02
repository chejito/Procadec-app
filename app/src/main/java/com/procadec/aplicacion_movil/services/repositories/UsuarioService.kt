package com.procadec.aplicacion_movil.services.repositories

import com.procadec.aplicacion_movil.services.entities.Usuario
import retrofit2.Call
import retrofit2.http.*

interface UsuarioService {

    @GET("/api/usuarios")
    fun getAll(): Call<List<Usuario.All>>

    @GET("/api/usuarios/{id}")
    fun getById(@Path("id") id: Long): Call<Usuario.All>

    @POST("/api/usuarios")
    fun create(@Body usuario: Usuario.Register): Call<Usuario.All>

    @PUT("/api/usuarios/{id}")
    fun update(@Path("id") id: Long, @Body usuario: Usuario.All): Call<Usuario.All>

    @DELETE("/api/usuarios/{id}")
    fun delete(@Path("id") id: Long): Call<Usuario.Delete>

}