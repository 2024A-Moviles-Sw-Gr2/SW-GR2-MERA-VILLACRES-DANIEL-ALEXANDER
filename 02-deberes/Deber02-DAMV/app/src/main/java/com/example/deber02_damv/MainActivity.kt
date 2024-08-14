package com.example.deber02_damv

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.deber02_damv.SQLite.BaseDeDatos
import com.example.deber02_damv.SQLite.SQLiteHelper
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var adaptador: ArrayAdapter<Biblioteca>
    private val bibliotecas: MutableList<Biblioteca> = mutableListOf()

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
                        actualizarListaBibliotecas()
                        mostrarSnackbar("Bibliotecas actualizadas")
                    }else{
                        mostrarSnackbar("Bibliotecas NO actualizadas")
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa la base de datos aquí
        BaseDeDatos.tablas = SQLiteHelper(this)


        val botonCrearBiblioteca = findViewById<Button>(R.id.btn_crear_biblioteca)
        botonCrearBiblioteca.setOnClickListener {
            val intentExplicito = Intent(
                this,
                FormularioBiblioteca::class.java
            )
            intentExplicito.putExtra("operacion", "crear")
            intentExplicito.putExtra("id", -1)
            callbackFormulario.launch(intentExplicito)
        }

        val listView = findViewById<ListView>(R.id.lv_bibliotecas)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            bibliotecas
        )
        listView.adapter = adaptador
        registerForContextMenu(listView)

        // Load initial data
        actualizarListaBibliotecas()
    }

    private var posicionItemSeleccionado = -1

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ){
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_bibliotecas, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionItemSeleccionado = info.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val nombreBibliotecaSeleccionada = adaptador.getItem(posicionItemSeleccionado)!!.nombre
        return when (item.itemId) {
            R.id.mi_editar_biblioteca -> {
                val id = BaseDeDatos.tablas!!.obtenerIDBiblioteca(nombreBibliotecaSeleccionada)
                val intentExplicito = Intent(
                    this,
                    FormularioBiblioteca::class.java
                )
                intentExplicito.putExtra("operacion", "actualizar")
                intentExplicito.putExtra("id", id)
                callbackFormulario.launch(intentExplicito)
                true
            }
            R.id.mi_eliminar_biblioteca -> {
                val id = BaseDeDatos.tablas!!.obtenerIDBiblioteca(nombreBibliotecaSeleccionada)
                if (id != null) {
                    BaseDeDatos.tablas!!.eliminarBiblioteca(id)
                    mostrarSnackbar("Se eliminó la biblioteca: $nombreBibliotecaSeleccionada")
                    actualizarListaBibliotecas()
                }
                true
            }
            R.id.mi_ver_biblioteca -> {
                val intentExplicito = Intent(
                    this,
                    LibrosActivity::class.java
                )
                intentExplicito.putExtra("biblioteca", nombreBibliotecaSeleccionada)
                callbackFormulario.launch(intentExplicito)
                true
            }
            R.id.mi_ver_ubicación -> {
                val intentExplicito = Intent(
                    this,
                    MapView::class.java
                )
                val id = BaseDeDatos.tablas!!.obtenerIDBiblioteca(nombreBibliotecaSeleccionada)
                intentExplicito.putExtra("id", id)
                callbackFormulario.launch(intentExplicito)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun actualizarListaBibliotecas() {
        val nuevasBibliotecas = BaseDeDatos.tablas?.consultarListaBiblioteca() ?: emptyList()
        bibliotecas.clear()
        bibliotecas.addAll(nuevasBibliotecas)
        adaptador.notifyDataSetChanged()
    }

    private fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.cl_main_bibliotecas),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }
}
