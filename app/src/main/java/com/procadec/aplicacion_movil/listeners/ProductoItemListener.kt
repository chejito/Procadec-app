package com.procadec.aplicacion_movil.listeners

import android.widget.TextView
import com.procadec.aplicacion_movil.services.entities.Producto

interface ProductoItemListener {
    fun onSumarClick(producto:  Producto.All, textView: TextView)
    fun onRestarClick(producto: Producto.All, textView: TextView)
    fun onRecibirClick(producto: Producto.All, textView: TextView)
    fun onEnviarClick(producto: Producto.All, textView: TextView)
    fun onEditarClick(producto: Producto.All)
    fun onEliminarClick(producto: Producto.All)
}