package com.procadec.aplicacion_movil.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.procadec.aplicacion_movil.R
import com.procadec.aplicacion_movil.adapters.ProductoAdapter
import com.procadec.aplicacion_movil.application.App
import com.procadec.aplicacion_movil.databinding.FragmentProductosBinding
import com.procadec.aplicacion_movil.listeners.ProductoItemListener
import com.procadec.aplicacion_movil.services.entities.Categoria
import com.procadec.aplicacion_movil.services.entities.Producto
import com.procadec.aplicacion_movil.viewmodels.CategoriasViewModel
import com.procadec.aplicacion_movil.viewmodels.ProductosViewModel

class ProductosFragment : Fragment(), ProductoItemListener {

    private lateinit var binding: FragmentProductosBinding
    private lateinit var viewModelProductos: ProductosViewModel
    private lateinit var viewModelCategorias: CategoriasViewModel
    private lateinit var categoriaSeleccionada: Categoria.All
    private lateinit var productosLista: MutableList<Producto.All>
    private lateinit var productoCreado: Producto.All
    private lateinit var productoActualizado: Producto.All
    private lateinit var mAdapter: ProductoAdapter
    private var modo = ""
    private lateinit var productoAntiguo: Producto.All
    private var cantidadEditar = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate<FragmentProductosBinding>(
            inflater, R.layout.fragment_productos, container, false)

        val usuario = App.obtenerUsuario()
        val menu = binding.topAppBar.menu
        if (usuario != null) {
            menu.getItem(0).setTitle(usuario.nombre)
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                    App.limpiarPreferencias()
                    view?.findNavController()
                        ?.navigate(R.id.action_productosFragment_to_loginFragment)
                    true
                }
                R.id.icono_sesion -> {
                    view?.findNavController()
                        ?.navigate(R.id.action_productosFragment_to_perfilFragment)
                    true
                }
                else -> false
            }
        }

        val rv = binding.productosLista

        binding.productosNuevo.visibility = View.GONE
        binding.productosContenedorRvFab.visibility = View.VISIBLE
        binding.productosNuevoFab.visibility = View.GONE

        mAdapter = ProductoAdapter(this)

        var categorias: List<Categoria.All>

        rv.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }

        viewModelCategorias = ViewModelProvider(this).get(CategoriasViewModel::class.java)
        viewModelProductos = ViewModelProvider(this).get(ProductosViewModel::class.java)
        productosLista = mutableListOf()

        val chipGroup = binding.categoriasChipGrupo

        viewModelCategorias.cargarCategorias().observe(viewLifecycleOwner, {

            val inflator = LayoutInflater.from(chipGroup.context)
            categorias = it

            mAdapter.aniadirProductos(productosLista)

            for (categoria in categorias) {
                val chip = inflator.inflate(R.layout.chip_personalizada, chipGroup, false) as Chip
                chip.text = categoria.nombre
                chip.tag = categorias.indexOf(categoria)
                chip.setOnClickListener {
                    productosLista.clear()
                    categoriaSeleccionada = categorias[chip.tag as Int]
                    productosLista.addAll(categoria.productos)
                    mAdapter.aniadirProductos(productosLista)
                    binding.productosNuevoFab.visibility = View.VISIBLE
                    Log.d("CHIP", (categoriaSeleccionada.toString()))
                }
                chipGroup.addView(chip)
            }
        })

        binding.productosNuevaReferenciaTie.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.productosNuevaReferenciaTil.error = ""
        }

        binding.productosNuevaDescripcionTie.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.productosNuevaDescripcionTil.error = ""
        }

        binding.productosNuevaCantidadTie.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.productosNuevaCantidadTil.error = ""
        }

        binding.productosNuevaReferenciaTie.setOnClickListener {
            val texto = it as TextInputEditText
            if (texto.text!!.isNotEmpty()) {
                binding.productosNuevaReferenciaTil.error = ""
            }
        }

        binding.productosNuevaDescripcionTie.setOnClickListener {
            val texto = it as TextInputEditText
            if (texto.text!!.isNotEmpty()) {
                binding.productosNuevaDescripcionTil.error = ""
            }
        }

        binding.productosNuevaCantidadTie.setOnClickListener {
            val texto = it as TextInputEditText
            if (texto.text!!.isNotEmpty()) {
                binding.productosNuevaCantidadTil.error = ""
            }
        }

        binding.productosNuevaCantidadTie.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.productosNuevaCantidadTil.performClick()
            }
            false
        }

        binding.productosNuevoFab.setOnClickListener {
            modo = "Nuevo producto"
            binding.productosNuevaReferenciaTie.setText("")
            binding.productosNuevaDescripcionTie.setText("")
            binding.productosNuevaCantidadTie.setText("")
            binding.productosNuevoProducto.setText(modo)
            binding.productosNuevo.visibility = View.VISIBLE
            binding.productosContenedorRvFab.visibility = View.GONE
        }

        binding.productosNuevoProductoGuardarBoton.setOnClickListener {

            val referencia = binding.productosNuevaReferenciaTie
            val descripcion = binding.productosNuevaDescripcionTie
            val cantidad = binding.productosNuevaCantidadTie

            if (referencia.text.toString().isBlank()) {
                binding.productosNuevaReferenciaTil.error =
                    getString(R.string.error_referencia_requerida)
                return@setOnClickListener
            }

            if (referencia.text.toString().length < 5 || referencia.text.toString().length > 20) {
                binding.productosNuevaReferenciaTil.error =
                    getString(R.string.error_referencia_longitud)
                return@setOnClickListener
            }

            if (descripcion.text.toString().isBlank()) {
                binding.productosNuevaDescripcionTil.error =
                    getString(R.string.error_descripcion_requerida)
                return@setOnClickListener
            }

            if (descripcion.text.toString().length < 5 || referencia.text.toString().length > 200) {
                binding.productosNuevaDescripcionTil.error =
                    getString(R.string.error_descripcion_longitud)
                return@setOnClickListener
            }

            if (cantidad.text.toString().isBlank()) {
                binding.productosNuevaCantidadTil.error =
                    getString(R.string.error_cantidad_requerida)
                return@setOnClickListener
            }

            if (!cantidad.text.toString().isDigitsOnly()) {
                binding.productosNuevaCantidadTil.error =
                    getString(R.string.error_cantidad_entero)
                return@setOnClickListener
            }

            if (modo.equals("Nuevo producto")) {
                val productoCrear: Producto.Create = Producto.Create(
                    referencia.text.toString(),
                    descripcion.text.toString(),
                    cantidad.text.toString().toInt()
                )
                crearProducto(productoCrear, categoriaSeleccionada)
            } else {
                val productoNuevo: Producto.All = Producto.All(
                    productoAntiguo.id,
                    referencia.text.toString(),
                    descripcion.text.toString(),
                    cantidad.text.toString().toInt()
                )

                actualizarProducto(productoAntiguo, productoNuevo, categoriaSeleccionada)
            }
        }

        binding.productosNuevoProductoCancelarBoton.setOnClickListener {
            binding.productosNuevaReferenciaTie.setText("")
            binding.productosNuevaDescripcionTie.setText("")
            binding.productosNuevaCantidadTie.setText("")
            binding.productosNuevoProducto.text = ""
            binding.productosNuevo.visibility = View.GONE
            binding.productosContenedorRvFab.visibility = View.VISIBLE
        }

        return binding.root
    }

    fun crearProducto(productoCrear: Producto.Create, categoriaSeleccionada: Categoria.All) {
        viewModelProductos.crearProducto(productoCrear).observe(viewLifecycleOwner, {
            if (it != null) {
                mAdapter.aniadirProducto(it)
                Snackbar.make(binding.root,
                    getString(R.string.producto_creado),
                    Snackbar.LENGTH_SHORT).show()
                productoCreado = it

                categoriaSeleccionada.productos.add(productoCreado)

                viewModelCategorias.actualizarCategoria(categoriaSeleccionada)
                    .observe(viewLifecycleOwner, {

                    })

                binding.productosNuevaReferenciaTie.setText("")
                binding.productosNuevaDescripcionTie.setText("")
                binding.productosNuevaCantidadTie.setText("")
                binding.productosNuevoProducto.setText("")
                modo = ""
                binding.productosNuevo.visibility = View.GONE
                binding.productosContenedorRvFab.visibility = View.VISIBLE
            } else {
                Snackbar.make(binding.root,
                    getString(R.string.producto_no_creado),
                    Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    fun actualizarProducto(
        productoAntiguo: Producto.All,
        productoNuevo: Producto.All,
        categoriaSeleccionada: Categoria.All,
    ) {
        viewModelProductos.actualizarProducto(productoNuevo).observe(viewLifecycleOwner, {
            if (it != null) {
                mAdapter.actualizarProducto(productoAntiguo, it)
                Snackbar.make(binding.root,
                    getString(R.string.producto_actualizado),
                    Snackbar.LENGTH_SHORT).show()
                productoActualizado = it

                val indice = categoriaSeleccionada.productos.indexOf(productoAntiguo)
                categoriaSeleccionada.productos[indice] = productoNuevo

                viewModelCategorias.actualizarCategoria(categoriaSeleccionada)
                    .observe(viewLifecycleOwner, {

                    })

                binding.productosNuevaReferenciaTie.setText("")
                binding.productosNuevaDescripcionTie.setText("")
                binding.productosNuevaCantidadTie.setText("")
                binding.productosNuevoProducto.setText("")
                modo = ""
                binding.productosNuevo.visibility = View.GONE
                binding.productosContenedorRvFab.visibility = View.VISIBLE
            } else {
                Snackbar.make(binding.root,
                    getString(R.string.producto_no_actualizado),
                    Snackbar.LENGTH_SHORT).show()
            }
        })
    }


    override fun onSumarClick(producto: Producto.All, textView: TextView) {
        var texto = textView.text.toString().toInt()
        texto += 1
        textView.text = texto.toString()
    }

    override fun onRestarClick(producto: Producto.All, textView: TextView) {
        var texto = textView.text.toString().toInt()
        if(texto > 0)
        texto -= 1
        textView.text = texto.toString()
    }

    override fun onRecibirClick(producto: Producto.All, textView: TextView) {
        var texto = textView.text.toString().toInt()
        val cantidadNueva = texto + producto.cantidad
        val productoActualizado: Producto.All = Producto.All(
            producto.id,
            producto.referencia,
            producto.descripcion,
            cantidadNueva
        )
        actualizarProducto(producto, productoActualizado, categoriaSeleccionada)
        textView.text = "0"
    }

    override fun onEnviarClick(producto: Producto.All, textView: TextView) {
        var texto = textView.text.toString().toInt()
        if (texto <= producto.cantidad) {
            val cantidadNueva = producto.cantidad - texto
            val productoActualizado: Producto.All = Producto.All(
                producto.id,
                producto.referencia,
                producto.descripcion,
                cantidadNueva
            )
            actualizarProducto(producto, productoActualizado, categoriaSeleccionada)
        }else{
            Snackbar.make(binding.root,
                getString(R.string.error_no_stock),
                Snackbar.LENGTH_SHORT).show()
        }
        textView.text = "0"
    }

    override fun onEditarClick(producto: Producto.All) {
        modo = "Editar producto"
        binding.productosNuevaReferenciaTie.setText(producto.referencia)
        binding.productosNuevaDescripcionTie.setText(producto.descripcion)
        binding.productosNuevaCantidadTie.setText(producto.cantidad.toString())
        binding.productosNuevoProducto.setText(modo)
        binding.productosNuevo.visibility = View.VISIBLE
        binding.productosContenedorRvFab.visibility = View.GONE
        productoAntiguo = producto
    }

    override fun onEliminarClick(producto: Producto.All) {

        viewModelProductos.eliminarProducto(producto).observe(viewLifecycleOwner, {
            if (it == null) {
                Snackbar.make(binding.root,
                    getString(R.string.producto_eliminado),
                    Snackbar.LENGTH_SHORT).show()
                mAdapter.eliminarProducto(producto)
                categoriaSeleccionada.productos.remove(producto)

                viewModelCategorias.actualizarCategoria(categoriaSeleccionada)
                    .observe(viewLifecycleOwner, {

                    })
            } else {
                Snackbar.make(binding.root,
                    getString(R.string.producto_no_eliminado),
                    Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}