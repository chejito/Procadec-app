package com.procadec.aplicacion_movil.services.entities

sealed class Categoria{
    data class All(val id: Long, var nombre: String, val productos: MutableList<Producto.All>)
    data class Create(val nombre: String, val productos: MutableList<Producto.All>)
    data class Delete(val msg: String)
}
