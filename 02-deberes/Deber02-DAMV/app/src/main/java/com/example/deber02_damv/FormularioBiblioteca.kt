package com.example.deber02_damv

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.deber02_damv.SQLite.BaseDeDatos
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale

class FormularioBiblioteca : AppCompatActivity(){

    private var operacion: String = ""
    private var id: Int = -1
    var longitud = 0.0
    var latitud = 0.0

    val callbackFormulario =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
                result ->
            if(result.resultCode == Activity.RESULT_OK){
                if(result.data != null){
                    val data = result.data
                    val respuesta = data?.getBooleanExtra("respuesta", false)
                    if(respuesta!!){
                        latitud = data!!.getDoubleExtra("latitud", 0.0)
                        longitud = data!!.getDoubleExtra("longitud", 0.0)

                        val lat_position = findViewById<EditText>(R.id.ti_latitud)
                        val long_position = findViewById<EditText>(R.id.ti_longitud)
                        lat_position.setText(latitud.toString())
                        long_position.setText(longitud.toString())
                    }else{
                        mostrarSnackbar("Ubicación NO encontrada")
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_biblioteca)

        operacion = intent.getStringExtra("operacion") ?: ""
        id = intent.getIntExtra("id", -1)

        val botonSeleccionarUbicacion = findViewById<Button>(R.id.btn_buscar_ubicacion)
        botonSeleccionarUbicacion.setOnClickListener {
            val intent = Intent(this, MapSelect::class.java)
            callbackFormulario.launch(intent)
        }

        val botonFormulario = findViewById<Button>(R.id.btn_formulario_biblioteca)
        botonFormulario.setOnClickListener {
            val nombre = findViewById<EditText>(R.id.ti_nombre_biblioteca).text.toString()
            val ubicacion = findViewById<EditText>(R.id.ti_ubicacion_biblioteca).text.toString()
            val fechaStr = findViewById<EditText>(R.id.ti_fecha_biblioteca).text.toString()
            val presupuestoStr = findViewById<EditText>(R.id.ti_presupuesto_biblioteca).text.toString()
            val esPublicaStr = findViewById<EditText>(R.id.ti_esPublica_biblioteca).text.toString()
            val lat_positionStr = findViewById<EditText>(R.id.ti_latitud).text.toString()
            val long_positionStr = findViewById<EditText>(R.id.ti_longitud).text.toString()

            val fechaFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fecha = fechaFormat.parse(fechaStr)
            val presupuesto = presupuestoStr.toDouble()
            val lat_position = lat_positionStr.toDouble()
            val long_position = long_positionStr.toDouble()
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
                    esPublica,
                    lat_position,
                    long_position
                )
            } else {
                BaseDeDatos.tablas!!.actualizarBiblioteca(
                    id,
                    nombre,
                    ubicacion,
                    fecha,
                    presupuesto,
                    esPublica,
                    lat_position,
                    long_position
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

    fun mostrarSnackbar(texto:String){
        val snack = Snackbar.make(
            findViewById(R.id.cl_formulario_biblioteca),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }
}