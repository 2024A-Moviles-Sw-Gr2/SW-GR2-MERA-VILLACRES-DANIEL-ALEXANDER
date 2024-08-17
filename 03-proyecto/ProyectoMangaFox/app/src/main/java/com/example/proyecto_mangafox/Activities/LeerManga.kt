package com.example.proyecto_mangafox.Activities

import android.os.Bundle
import android.util.Log
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

        //val mangaID = "JujutsuKaisen"
        //val numCapitulo = "JK01"
        val mangaID = intent.getStringExtra("mangaID")
        val capituloID = intent.getStringExtra("capituloID")
        val numCapitulo = intent.getStringExtra("numCapitulo")
        //obtenerInfoCapitulo(mangaID, numCapitulo)
        if (mangaID != null && capituloID != null) {
            obtenerUrlManga(mangaID, capituloID)
        }
    }

    /*private fun obtenerInfoCapitulo(mangaID: String, numCapitulo: String): Pair<String, String> {
        var tituloCapitulo = ""
        var numeroCapitulo = ""
        db.collection("Manga")
            .document(mangaID)
            .collection("Capitulos")
            .document(numCapitulo)
            .get()
            .addOnSuccessListener { document ->
                tituloCapitulo = document.getString("tituloCapitulo") ?: ""
            }
            .addOnFailureListener { exception ->
                Log.d("InfoCapitulo", "Error al obtener la información del capítulo: ", exception)
            }
        return Pair(tituloCapitulo, numCapitulo)
    }*/

    private fun obtenerUrlManga(mangaID: String, numCapitulo: String) {
        db.collection("Manga")
            .document(mangaID)
            .collection("Capitulos")
            .document(numCapitulo)
            .get()
            .addOnSuccessListener { document ->
                val mangaUrl = document.getString("capituloURL")
                if (!mangaUrl.isNullOrEmpty()) {
                    Log.d("LinkManga", "Link del manga: $mangaUrl")
                    downloadAndDisplayPdf(mangaUrl)
                } else {
                    Log.d("LinkManga", "URL del manga no encontrada, cargando URL por defecto.")
                    downloadAndDisplayPdf(defaultPdfUrl)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("LinkManga", "Error al obtener el link del manga: ", exception)
                downloadAndDisplayPdf(defaultPdfUrl)
            }
    }

    private fun downloadAndDisplayPdf(pdfUrl: String) {
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
