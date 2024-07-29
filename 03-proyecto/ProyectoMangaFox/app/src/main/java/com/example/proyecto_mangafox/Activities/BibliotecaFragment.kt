package com.example.proyecto_mangafox.Activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_mangafox.Activities.RecyclerViews.RVAdapterBiblioteca
import com.example.proyecto_mangafox.Activities.RecyclerViews.InterfaceOnClick
import com.example.proyecto_mangafox.R

class BibliotecaFragment : Fragment(), InterfaceOnClick.ItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_biblioteca, container, false)

        // Configura el RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_biblioteca)
        val sectionList = listOf(
            listOf("One Piece", "1") to R.drawable.ic_one_piece_manga,
            listOf("Tokyo Ghoul", "95") to R.drawable.ic_tokyo_ghoul_manga,
            listOf("Kaguya-sama: Love is war", "70") to R.drawable.ic_kaguya_sama_manga,
            listOf("Solo Leveling", "150") to R.drawable.ic_solo_leveling_manga,
            listOf("Naruto", "84") to R.drawable.ic_naruto_manga,
            listOf("Haikyuu!!", "26") to R.drawable.ic_haikyuu_manga,
        )

        // Cambia LinearLayoutManager a GridLayoutManager
        val gridLayoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = RVAdapterBiblioteca(this, sectionList)

        return view
    }

    override fun onItemClick(position: Int) {
        // Llama a la función para cambiar de actividad
        when (position) {
            // Puedes usar el position para decidir qué actividad abrir
            0 -> irActividad(InfoManga::class.java)
        }
    }

    private fun irActividad(clase: Class<*>) {
        val intent = Intent(activity, clase)
        startActivity(intent)
    }
}
