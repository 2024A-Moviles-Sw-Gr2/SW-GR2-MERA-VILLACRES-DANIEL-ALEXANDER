package com.example.proyecto_mangafox.Activities

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto_mangafox.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.pdfview.PDFView
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.concurrent.thread

class LeerManga : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var pdfView: PDFView
    private val defaultPdfUrl = "https://firebasestorage.googleapis.com/v0/b/qhgm-2024a-sw-gr2.appspot.com/o/Mangas%2FJujutsu%20Kaisen%2FJK01.pdf?alt=media&token=5679db74-4060-4e85-98fd-128d3d237ef5"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leer_manga)

        pdfView = findViewById(R.id.pdfView)
        val nombreManga = findViewById<TextView>(R.id.tv_nombre_manga_leermanga)
        val nombreCapitulo = findViewById<TextView>(R.id.tv_nombre_capitulo_leermanga)
        val botonRegresar = findViewById<ImageButton>(R.id.ib_regresar_leermanga)

        val mangaID = intent.getStringExtra("mangaID")
        val capituloID = intent.getStringExtra("capituloID")

        if (mangaID != null && capituloID != null) {
            obtenerTituloManga(mangaID, nombreManga)
            obtenerNombreCapitulo(mangaID, capituloID, nombreCapitulo)
            leerManga(mangaID, capituloID)
        }

        botonRegresar.setOnClickListener {
            finish()
        }

    }

    private fun obtenerTituloManga(mangaID: String, textView: TextView) {
        db.collection("Manga")
            .document(mangaID)
            .get()
            .addOnSuccessListener { document ->
                val nombreManga = document.getString("titulo") ?: "Título no disponible"
                textView.text = nombreManga
            }
            .addOnFailureListener { exception ->
                Log.d("InfoManga", "Error al obtener la información del manga: ", exception)
                textView.text = "Error al cargar título"
            }
    }

    private fun obtenerNombreCapitulo(mangaID: String, capituloID: String, textView: TextView) {
        db.collection("Manga")
            .document(mangaID)
            .collection("Capitulos")
            .document(capituloID)
            .get()
            .addOnSuccessListener { document ->
                val numCap = document.get("numCap")?.toString() ?: "Desconocido"
                val tituloCapitulo = document.get("tituloCapitulo") ?: "Título no disponible"
                textView.text = "Capítulo $numCap: $tituloCapitulo"
            }
            .addOnFailureListener { exception ->
                Log.d("InfoCapitulo", "Error al obtener la información del capítulo: ", exception)
                textView.text = "Error al cargar capítulo"
            }
    }

    private fun leerManga(mangaID: String, capituloID: String) {
        db.collection("Manga")
            .document(mangaID)
            .collection("Capitulos")
            .document(capituloID)
            .get()
            .addOnSuccessListener { document ->
                val mangaUrl = document.getString("capituloURL")
                if (!mangaUrl.isNullOrEmpty()) {
                    Log.d("LinkManga", "Link del manga: $mangaUrl")
                    mostrarManga(mangaUrl)
                } else {
                    Log.d("LinkManga", "URL del manga no encontrada, cargando URL por defecto.")
                    mostrarManga(defaultPdfUrl)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("LinkManga", "Error al obtener el link del manga: ", exception)
                mostrarManga(defaultPdfUrl)
            }
    }

    private fun mostrarManga(pdfUrl: String) {
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(pdfUrl).build()
                val response = client.newCall(request).execute()
                val inputStream: InputStream = response.body?.byteStream() ?: return@thread

                // Guarda el PDF en un archivo temporal
                val file = File.createTempFile("temp_pdf", ".pdf", cacheDir)
                val outputStream = FileOutputStream(file)

                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()

                // Mostrar el PDF en el PDFView
                runOnUiThread {
                    pdfView.fromFile(file).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Manejo de errores
            }
        }
    }
}
