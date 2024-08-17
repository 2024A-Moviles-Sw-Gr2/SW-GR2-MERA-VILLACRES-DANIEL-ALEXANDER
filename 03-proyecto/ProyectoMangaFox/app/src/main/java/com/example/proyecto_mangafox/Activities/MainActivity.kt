package com.example.proyecto_mangafox.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.proyecto_mangafox.R
import com.example.proyecto_mangafox.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Detectar botones del menu inferior y cambiar los fragmentos
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val username = intent.getStringExtra("username")
            val bundle = Bundle()
            bundle.putString("username", username)

            when (item.itemId) {
                R.id.mi_biblioteca -> {
                    val fragment = BibliotecaFragment()
                    fragment.arguments = bundle
                    loadFragment(fragment)
                    true
                }
                R.id.mi_explorar -> {
                    val fragment = ExplorarFragment()
                    fragment.arguments = bundle
                    loadFragment(fragment)
                    true
                }
                R.id.mi_cuenta -> {
                    val fragment = CuentaFragment()
                    fragment.arguments = bundle
                    loadFragment(fragment)
                    true
                }
                else -> false
            }
        }
        // Cargar el fragmento por defecto
        binding.bottomNavigationView.selectedItemId = R.id.mi_biblioteca
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_contenido_main, fragment)
        transaction.commit()
    }
}
