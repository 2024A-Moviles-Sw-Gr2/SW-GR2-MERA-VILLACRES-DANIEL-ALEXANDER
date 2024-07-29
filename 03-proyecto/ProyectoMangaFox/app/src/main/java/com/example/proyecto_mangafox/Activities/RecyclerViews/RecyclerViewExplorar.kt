package com.example.proyecto_mangafox.Activities.RecyclerViews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_mangafox.R

class RecyclerViewExplorar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_explorar)
        inicializarRecyclerView()
    }

    // ItemClickListener.kt
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }


    fun inicializarRecyclerView(){
        val sectionList = listOf(
            "One Piece" to R.drawable.ic_one_piece_manga,
            "One Piece" to R.drawable.ic_tokyo_ghoul_manga,
            "One Piece" to R.drawable.ic_kaguya_sama_manga,
            "One Piece" to R.drawable.ic_solo_leveling_manga,
            "One Piece" to R.drawable.ic_naruto_manga,
            "One Piece" to R.drawable.ic_haikyuu_manga,
        )

        val recyclerView: RecyclerView = findViewById(R.id.rv_explorar)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RVAdapterExplorar(this, sectionList)
    }
}