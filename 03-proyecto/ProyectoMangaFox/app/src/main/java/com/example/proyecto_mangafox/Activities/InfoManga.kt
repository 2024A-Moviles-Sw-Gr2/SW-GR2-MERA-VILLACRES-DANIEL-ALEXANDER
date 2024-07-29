package com.example.proyecto_mangafox.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_mangafox.Activities.RecyclerViews.InterfaceOnClick
import com.example.proyecto_mangafox.Activities.RecyclerViews.RVAdapterInfoManga
import com.example.proyecto_mangafox.R

class InfoManga : AppCompatActivity(), InterfaceOnClick.ItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_manga)
        inicializarRecyclerView()
    }

    override fun onItemClick(position: Int) {
        // Llama a la función para cambiar de actividad
        when (position) {
            0 -> irActividad(LeerManga::class.java)
        }
    }

    fun inicializarRecyclerView(){
        val sectionList = listOf(
            listOf("Capítulo 1: Amanecer de la aventura", "20/10/1999", "Pág. 10"),
            listOf("Capítulo 2: Amanecer de la aventura", "25/11/1999", "Pág. 14"),
            listOf("Capítulo 3: Amanecer de la aventura", "12/12/1999", "Pág. 11"),
            listOf("Capítulo 4: Amanecer de la aventura", "05/01/2000", "Pág. 17"),
        )

        val recyclerView: RecyclerView = findViewById(R.id.rv_capitulos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RVAdapterInfoManga(this, sectionList)
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