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
import com.example.proyecto_mangafox.Activities.RecyclerViews.RVAdapterBiblioteca
import com.example.proyecto_mangafox.Activities.RecyclerViews.RVAdapterExplorar
import com.example.proyecto_mangafox.Activities.RecyclerViews.RVAdapterInfoManga
import com.example.proyecto_mangafox.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class InfoManga : AppCompatActivity(), InterfaceOnClick.ItemClickListener {

    private lateinit var adapterInfoManga: RVAdapterInfoManga
    val listaCapituloID = mutableListOf<String>()
    val db = Firebase.firestore
    val currentUserId = "Mataso97"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_manga)

        val imgGuardarBiblioteca = findViewById<ImageView>(R.id.iv_guardar_infomanga)
        val txtGuardarBiblioteca = findViewById<TextView>(R.id.tv_guardar_infomanga)

        val mangaID = intent.getStringExtra("mangaID")
        //val mangaID = "JujutsuKaisen"
        if (mangaID != null) {
            inicializarRecyclerView(mangaID)
            llenarDatosManga(mangaID)

            // Llama a la coroutine
            CoroutineScope(Dispatchers.Main).launch {
                val existeManga = searchManga(mangaID)

                if (existeManga) {
                    imgGuardarBiblioteca.setImageResource(R.drawable.ic_corazon_negro)
                    txtGuardarBiblioteca.text = "En biblioteca"
                } else {
                    imgGuardarBiblioteca.setImageResource(R.drawable.ic_corazon_blanco)
                    txtGuardarBiblioteca.text = "Añadir a la \n biblioteca"
                }
            }
        }

        imgGuardarBiblioteca.setOnClickListener {
            if(txtGuardarBiblioteca.text.equals("En biblioteca")){
                imgGuardarBiblioteca.setImageResource(R.drawable.ic_corazon_blanco)
                txtGuardarBiblioteca.text = "Añadir a la \n biblioteca"

                // Eliminar de la coleccion MiBiblioteca
                intent.getStringExtra("mangaID")?.let { it ->
                    db.collection("Usuario").document(currentUserId)
                        .collection("MiBiblioteca")
                        .document(it)
                        .delete()
                }

            }else{
                imgGuardarBiblioteca.setImageResource(R.drawable.ic_corazon_negro)
                txtGuardarBiblioteca.text = "En biblioteca"

                val mangaData = hashMapOf(
                    "ultimoCapLeido" to 1 // Valor inicial del último capítulo leído
                )

                // Guardar manga en MiBiblioteca
                intent.getStringExtra("mangaID")?.let { it ->
                    db.collection("Usuario").document(currentUserId)
                        .collection("MiBiblioteca")
                        .document(it)
                        .set(mangaData)
                }

            }
        }
    }
    suspend fun searchManga(mangaID: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val result = db.collection("Usuario").document(currentUserId)
                    .collection("MiBiblioteca")
                    .get()
                    .await()

                for (document in result) {
                    if (document.id == mangaID) {
                        return@withContext true
                    }
                }
                return@withContext false
            } catch (e: Exception) {
                Log.w("Firestore", "Error al buscar el manga en la biblioteca.", e)
                return@withContext false
            }
        }
    }

    override fun onItemClick(position: Int) {
        intent.getStringExtra("mangaID")?.let {
            db.collection("Manga")
                .document(it)
                .collection("Capitulos")
                .get()
                .addOnSuccessListener { result ->

                    for (document in result){
                        if(document.id == listaCapituloID[position]){
                            val mangaID = intent.getStringExtra("mangaID")
                            val capituloID = document.id
                            val numCapitulo = document.get("numCapitulos").toString()

                            if (mangaID != null) {
                                irActividadLeerManga(LeerManga::class.java, mangaID, capituloID, numCapitulo)
                            }
                        }
                    }

                }
        }
    }

    private fun irActividadLeerManga(clase: Class<*>, mangaID: String, capituloID: String, numCapitulo: String) {
        val intent = Intent(this, clase)
        intent.putExtra("mangaID", mangaID)
        intent.putExtra("capituloID", capituloID)
        intent.putExtra("numCapitulo", numCapitulo)
        startActivity(intent)
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
                    listaCapituloID.add(document.id)

                    sectionList.add(listOf(tituloCapitulo, fechaCapitulo, numPaginas))
                }
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreError", "Error getting documents.", exception)
            }

        val recyclerView: RecyclerView = findViewById(R.id.rv_capitulos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapterInfoManga = RVAdapterInfoManga(this, sectionList)
        recyclerView.adapter = adapterInfoManga
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