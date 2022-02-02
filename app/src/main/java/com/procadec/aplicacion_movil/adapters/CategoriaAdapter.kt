package com.procadec.aplicacion_movil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.procadec.aplicacion_movil.databinding.ItemCategoriaBinding
import com.procadec.aplicacion_movil.listeners.CategoriaItemListener
import com.procadec.aplicacion_movil.services.entities.Categoria

class CategoriaAdapter(private val clickListener: CategoriaItemListener) : RecyclerView.Adapter<CategoriaAdapter.ViewHolder>() {
    val lista = mutableListOf<Categoria.All>()


    fun aniadirCategorias(lista: List<Categoria.All>) {
        this.lista.addAll(lista)
        notifyDataSetChanged()
    }

    fun aniadirCategoria(categoria: Categoria.All){
        lista.add(categoria)
        notifyDataSetChanged()
    }

    fun actualizarCategoria(categoria: Categoria.All){
        val pos = lista.indexOf(categoria)
        lista[pos] = categoria
        notifyDataSetChanged()
    }

    fun eliminarCategoria(categoria: Categoria.All){
        val pos = lista.indexOf(categoria)
        lista.removeAt(pos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rellenarDatos(lista[position])
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

    class ViewHolder(val binding: ItemCategoriaBinding, val adapter: CategoriaAdapter) : RecyclerView.ViewHolder(binding.root) {
        val editarBoton = binding.itemCategoriaBotonEditar
        val eliminarBoton = binding.itemCategoriaBotonEliminar
        fun rellenarDatos(categoria: Categoria.All) {

            binding.itemCategoriaNombre.text = categoria.nombre
            binding.itemCategoriaProductos.text =
                (categoria.productos.size).toString() + """ productos"""


        }

        companion object {
            fun create(parent: ViewGroup, adapter: CategoriaAdapter): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemCategoriaBinding.inflate(inflater, parent, false)
                return ViewHolder(binding, adapter)
            }
        }
    }
}