package com.example.deber03_damv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        inicializarRecyclerView()
    }

    fun inicializarRecyclerView(){
        val sectionList = listOf(
            "Recomendados para ti" to listOf(R.drawable.ic_movie1_avatar, R.drawable.ic_movie2_annabelle, R.drawable.ic_movie3_deadpool),
            "Descubre lo mejor de Premium" to listOf(R.drawable.ic_movie4_jhonwick, R.drawable.ic_movie5_moana, R.drawable.ic_movie6_starwars),
            "Series más vistas" to listOf(R.drawable.ic_movie7_starvsevil, R.drawable.ic_movie8_noragami, R.drawable.ic_movie9_parasyte),
            "Animes más vistos" to listOf(R.drawable.ic_movie10_yourname, R.drawable.ic_movie11_onepiece, R.drawable.ic_movie12_digimon)
        )

        val recyclerView: RecyclerView = findViewById(R.id.rv_movie_sections)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerViewAdapter(this, sectionList)
    }


}