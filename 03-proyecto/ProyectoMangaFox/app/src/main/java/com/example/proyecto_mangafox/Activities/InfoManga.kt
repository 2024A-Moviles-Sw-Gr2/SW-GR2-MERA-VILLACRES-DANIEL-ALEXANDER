package com.example.proyecto_mangafox.Activities

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_mangafox.Activities.RecyclerViews.InterfaceOnClick
import com.example.proyecto_mangafox.Activities.RecyclerViews.RVAdapterExplorar
import com.example.proyecto_mangafox.Activities.RecyclerViews.RVAdapterInfoManga
import com.example.proyecto_mangafox.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class InfoManga : AppCompatActivity(), InterfaceOnClick.ItemClickListener {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_manga)

        //val mangaID = intent.getStringExtra("mangaID")
        val mangaID = "JujutsuKaisen"
        inicializarRecyclerView(mangaID)
        llenarDatosManga(mangaID)

    }

    override fun onItemClick(position: Int) {
        // Llama a la función para cambiar de actividad
        when (position) {
            0 -> irActividad(LeerManga::class.java)
        }
    }

    fun llenarDatosManga(mangaID: String){
        val portadaManga = findViewById<ImageView>(R.id.iv_portada_frente)
        val portadaFondo = findViewById<ImageView>(R.id.iv_portada_fondo)
        val nombreManga = findViewById<TextView>(R.id.tv_nombre_manga_infomanga)
        val autorManga = findViewById<TextView>(R.id.tv_nombre_autor_infomanga)
        val estadoManga = findViewById<TextView>(R.id.tv_estado_infomanga)
        val descripcionManga = findViewById<TextView>(R.id.tv_descipcion_infomanga)
        val generoManga = findViewById<TextView>(R.id.tv_genero1)
        val numCapitulos = findViewById<TextView>(R.id.tv_num_capitulos_infomanga)

        // Obtiene los datos del manga desde Firestore
        db.collection("Manga")
            .document(mangaID)
            .get()
            .addOnSuccessListener { result ->
                val portadaMangaURL = result.get("portadaURL").toString()
                Glide.with(this)
                    .load(portadaMangaURL)
                    .into(portadaManga)
                Glide.with(this)
                    .load(portadaMangaURL)
                    .into(portadaFondo)
                nombreManga.text = result.get("titulo").toString()
                autorManga.text = result.get("autor").toString()
                estadoManga.text = result.get("estado").toString()
                descripcionManga.text = result.get("descripcion").toString()
                generoManga.text = result.get("genero").toString()
                numCapitulos.text = result.get("numCapitulos").toString() + " capítulos"
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreError", "Error getting documents.", exception)
            }
    }

    fun inicializarRecyclerView(mangaID: String){
        // Inicializa sectionList como una lista mutable
        val sectionList = mutableListOf<List<String>>()

        // Obtiene los capítulos del manga desde Firestore
        db.collection("Manga")
            .document(mangaID)
            .collection("Capitulos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Capitulo", "${document.id} => ${document.get("titulo")}")
                    // Obtiene el título, fecha de publicación y número de página del capítulo
                    val tituloCapitulo = "Capítulo " + document.get("numCap") + ": " + document.get("tituloCapitulo").toString()
                    val fechaCapitulo = document.get("fechaPublicacion").toString()
                    val numPaginas = "Pág. " + document.get("numPaginas").toString()

                    sectionList.add(listOf(tituloCapitulo, fechaCapitulo, numPaginas))
                }
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreError", "Error getting documents.", exception)
            }

        /*val sectionList = listOf(
            listOf("Capítulo 1: Amanecer de la aventura", "20/10/1999", "Pág. 10"),
            listOf("Capítulo 2: Amanecer de la aventura", "25/11/1999", "Pág. 14"),
            listOf("Capítulo 3: Amanecer de la aventura", "12/12/1999", "Pág. 11"),
            listOf("Capítulo 4: Amanecer de la aventura", "05/01/2000", "Pág. 17"),
        )*/

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