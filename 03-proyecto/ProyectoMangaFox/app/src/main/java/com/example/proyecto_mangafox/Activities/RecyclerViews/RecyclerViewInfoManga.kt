package com.example.proyecto_mangafox.Activities.RecyclerViews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_mangafox.R

class RecyclerViewInfoManga : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_manga)
        inicializarRecyclerView()
    }

    // ItemClickListener.kt
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }


    fun inicializarRecyclerView(){
        val sectionList = listOf(
            1 to listOf("Capítulo 1 Romance Down: Amanecer de la aventura", "20/10/1999", "Pág. 10"),
            2 to listOf("Capítulo 2 Romance Down: Amanecer de la aventura", "25/11/1999", "Pág. 14"),
            3 to listOf("Capítulo 3 Romance Down: Amanecer de la aventura", "12/12/1999", "Pág. 11"),
            4 to listOf("Capítulo 4 Romance Down: Amanecer de la aventura", "05/01/2000", "Pág. 17"),
        )

        val recyclerView: RecyclerView = findViewById(R.id.rv_capitulos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RVAdapterInfoManga(this, sectionList)
    }
}