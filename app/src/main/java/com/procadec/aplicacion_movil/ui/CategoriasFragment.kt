package com.procadec.aplicacion_movil.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.procadec.aplicacion_movil.R
import com.procadec.aplicacion_movil.adapters.CategoriaAdapter
import com.procadec.aplicacion_movil.application.App
import com.procadec.aplicacion_movil.databinding.FragmentCategoriasBinding
import com.procadec.aplicacion_movil.listeners.CategoriaItemListener
import com.procadec.aplicacion_movil.services.entities.Categoria
import com.procadec.aplicacion_movil.services.entities.Producto
import com.procadec.aplicacion_movil.viewmodels.CategoriasViewModel

class CategoriasFragment : Fragment(), CategoriaItemListener {

    private lateinit var binding: FragmentCategoriasBinding
    private lateinit var viewModel: CategoriasViewModel
    private lateinit var mAdapter: CategoriaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate<FragmentCategoriasBinding>(
            inflater, R.layout.fragment_categorias, container, false)

        val usuario = App.obtenerUsuario()
        val menu = binding.topAppBar.menu
        if (usuario != null) {
            menu.getItem(0).setTitle(usuario.nombre)
        }

        val rv = binding.categoriasLista

        binding.categoriasContenedorRvFab.visibility = View.VISIBLE
        binding.categoriasNuevaCategoria.visibility = View.GONE
        binding.categoriasEditarCategoria.visibility = View.GONE

        mAdapter = CategoriaAdapter(this)


        rv.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }

        viewModel = ViewModelProvider(this).get(CategoriasViewModel::class.java)

        viewModel.cargarCategorias().observe(viewLifecycleOwner, {
            mAdapter.aniadirCategorias(it)
        })

        binding.categoriasNuevoNombreTie.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.categoriasNuevoNombreTil.error = ""
        }

        binding.categoriasNuevoNombreTie.setOnClickListener {
            val texto = it as TextInputEditText
            if (texto.text!!.isNotEmpty()) {
                binding.categoriasNuevoNombreTil.error = ""
            }
        }

        binding.categoriasNuevaFab.setOnClickListener {
            binding.categoriasNuevoNombre.setText(getString(R.string.nueva_categoria))
            binding.categoriasContenedorRvFab.visibility = View.GONE
            binding.categoriasNuevaCategoria.visibility = View.VISIBLE
        }

        binding.categoriasNuevoNombreTie.setOnEditorActionListener {_, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.categoriasNuevaCategoriaGuardarBoton.performClick()
            }
            false
        }

        binding.categoriasNuevaCategoriaGuardarBoton.setOnClickListener {

            val categoriaNombre = binding.categoriasNuevoNombreTie
            val categoriaProductos =  mutableListOf<Producto.All>()
            val categoria = Categoria.Create(categoriaNombre.text.toString()
                .replaceFirstChar { it.uppercase() }, categoriaProductos)


            if (categoriaNombre.text.toString().isBlank()) {
                binding.categoriasNuevoNombreTil.error =
                    getString(R.string.error_nombre_cat_requerido)
                return@setOnClickListener
            }

            if (categoriaNombre.text.toString().length < 5) {
                binding.categoriasNuevoNombreTil.error =
                    getString(R.string.error_nombre_cat_longitud)
                return@setOnClickListener
            }

            viewModel.crearCategoria(categoria).observe(viewLifecycleOwner, {
                if (it != null) {
                    mAdapter.aniadirCategoria(it)
                    Snackbar.make(binding.root,
                        getString(R.string.categoria_creada),
                        Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.root,
                        getString(R.string.categoria_no_creada),
                        Snackbar.LENGTH_SHORT).show()
                }
            })

            binding.categoriasNuevoNombreTie.setText("")
            binding.categoriasContenedorRvFab.visibility = View.VISIBLE
            binding.categoriasNuevaCategoria.visibility = View.GONE
        }


        binding.categoriasNuevaCategoriaCancelarBoton.setOnClickListener() {
            binding.categoriasNuevoNombreTie.setText("")
            binding.categoriasContenedorRvFab.visibility = View.VISIBLE
            binding.categoriasNuevaCategoria.visibility = View.GONE
        }

        binding.topAppBar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.logout -> {
                    App.limpiarPreferencias()
                    view?.findNavController()?.navigate(R.id.action_categoriasFragment_to_loginFragment)
                    true
                }
                R.id.icono_sesion -> {
                    view?.findNavController()?.navigate(R.id.action_categoriasFragment_to_perfilFragment)
                    true
                }
                else -> false
            }
        }

        return binding.root
    }

    override fun onEditarClick(categoria: Categoria.All) {
        binding.categoriasEditarNombreTie.setText(categoria.nombre)
        binding.categoriasNuevoNombreTil.requestFocus()
        binding.categoriasContenedorRvFab.visibility = View.GONE
        binding.categoriasEditarCategoria.visibility = View.VISIBLE

        binding.categoriasEditarNombreTie.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.categoriasNuevoNombreTil.error = ""
        }

        binding.categoriasEditarNombreTie.setOnClickListener {
            val texto = it as TextInputEditText
            if (texto.text!!.isNotEmpty()) {
                binding.categoriasNuevoNombreTil.error = ""
            }
        }

        binding.categoriasEditarNombreTie.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.categoriasEditarCategoriaGuardarBoton.performClick()
            }
            false
        }

        binding.categoriasEditarCategoriaGuardarBoton.setOnClickListener {

            val categoriaNombre = binding.categoriasEditarNombreTie

            if (categoriaNombre.text.toString().isBlank()) {
                binding.categoriasEditarNombreTil.error =
                    getString(R.string.error_nombre_cat_requerido)
                return@setOnClickListener
            }

            if (categoriaNombre.text.toString().length < 5) {
                binding.categoriasEditarNombreTil.error =
                    getString(R.string.error_nombre_cat_longitud)
                return@setOnClickListener
            }

            categoria.nombre = categoriaNombre.text.toString()

            viewModel.actualizarCategoria(categoria).observe(viewLifecycleOwner, {
                if (it != null) {
                    mAdapter.actualizarCategoria(categoria)
                    Snackbar.make(binding.root,
                        getString(R.string.categoria_actualizada),
                        Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.root,
                        getString(R.string.categoria_no_actualizada),
                        Snackbar.LENGTH_SHORT).show()
                }
            })

            mAdapter.actualizarCategoria(categoria)

            binding.categoriasEditarNombreTie.setText("")
            binding.categoriasContenedorRvFab.visibility = View.VISIBLE
            binding.categoriasEditarCategoria.visibility = View.GONE
        }

        binding.categoriasEditarCategoriaCancelarBoton.setOnClickListener() {
            binding.categoriasEditarNombreTie.setText("")
            binding.categoriasContenedorRvFab.visibility = View.VISIBLE
            binding.categoriasEditarCategoria.visibility = View.GONE
        }
    }

    override fun onEliminarClick(categoria: Categoria.All) {
        viewModel.eliminarCategoria(categoria).observe(viewLifecycleOwner, {
            if (it == null) {
                mAdapter.eliminarCategoria(categoria)
                Snackbar.make(binding.root,
                    getString(R.string.categoria_eliminada),
                    Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root,
                    getString(R.string.categoria_no_eliminada),
                    Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}