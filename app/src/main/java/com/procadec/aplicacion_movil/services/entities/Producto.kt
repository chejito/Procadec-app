package com.procadec.aplicacion_movil.services.entities

sealed class Producto {
    data class All(val id: Long, val referencia: String, val descripcion: String, val cantidad: Int)
    data class Create(val referencia: String, val descripcion: String, val cantidad: Int)
    data class Delete(val msg: String)
}