package com.example.deber02_damv

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

class FormularioBiblioteca : AppCompatActivity() {

    private var operacion: String = ""
    private var id: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_biblioteca)

        operacion = intent.getStringExtra("operacion") ?: ""
        id = intent.getIntExtra("id", -1)

        val botonFormulario = findViewById<Button>(R.id.btn_formulario_biblioteca)

        botonFormulario.setOnClickListener {
            val nombre = findViewById<EditText>(R.id.ti_nombre_biblioteca).text.toString()
            val ubicacion = findViewById<EditText>(R.id.ti_ubicacion_biblioteca).text.toString()
            val fechaStr = findViewById<EditText>(R.id.ti_fecha_biblioteca).text.toString()
            val presupuestoStr = findViewById<EditText>(R.id.ti_presupuesto_biblioteca).text.toString()
            val esPublicaStr = findViewById<EditText>(R.id.ti_esPublica_biblioteca).text.toString()

            val fechaFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fecha = fechaFormat.parse(fechaStr)
            val presupuesto = presupuestoStr.toDouble()
            val esPublica = when (esPublicaStr.trim().lowercase()) {
                "si" -> true
                "no" -> false
                else -> false
            }

            val respuesta = if (operacion == "crear") {
                BaseDeDatos.tablas!!.crearBiblioteca(
                    nombre,
                    ubicacion,
                    fecha,
                    presupuesto,
                    esPublica
                )
            } else {
                BaseDeDatos.tablas!!.actualizarBiblioteca(
                    id,
                    nombre,
                    ubicacion,
                    fecha,
                    presupuesto,
                    esPublica
                )
            }
            devolverRespuesta(respuesta)
        }

        if (operacion == "crear") {
            pantallaCrearBiblioteca()
        } else if (operacion == "actualizar") {
            pantallaActualizarBiblioteca(id)
        }
    }

    private fun devolverRespuesta(respuesta: Boolean) {
        val intentDevolverRespuesta = Intent()
        intentDevolverRespuesta.putExtra("respuesta", respuesta)
        setResult(RESULT_OK, intentDevolverRespuesta)
        finish()
    }

    private fun pantallaCrearBiblioteca() {
        val titulo = findViewById<TextView>(R.id.tv_formulario_biblioteca)
        titulo.text = "Crear Biblioteca"
        val boton = findViewById<Button>(R.id.btn_formulario_biblioteca)
        boton.text = "Crear"
    }

    private fun pantallaActualizarBiblioteca(id: Int) {
        val titulo = findViewById<TextView>(R.id.tv_formulario_biblioteca)
        titulo.text = "Actualizar Biblioteca"
        val boton = findViewById<Button>(R.id.btn_formulario_biblioteca)
        boton.text = "Guardar"
        val biblioteca = BaseDeDatos.tablas!!.consultarBibliotecaPorID(id)
        val nombre = findViewById<EditText>(R.id.ti_nombre_biblioteca)
        nombre.setText(biblioteca!!.nombre)
        val ubicacion = findViewById<EditText>(R.id.ti_ubicacion_biblioteca)
        ubicacion.setText(biblioteca!!.ubicacion)
        val fecha = findViewById<EditText>(R.id.ti_fecha_biblioteca)
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
        val fechaFormateada = formatoFecha.format(biblioteca!!.fechaCreacion)
        fecha.setText(fechaFormateada)
        val presupuesto = findViewById<EditText>(R.id.ti_presupuesto_biblioteca)
        presupuesto.setText(biblioteca!!.presupuestoAnual.toString())
        val esPublica = findViewById<EditText>(R.id.ti_esPublica_biblioteca)
        val respuesta = if (biblioteca!!.esPublica) "si" else "no"
        esPublica.setText(respuesta)
    }
}