package com.example.proyecto_mangafox.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.proyecto_mangafox.R

class InfoManga : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_manga)
    }

    fun irActividad(
        clase: Class<*>
    ){
        val intent = Intent(this,clase)
        startActivity(intent)
    }

    fun btn_regresar(view: View) {
        irActividad(MainActivity::class.java)
    }

    fun btn_guardar(view: View) {}
    fun btn_leer(view: View) {
        irActividad(LeerManga::class.java)
    }
}