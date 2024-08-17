package com.example.proyecto_mangafox.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_mangafox.Activities.RecyclerViews.RVAdapterBiblioteca
import com.example.proyecto_mangafox.Activities.RecyclerViews.InterfaceOnClick
import com.example.proyecto_mangafox.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BibliotecaFragment : Fragment(), InterfaceOnClick.ItemClickListener {

    private lateinit var adapterBiblioteca: RVAdapterBiblioteca
    val db = Firebase.firestore
    val listaMangas = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_biblioteca, container, false)

        val sectionList = mutableListOf<Pair<List<String>, String>>()

        val recyclerView: RecyclerView = view.findViewById(R.id.rv_biblioteca)

        val currentUserId = "Mataso97" // Cambia esto por el ID real del usuario

        db.collection("Usuario").document(currentUserId)
            .collection("MiBiblioteca")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    listaMangas.add(document.id)
                }

                db.collection("Manga")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            if (listaMangas.contains(document.id)) {
                                val tituloManga = document.getString("titulo") ?: ""
                                val portadaMangaURL = document.getString("portadaURL") ?: ""

                                sectionList.add(listOf(tituloManga, document.id) to portadaMangaURL)
                            }
                        }

                        // Configura el adaptador del RecyclerView después de haber llenado la lista
                        adapterBiblioteca = RVAdapterBiblioteca(this, sectionList)
                        recyclerView.adapter = adapterBiblioteca
                    }
            }

        // Cambia LinearLayoutManager a GridLayoutManager
        val gridLayoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = gridLayoutManager

        return view
    }

    override fun onItemClick(position: Int) {
        val mangaTitulo = (adapterBiblioteca).contenido[position].first[1]

        irActividad(InfoManga::class.java, mangaTitulo)
    }

    private fun irActividad(clase: Class<*>, mangaTitulo: String) {
        val intent = Intent(activity, clase)
        intent.putExtra("mangaID", mangaTitulo)
        startActivity(intent)
    }

}
