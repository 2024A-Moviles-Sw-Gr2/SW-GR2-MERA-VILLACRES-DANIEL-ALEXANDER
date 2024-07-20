package com.example.deber02_damv

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.deber02_damv.SQLite.BaseDeDatos
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale

class FormularioLibros : AppCompatActivity() {

    private var operacion: String = ""
    private var id: Int = -1
    private var biblioteca: String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_libros)

        operacion = intent.getStringExtra("operacion") ?: ""
        id = intent.getIntExtra("id", -1)
        biblioteca = intent.getStringExtra("biblioteca") ?: ""

        val botonFormulario = findViewById<Button>(R.id.btn_formulario_libro)
        botonFormulario.setOnClickListener {
            val titulo = findViewById<EditText>(R.id.ti_titulo_libro).text.toString()
            val autor = findViewById<EditText>(R.id.ti_autor_libro).text.toString()
            val anioStr = findViewById<EditText>(R.id.ti_anio_libro).text.toString()
            val precioStr = findViewById<EditText>(R.id.ti_precio_libro).text.toString()
            val disponibleStr = findViewById<EditText>(R.id.ti_disponible_libro).text.toString()

            val anio = anioStr.toInt()
            val precio = precioStr.toDouble()
            val disponible = when (disponibleStr.trim().lowercase()) {
                "si" -> true
                "no" -> false
                else -> false
            }

            val respuesta = if (operacion == "crear") {
                BaseDeDatos.tablas!!.crearLibro(
                    titulo,
                    autor,
                    anio,
                    precio,
                    disponible,
                    biblioteca
                )
            } else {
                BaseDeDatos.tablas!!.actualizarLibro(
                    id,
                    titulo,
                    autor,
                    anio,
                    precio,
                    disponible
                )
            }
            if (respuesta) {
                devolverRespuesta(respuesta)
            }
        }

        if (operacion == "crear") {
            pantallaCrearLibro()
        } else if (operacion == "actualizar") {
            pantallaActualizarLibro(id)
        }
    }

    private fun devolverRespuesta(respuesta: Boolean) {
        val intentDevolverRespuesta = Intent()
        intentDevolverRespuesta.putExtra("respuesta", respuesta)
        setResult(RESULT_OK, intentDevolverRespuesta)
        finish()
    }

    private fun pantallaCrearLibro() {
        val titulo = findViewById<TextView>(R.id.tv_formulario_libro)
        titulo.text = "Crear Libro"
        val boton = findViewById<Button>(R.id.btn_formulario_libro)
        boton.text = "Crear"
    }

    private fun pantallaActualizarLibro(id: Int) {
        val titulo = findViewById<TextView>(R.id.tv_formulario_libro)
        titulo.text = "Actualizar Libro"
        val boton = findViewById<Button>(R.id.btn_formulario_libro)
        boton.text = "Guardar"
        val libro = BaseDeDatos.tablas!!.consultarLibroPorID(id)
        val tituloLibro = findViewById<EditText>(R.id.ti_titulo_libro)
        tituloLibro.setText(libro!!.titulo)
        val autor = findViewById<EditText>(R.id.ti_autor_libro)
        autor.setText(libro!!.autor)
        val anio = findViewById<EditText>(R.id.ti_anio_libro)
        anio.setText(libro.anioPublicacion.toString())
        val precio = findViewById<EditText>(R.id.ti_precio_libro)
        precio.setText(libro.precio.toString())
        val disponible = findViewById<EditText>(R.id.ti_disponible_libro)
        val respuesta = if (libro!!.disponible) "si" else "no"
        disponible.setText(respuesta)
    }
}
