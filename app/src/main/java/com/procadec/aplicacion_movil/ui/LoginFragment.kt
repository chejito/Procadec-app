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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.procadec.aplicacion_movil.R
import com.procadec.aplicacion_movil.application.App
import com.procadec.aplicacion_movil.databinding.FragmentLoginBinding
import com.procadec.aplicacion_movil.services.entities.Usuario
import com.procadec.aplicacion_movil.viewmodels.LoginViewModel

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater, R.layout.fragment_login, container, false
        )
        val usuario = App.obtenerUsuario()

        if (usuario != null) {
            binding.loginUsuarioTie.setText(usuario.nombre)
            binding.loginContraseniaTie.setText(usuario.contrasenia)
        }else {
            binding.loginUsuarioTie.setText("")
            binding.loginContraseniaTie.setText("")
        }

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.loginUsuarioTie.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.loginUsuarioTil.error = ""
        }

        binding.loginContraseniaTie.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.loginContraseniaTil.error = ""
        }

        binding.loginUsuarioTie.setOnClickListener {
            val texto = it as TextInputEditText
            if (texto.text!!.isNotEmpty()) {
                binding.loginUsuarioTil.error = ""
            }
        }

        binding.loginContraseniaTie.setOnClickListener {
            val texto = it as TextInputEditText
            if (texto.text!!.isNotEmpty()) {
                binding.loginContraseniaTil.error = ""
            }
        }

        binding.loginContraseniaTie.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.loginIniciarBoton.performClick()
            }
            false
        }


        binding.loginIniciarBoton.setOnClickListener {

            val usuarioEdit = binding.loginUsuarioTie
            val contraEdit = binding.loginContraseniaTie

            val usuarioLogin = Usuario.Login(
                nombre = usuarioEdit.text.toString(),
                contrasenia = contraEdit.text.toString()
            )

            val campos = listOf(usuarioEdit, contraEdit)
            var error = false

            campos.forEach {
                if (it.text.toString().isBlank()) {
                    error = true
                    when (it.id) {
                        R.id.login_usuario_tie -> {
                            binding.loginUsuarioTil.error =
                                getString(R.string.error_usuario_requerido)
                        }

                        R.id.login_contrasenia_tie -> {
                            binding.loginContraseniaTil.error =
                                getString(R.string.error_contrasenia_requerida)
                        }
                    }
                }
            }

            if (error)
                return@setOnClickListener

            if (usuarioEdit.text.toString().length < 5) {
                binding.loginUsuarioTil.error = getString(R.string.error_usuario_longitud)
                return@setOnClickListener
            }

            if (contraEdit.text.toString().length < 5) {
                binding.loginContraseniaTil.error = getString(R.string.error_contrasenia_longitud)
                return@setOnClickListener
            }

            viewModel.login(usuarioLogin).observe(viewLifecycleOwner, {

                if (it) {
                    navegarAlFragmento(R.id.action_loginFragment_to_productosFragment)
                } else {
                    Snackbar.make(binding.root, getString(R.string.error_usuario_contrasenia_incorrectos), Snackbar.LENGTH_SHORT).show()
                    usuarioEdit.setText("")
                    contraEdit.setText("")
                    usuarioEdit.requestFocus()
                }
            })
        }

        binding.loginRegistrarBoton.setOnClickListener {
            navegarAlFragmento(R.id.action_loginFragment_to_registroFragment)
        }

        return binding.root
    }

    // Metodo de navegacion
    private fun navegarAlFragmento(destino: Int) {
        view?.findNavController()?.navigate(destino)
    }

}