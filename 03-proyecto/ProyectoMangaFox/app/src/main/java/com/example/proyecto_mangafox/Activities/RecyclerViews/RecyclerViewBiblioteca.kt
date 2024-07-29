package com.example.proyecto_mangafox.Activities.RecyclerViews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_mangafox.R

class RecyclerViewBiblioteca : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_biblioteca)
        inicializarRecyclerView()
    }

    // ItemClickListener.kt
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }


    fun inicializarRecyclerView(){
        val sectionList = listOf(
            listOf("One Piece", "1") to R.drawable.ic_one_piece_manga,
            listOf("Tokyo Ghoul", "95") to R.drawable.ic_tokyo_ghoul_manga,
            listOf("Kaguya-sama: Love is war", "70") to R.drawable.ic_kaguya_sama_manga,
            listOf("Solo Leveling", "150") to R.drawable.ic_solo_leveling_manga,
            listOf("Naruto", "84") to R.drawable.ic_naruto_manga,
            listOf("Haikyuu!!", "26") to R.drawable.ic_haikyuu_manga,
        )

        val recyclerView: RecyclerView = findViewById(R.id.rv_biblioteca)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RVAdapterBiblioteca(this, sectionList)
    }
}