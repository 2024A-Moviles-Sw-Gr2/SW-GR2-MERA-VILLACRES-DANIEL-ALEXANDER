package com.example.deber02_damv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.deber02_damv.SQLite.BaseDeDatos
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale

class FormularioBiblioteca(val operacion:String, val id:Int) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_biblioteca)

        val botonFormulario = findViewById<Button>(R.id.btn_formulario_biblioteca)

        botonFormulario.setOnClickListener {
            val nombre = findViewById<EditText>(R.id.ti_nombre_biblioteca).text.toString()
            val ubicacion = findViewById<EditText>(R.id.ti_ubicacion_biblioteca).text.toString()
            val fechaStr = findViewById<EditText>(R.id.ti_fecha_biblioteca).text.toString()
            val presupuestoStr = findViewById<EditText>(R.id.ti_presupuesto_biblioteca).text.toString()
            val esPublicaStr = findViewById<EditText>(R.id.ti_esPublica_biblioteca).text.toString()

            // Convertir fechaStr a Date
            val fechaFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fecha = fechaFormat.parse(fechaStr)

            val presupuesto = presupuestoStr.toDouble()

            val esPublica = when (esPublicaStr.trim().lowercase()) {
                "si" -> true
                "no" -> false
                else -> false  // Si la respuesta no es clara, asumir que no es pública
            }

            if(operacion.equals("crear")){
                 val respuesta = BaseDeDatos.tablaBiblioteca!!
                    .crearBiblioteca(
                        nombre,
                        ubicacion,
                        fecha,
                        presupuesto,
                        esPublica
                    )
                if(respuesta) mostrarSnackbar("Biblioteca Creada!")
            } else if(operacion.equals("actualizar")) {
                val respuesta = BaseDeDatos.tablaBiblioteca!!
                    .actualizarBiblioteca(
                        id,
                        nombre,
                        ubicacion,
                        fecha,
                        presupuesto,
                        esPublica
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