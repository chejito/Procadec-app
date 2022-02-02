package com.procadec.aplicacion_movil.ui

import android.os.Bundle
import android.util.Patterns
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
import com.procadec.aplicacion_movil.databinding.FragmentRegistroBinding
import com.procadec.aplicacion_movil.services.entities.Usuario
import com.procadec.aplicacion_movil.viewmodels.RegistroViewModel

class RegistroFragment : Fragment() {

    private lateinit var viewModel: RegistroViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentRegistroBinding>(
            inflater,
            R.layout.fragment_registro,
            container,
            false
        )


        viewModel = ViewModelProvider(this).get(RegistroViewModel::class.java)

        binding.registroUsuarioTie.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.registroUsuarioTil.error = ""
        }

        binding.registroEmailTie.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.registroEmailTil.error = ""
        }

        binding.registroContraseniaTie.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.registroContraseniaTil.error = ""
        }

        binding.registroContraseniaRepTie.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.registroContraseniaRepTil.error = ""
        }

        binding.registroUsuarioTie.setOnClickListener {
            val texto = it as TextInputEditText
            if (texto.text!!.isNotEmpty()) {
                binding.registroUsuarioTil.error = ""
            }
        }

        binding.registroEmailTie.setOnClickListener {
            val texto = it as TextInputEditText
            if (texto.text!!.isNotEmpty()) {
                binding.registroEmailTil.error = ""
            }
        }

        binding.registroContraseniaTie.setOnClickListener {
            val texto = it as TextInputEditText
            if (texto.text!!.isNotEmpty()) {
                binding.registroContraseniaTil.error = ""
            }
        }

        binding.registroContraseniaRepTie.setOnClickListener {
            val texto = it as TextInputEditText
            if (texto.text!!.isNotEmpty()) {
                binding.registroContraseniaRepTil.error = ""
            }
        }

        binding.registroContraseniaRepTie.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.registroRegistrarBoton.performClick()
            }
            false
        }

        binding.registroRegistrarBoton.setOnClickListener {

            val usuario = binding.registroUsuarioTie
            val email = binding.registroEmailTie
            val contra = binding.registroContraseniaTie
            val contraRep = binding.registroContraseniaRepTie

            val usuarioRegistro = Usuario.Register(
                usuario.text.toString(),
                email.text.toString(),
                contra.text.toString()
            )

            val campos = listOf(usuario, email, contra, contraRep)
            var error = false
            campos.forEach {
                if (it.text.toString().isBlank()){
                    error = true
                    when (it.id) {
                        R.id.registro_usuario_tie -> {
                            binding.registroUsuarioTil.error = getString(R.string.error_usuario_requerido)
                        }
                        R.id.registro_email_tie -> {
                            binding.registroEmailTil.error = getString(R.string.error_email_requerido)
                        }
                        R.id.registro_contrasenia_tie -> {
                            binding.registroContraseniaTil.error = getString(R.string.error_contrasenia_requerida)
                        }
                        R.id.registro_contrasenia_rep_tie -> {
                            binding.registroContraseniaRepTil.error = getString(R.string.error_contrasenia_rep_requerida)
                        }
                    }
                }
            }

            if (error)
                return@setOnClickListener

            if (usuario.text.toString().length < 5) {
                binding.registroUsuarioTil.error = getString(R.string.error_usuario_longitud)
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
                binding.registroEmailTil.error = getString(R.string.error_email_formato_invalido)
                return@setOnClickListener
            }

            if (contra.text.toString().length < 5) {
                binding.registroContraseniaTil.error = getString(R.string.error_contrasenia_longitud)
                return@setOnClickListener
            }

            if (contra.text.toString() != contraRep.text.toString()) {
                binding.registroContraseniaTil.error = getString(R.string.error_contrasenias_no_coinciden)
                binding.registroContraseniaRepTil.error = getString(R.string.error_contrasenias_no_coinciden)
                return@setOnClickListener
            }

            viewModel.registro(usuarioRegistro).observe(viewLifecycleOwner, {
                if (it.equals(1)) {
                    Snackbar.make(binding.root, getString(R.string.usuario_registrado), Snackbar.LENGTH_SHORT).show()
                    view?.findNavController()?.navigate(
                        RegistroFragmentDirections.actionRegistroFragmentToLoginFragment()
                    )
                } else {
                    Snackbar.make(binding.root, getString(R.string.usuario_ya_registrado), Snackbar.LENGTH_SHORT).show()
                }
            })

        }

        binding.registroCancelarBoton.setOnClickListener {
            view?.findNavController()?.navigate(
                RegistroFragmentDirections.actionRegistroFragmentToLoginFragment())
        }

        return binding.root
    }

}