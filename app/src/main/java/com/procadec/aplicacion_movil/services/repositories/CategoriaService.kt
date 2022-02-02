package com.procadec.aplicacion_movil.services.repositories

import com.procadec.aplicacion_movil.services.entities.Categoria
import retrofit2.Call
import retrofit2.http.*

interface CategoriaService {

    @GET("/api/categorias")
    fun getAll(): Call<List<Categoria.All>>

    @GET("/api/categorias/{id}")
    fun getById(@Path("id") id: Long): Call<Categoria.All>

    @POST("/api/categorias")
    fun create(@Body usuario: Categoria.Create): Call<Categoria.All>

    @PUT("/api/categorias/{id}")
    fun update(@Path("id") id: Long, @Body categoria: Categoria.All): Call<Categoria.All>

    @DELETE("/api/categorias/{id}")
    fun deleteById(@Path("id") id: Long): Call<Categoria.Delete>

}