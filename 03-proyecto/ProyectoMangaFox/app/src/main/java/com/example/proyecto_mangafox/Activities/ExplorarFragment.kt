package com.example.proyecto_mangafox.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_mangafox.Activities.RecyclerViews.InterfaceOnClick
import com.example.proyecto_mangafox.Activities.RecyclerViews.RVAdapterExplorar
import com.example.proyecto_mangafox.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ExplorarFragment : Fragment(), InterfaceOnClick.ItemClickListener {

    private lateinit var adapterExplorar: RVAdapterExplorar
    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explorar, container, false)

        // Inicializa sectionList como una lista mutable
        val sectionList = mutableListOf<Pair<List<String>, String>>()

        // Configura el RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_explorar)

        // Obtiene la colección "Manga" de Firestore
        db.collection("Manga")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Manga", "${document.id} => ${document.get("titulo")}")
                    // Obtiene el título y el URL de la portada del manga
                    val tituloManga = document.get("titulo").toString()
                    val portadaMangaURL = document.get("portadaURL").toString()

                    sectionList.add(listOf(tituloManga, document.id) to portadaMangaURL)
                }

                // Configura el adaptador del RecyclerView después de haber llenado la lista
                adapterExplorar = RVAdapterExplorar(this, sectionList)
                recyclerView.adapter = adapterExplorar
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreError", "Error getting documents.", exception)
            }

        // Cambia LinearLayoutManager a GridLayoutManager
        val gridLayoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = gridLayoutManager

        return view
    }

    override fun onItemClick(position: Int) {
        // Obtén el título del manga seleccionado
        val mangaTitulo = (adapterExplorar).contenido[position].first[1]

        // Llama a la función para cambiar de actividad, pasando el título del manga como extra
        irActividad(InfoManga::class.java, mangaTitulo)
    }

    private fun irActividad(clase: Class<*>, mangaTitulo: String) {
        val intent = Intent(activity, clase)
        intent.putExtra("mangaID", mangaTitulo)
        startActivity(intent)
    }

}