package com.procadec.aplicacion_movil.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.procadec.aplicacion_movil.R
import com.procadec.aplicacion_movil.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val bottomNavView = binding.bottomNavView
        val navController = findNavController(R.id.myNavHostFragment)
        bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.productosFragment -> showBottomNavigation()
                R.id.categoriasFragment -> showBottomNavigation()
                R.id.perfilFragment -> showBottomNavigation()
                else -> hideBottomNavigation()
            }
        }

    }

    private fun hideBottomNavigation() {
        binding.bottomNavView.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        binding.bottomNavView.visibility = View.VISIBLE
    }

}