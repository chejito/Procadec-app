package com.procadec.aplicacion_movil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.procadec.aplicacion_movil.databinding.ItemProductoBinding
import com.procadec.aplicacion_movil.listeners.ProductoItemListener
import com.procadec.aplicacion_movil.services.entities.Producto

class ProductoAdapter (private val clickListener: ProductoItemListener) : RecyclerView.Adapter<ProductoAdapter.ViewHolder>(){
    var lista = mutableListOf<Producto.All>()

    fun aniadirProductos(lista: List<Producto.All>) {
        this.lista.clear()
        this.lista.addAll(lista)
        notifyDataSetChanged()
    }

    fun actualizarProductos(lista: List<Producto.All>) {
        this.lista.clear()
        this.lista.addAll(lista)
        notifyDataSetChanged()
    }

    fun aniadirProducto(producto: Producto.All){
        lista.add(producto)
        notifyDataSetChanged()
    }

    fun actualizarProducto(productoAntiguo: Producto.All,producto: Producto.All){
        val pos = lista.indexOf(productoAntiguo)
        lista[pos] = producto
        notifyDataSetChanged()
    }

    fun eliminarProducto(producto: Producto.All){
        val pos = lista.indexOf(producto)
        lista.removeAt(pos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rellenarDatos(lista[position])

        holder.restarBoton.setOnClickListener {
            clickListener.onRestarClick(lista[position], holder.cantidadTextView)
        }
        holder.sumarBoton.setOnClickListener {
            clickListener.onSumarClick(lista[position], holder.cantidadTextView)
        }
        holder.recibirBoton.setOnClickListener {
            clickListener.onRecibirClick(lista[position], holder.cantidadTextView)
        }
        holder.enviarBoton.setOnClickListener {
            clickListener.onEnviarClick(lista[position], holder.cantidadTextView)
        }
        holder.editarBoton.setOnClickListener {
            clickListener.onEditarClick(lista[position])
        }
        holder.eliminarBoton.setOnClickListener {
            this.clickListener.onEliminarClick(lista[position])
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    class ViewHolder(val binding: ItemProductoBinding, val adapter: ProductoAdapter) : RecyclerView.ViewHolder(binding.root) {
        val cantidadTextView = binding.productoNuevaCantidadEdit
        val restarBoton = binding.productoRestarBoton
        val sumarBoton = binding.productoSumarBoton
        val recibirBoton = binding.itemProductoBotonRecibir
        val enviarBoton = binding.itemProductoBotonEnviar
        val editarBoton = binding.itemProductoBotonEditar
        val eliminarBoton = binding.itemProductoBotonEliminar

        fun rellenarDatos(producto: Producto.All) {
            binding.itemProductoReferencia.text = "Referencia: \n${producto.referencia}"
            binding.itemProductoDescripcion.text = "Descripción:\n${producto.descripcion}"
            binding.itemProductoCantidad.text ="${producto.cantidad} unidades"
        }

        companion object {
            fun create(parent: ViewGroup, adapter: ProductoAdapter): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemProductoBinding.inflate(inflater, parent, false)
                return ViewHolder(binding, adapter)
            }
        }
    }
}