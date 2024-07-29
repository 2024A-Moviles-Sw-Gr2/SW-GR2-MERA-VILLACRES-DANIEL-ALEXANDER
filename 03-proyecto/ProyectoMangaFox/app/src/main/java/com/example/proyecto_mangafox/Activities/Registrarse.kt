package com.example.proyecto_mangafox.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.proyecto_mangafox.R

class Registrarse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

        val botonRegistrarse = findViewById<Button>(R.id.btn_registrarse)
        botonRegistrarse.setOnClickListener { irActividad(Login::class.java) }
    }

    fun irActividad(
        clase: Class<*>
    ){
        val intent = Intent(this,clase)
        startActivity(intent)
    }
}
