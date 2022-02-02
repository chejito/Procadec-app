package com.procadec.aplicacion_movil.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.procadec.aplicacion_movil.R
import com.procadec.aplicacion_movil.databinding.FragmentInicioBinding


class InicioFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentInicioBinding>(
            inflater, R.layout.fragment_inicio, container, false)

        binding.inicioIniciarBtn.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(InicioFragmentDirections.actionInicioFragmentToLoginFragment())
        }

        return binding.root
    }

}