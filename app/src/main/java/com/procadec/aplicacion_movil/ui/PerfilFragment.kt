package com.procadec.aplicacion_movil.ui

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.procadec.aplicacion_movil.R
import com.procadec.aplicacion_movil.application.App
import com.procadec.aplicacion_movil.databinding.FragmentPerfilBinding
import com.procadec.aplicacion_movil.services.entities.Usuario
import com.procadec.aplicacion_movil.viewmodels.PerfilViewModel

class PerfilFragment : Fragment() {

    private lateinit var binding: FragmentPerfilBinding
    private lateinit var viewModel: PerfilViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentPerfilBinding>(
            inflater, R.layout.fragment_perfil, container, false)

        val usuario = App.obtenerUsuario()
        val menu = binding.topAppBar.menu
        if (usuario != null) {
            menu.getItem(0).setTitle(usuario.nombre)
        }

        viewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)

        binding.perfilEditarContainer.visibility = View.GONE

        if (usuario != null) {
            binding.perfilNombre.setText(usuario.nombre)
            binding.perfilEmail.setText(usuario.email)
            binding.perfilContrasenia.setText(usuario.contrasenia)
        }

        binding.perfilEditarNombreTie.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.perfilEditarNombreTil.error = ""
        }

        binding.perfilEditarEmailTie.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.perfilEditarEmailTil.error = ""
        }

        binding.perfilEditarContraseniaTie.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.perfilEditarContraseniaTil.error = ""
        }

        binding.perfilEditarContraseniaRepTie.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.perfilEditarContraseniaRepTil.error = ""
        }

        binding.perfilEditarNombreTie.setOnClickListener {
            val texto = it as TextInputEditText
            if (texto.text!!.isNotEmpty()) {
                binding.perfilEditarNombreTil.error = ""
            }
        }

        binding.perfilEditarEmailTie.setOnClickListener {
            val texto = it as TextInputEditText
            if (texto.text!!.isNotEmpty()) {
                binding.perfilEditarEmailTil.error = ""
            }
        }

        binding.perfilEditarContraseniaTie.setOnClickListener {
            val texto = it as TextInputEditText
            if (texto.text!!.isNotEmpty()) {
                binding.perfilEditarContraseniaTil.error = ""
            }
        }

        binding.perfilEditarContraseniaRepTie.setOnClickListener {
            val texto = it as TextInputEditText
            if (texto.text!!.isNotEmpty()) {
                binding.perfilEditarContraseniaRepTil.error = ""
            }
        }

        binding.perfilEditarContraseniaRepTie.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.perfilEditarGuardarBoton.performClick()
            }
            false
        }

        binding.perfilBotonEditar.setOnClickListener {
            binding.perfilContainer.visibility = View.GONE
            binding.perfilEditarContainer.visibility = View.VISIBLE
            if (usuario != null) {
                binding.perfilEditarNombreTie.setText(usuario.nombre)
                binding.perfilEditarEmailTie.setText(usuario.email)
                binding.perfilEditarContraseniaTie.setText(usuario.contrasenia)
                binding.perfilEditarContraseniaRepTie.setText(usuario.contrasenia)
            }
        }
//
        binding.perfilBotonEliminar.setOnClickListener {
            if (usuario != null) {
                eliminarUsuario(usuario)
            }
        }

        binding.perfilEditarGuardarBoton.setOnClickListener {
            // Comprobar campos
            val nombreEdit = binding.perfilEditarNombreTie
            val emailEdit = binding.perfilEditarEmailTie
            val contraEdit = binding.perfilEditarContraseniaTie
            val contraRepEdit = binding.perfilEditarContraseniaRepTie



            val campos = listOf(nombreEdit, emailEdit, contraEdit, contraRepEdit)
            var error = false
            campos.forEach {
                if (it.text.toString().isBlank()){
                    error = true
                    when (it.id) {
                        R.id.perfil_editar_nombre_tie -> {
                            binding.perfilEditarNombreTil.error = getString(R.string.error_usuario_requerido)
                        }
                        R.id.perfil_editar_email_tie -> {
                            binding.perfilEditarEmailTil.error = getString(R.string.error_email_requerido)
                        }
                        R.id.perfil_editar_contrasenia_tie -> {
                            binding.perfilEditarContraseniaTil.error = getString(R.string.error_contrasenia_requerida)
                        }
                        R.id.perfil_editar_contrasenia_rep_tie -> {
                            binding.perfilEditarContraseniaRepTil.error = getString(R.string.error_contrasenia_rep_requerida)
                        }
                    }
                }
            }

            if (error)
                return@setOnClickListener

            if (nombreEdit.text.toString().length < 5) {
                binding.perfilEditarNombreTil.error = getString(R.string.error_usuario_longitud)
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailEdit.text.toString()).matches()){
                binding.perfilEditarEmailTil.error = getString(R.string.error_email_formato_invalido)
                return@setOnClickListener
            }

            if (contraEdit.text.toString().length < 5) {
                binding.perfilEditarContraseniaTil.error = getString(R.string.error_contrasenia_longitud)
                return@setOnClickListener
            }

            if (contraEdit.text.toString() != contraRepEdit.text.toString()) {
                binding.perfilEditarContraseniaTil.error = getString(R.string.error_contrasenias_no_coinciden)
                binding.perfilEditarContraseniaRepTil.error = getString(R.string.error_contrasenias_no_coinciden)
                return@setOnClickListener
            }

            // Guardar datos nuevos
            val usuarioEdit = usuario?.let { it1 ->
                Usuario.All(
                    it1.id,
                    nombreEdit.text.toString(),
                    emailEdit.text.toString(),
                    contraEdit.text.toString()
                )
            }

            if (usuarioEdit != null) {
                actualizarUsuario(usuarioEdit)
            }
        }

        binding.perfilEditarCancelarBoton.setOnClickListener {
            binding.perfilEditarNombreTie.setText("")
            binding.perfilEditarEmailTie.setText("")
            binding.perfilEditarContraseniaTie.setText("")
            binding.perfilEditarContraseniaRepTie.setText("")
            binding.perfilEditarContainer.visibility = View.GONE
            binding.perfilContainer.visibility = View.VISIBLE
        }

        binding.topAppBar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.logout -> {
                    App.limpiarPreferencias()
                    view?.findNavController()?.navigate(R.id.action_perfilFragment_to_loginFragment)
                    true
                }
                else -> false
            }
        }

        return binding.root
    }

    private fun actualizarUsuario(usuario: Usuario.All){
        viewModel.actualizarUsuario(usuario).observe(viewLifecycleOwner, {
            if (it != null) {
                Snackbar.make(binding.root, getString(R.string.usuario_actualizado),
                    Snackbar.LENGTH_SHORT).show()
                App.guardarUsuario(usuario)
                binding.topAppBar.menu.getItem(0).setTitle(usuario.nombre)
            } else {
                Snackbar.make(binding.root, getString(R.string.usuario_no_actualizado),
                    Snackbar.LENGTH_SHORT).show()
            }
        })

        binding.perfilEditarNombreTie.setText("")
        binding.perfilEditarEmailTie.setText("")
        binding.perfilEditarContraseniaTie.setText("")
        binding.perfilEditarContraseniaRepTie.setText("")
        binding.perfilEditarContainer.visibility = View.GONE

        binding.perfilNombre.setText(usuario.nombre)
        binding.perfilEmail.setText(usuario.email)
        binding.perfilContrasenia.setText(usuario.contrasenia)
        binding.perfilContainer.visibility = View.VISIBLE
    }

    private fun eliminarUsuario(usuario: Usuario.All) {
        viewModel.eliminarUsuario(usuario).observe(viewLifecycleOwner, {
            if(it) {
                Snackbar.make(binding.root,
                    getString(R.string.usuario_no_eliminado),
                    Snackbar.LENGTH_SHORT).show()

            }else {
                Snackbar.make(binding.root,
                    getString(R.string.usuario_eliminado),
                    Snackbar.LENGTH_SHORT).show()
                App.limpiarPreferencias()
                view?.findNavController()?.navigate(R.id.action_perfilFragment_to_loginFragment)
            }
        })

    }

}