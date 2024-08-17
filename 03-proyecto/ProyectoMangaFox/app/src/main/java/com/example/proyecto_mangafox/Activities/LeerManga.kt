package com.example.proyecto_mangafox.Activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.proyecto_mangafox.R
import com.pdfview.PDFView
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.concurrent.thread

class LeerManga : AppCompatActivity() {

    private lateinit var pdfView: PDFView
    private val pdfUrl = "https://firebasestorage.googleapis.com/v0/b/qhgm-2024a-sw-gr2.appspot.com/o/Mangas%2FJujutsu%20Kaisen%2FJK01.pdf?alt=media&token=5679db74-4060-4e85-98fd-128d3d237ef5"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leer_manga)

        intent.getStringExtra("mangaID")

        pdfView = findViewById(R.id.pdfView)

        // Descargar y mostrar el PDF
        downloadAndDisplayPdf(pdfUrl)
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
