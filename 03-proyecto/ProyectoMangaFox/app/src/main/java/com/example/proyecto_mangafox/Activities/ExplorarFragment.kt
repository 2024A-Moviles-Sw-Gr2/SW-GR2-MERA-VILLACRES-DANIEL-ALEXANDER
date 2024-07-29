package com.example.proyecto_mangafox.Activities

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_mangafox.Activities.RecyclerViews.InterfaceOnClick
import com.example.proyecto_mangafox.Activities.RecyclerViews.RVAdapterExplorar
import com.example.proyecto_mangafox.R

class ExplorarFragment : Fragment(), InterfaceOnClick.ItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explorar, container, false)

        // Configura el RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_explorar)
        val sectionList = listOf(
            "One Piece" to R.drawable.ic_one_piece_manga,
            "Tokyo Ghoul" to R.drawable.ic_tokyo_ghoul_manga,
            "Kaguya-sama: Love is war" to R.drawable.ic_kaguya_sama_manga,
            "Solo Leveling" to R.drawable.ic_solo_leveling_manga,
            "Naruto" to R.drawable.ic_naruto_manga,
            "Haikyuu!!" to R.drawable.ic_haikyuu_manga,
        )

        // Cambia LinearLayoutManager a GridLayoutManager
        val gridLayoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = RVAdapterExplorar(this, sectionList)

        return view
    }

    override fun onItemClick(position: Int) {
        // Llama a la función para cambiar de actividad
        when (position) {
            0 -> irActividad(InfoManga::class.java)
        }
    }

    private fun irActividad(clase: Class<*>) {
        val intent = Intent(activity, clase)
        startActivity(intent)
    }
}
