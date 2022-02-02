package com.procadec.aplicacion_movil.services.repositories

import com.procadec.aplicacion_movil.services.entities.Producto
import retrofit2.Call
import retrofit2.http.*

interface ProductoService {

    @GET("/api/productos")
    fun getAll(): Call<List<Producto.All>>

    @GET("/api/productos/{id}")
    fun get(@Path("id") id: Long): Call<Producto.All>

    @POST("/api/productos")
    fun create(@Body usuario: Producto.Create): Call<Producto.All>

    @PUT("/api/productos/{id}")
    fun update(@Path("id") id: Long, @Body producto: Producto.All): Call<Producto.All>

    @DELETE("/api/productos/{id}")
    fun delete(@Path("id") id: Long): Call<Producto.Delete>

}