package com.procadec.aplicacion_movil.listeners

import com.procadec.aplicacion_movil.services.entities.Categoria

interface CategoriaItemListener {
    fun onEditarClick(categoria: Categoria.All)
    fun onEliminarClick(categoria: Categoria.All)
}