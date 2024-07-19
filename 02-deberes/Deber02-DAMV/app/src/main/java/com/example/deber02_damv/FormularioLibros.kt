package com.example.deber02_damv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.deber02_damv.SQLite.BaseDeDatos
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale

class FormularioLibros(val operacion:String, val id:Int, val biblioteca:String) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_libros)

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
                else -> false  // Si la respuesta no es clara, asumir que no es pública
            }

            if(operacion.equals("crear")){
                val respuesta = BaseDeDatos.tablaLibro!!
                    .crearLibro(
                        titulo,
                        autor,
                        anio,
                        precio,
                        disponible,
                        biblioteca
                    )
                if(respuesta) mostrarSnackbar("Biblioteca Creada!")
            } else if(operacion.equals("actualizar")) {
            val respuesta = BaseDeDatos.tablaLibro!!
                .actualizarLibro(
                    id,
                    titulo,
                    autor,
                    anio,
                    precio,
                    disponible
                )
            if(respuesta) mostrarSnackbar("Biblioteca Creada!")
        }

        }
    }

    fun mostrarSnackbar(texto:String){
        val snack = Snackbar.make(
            findViewById(R.id.cl_main_bibliotecas),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }
}