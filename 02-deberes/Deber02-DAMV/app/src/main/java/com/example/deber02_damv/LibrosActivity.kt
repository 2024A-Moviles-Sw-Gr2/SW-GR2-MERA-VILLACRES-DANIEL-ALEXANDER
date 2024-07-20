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
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.deber02_damv.SQLite.BaseDeDatos
import com.example.deber02_damv.SQLite.SQLiteHelper
import com.google.android.material.snackbar.Snackbar

class LibrosActivity : AppCompatActivity() {

    private lateinit var adaptador: ArrayAdapter<Libro>
    private val libros: MutableList<Libro> = mutableListOf()
    private var biblioteca: String = ""

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
                        actualizarListaLibros()
                        mostrarSnackbar("Libros actualizados")
                    }else{
                        mostrarSnackbar("Libros NO actualizados")
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_libros)

        BaseDeDatos.tablas = SQLiteHelper(this)

        biblioteca = intent.getStringExtra("biblioteca") ?: ""

        val botonCrearLibro = findViewById<Button>(R.id.btn_crear_libros)
        botonCrearLibro.setOnClickListener {
            val intentExplicito = Intent(this, FormularioLibros::class.java)
            intentExplicito.putExtra("operacion", "crear")
            intentExplicito.putExtra("id", -1)
            intentExplicito.putExtra("biblioteca", biblioteca)
            callbackFormulario.launch(intentExplicito)
        }

        // Manejo List view
        val listView = findViewById<ListView>(R.id.lv_libros)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            libros)
        listView.adapter = adaptador
        registerForContextMenu(listView)

        // Cargar datos iniciales
        actualizarListaLibros()
    }

    override fun onResume() {
        super.onResume()
        actualizarListaLibros()
    }

    private fun actualizarListaLibros() {
        val nuevasBibliotecas = BaseDeDatos.tablas?.consultarListaLibro(biblioteca) ?: emptyList()
        libros.clear()
        libros.addAll(nuevasBibliotecas)
        adaptador.notifyDataSetChanged()
    }

    private var posicionItemSeleccionado = -1

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_libros, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionItemSeleccionado = info.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val nombreLibroSeleccionada = adaptador.getItem(posicionItemSeleccionado)!!.titulo
        return when (item.itemId) {
            R.id.mi_editar_libro -> {
                val id = BaseDeDatos.tablas!!.obtenerIDLibro(nombreLibroSeleccionada)
                val intentExplicito = Intent(this, FormularioLibros::class.java)
                intentExplicito.putExtra("operacion", "actualizar")
                intentExplicito.putExtra("id", id)
                intentExplicito.putExtra("biblioteca", biblioteca)
                callbackFormulario.launch(intentExplicito)
                true
            }
            R.id.mi_eliminar_libro -> {
                val id = BaseDeDatos.tablas!!.obtenerIDLibro(nombreLibroSeleccionada)
                if (id != null) {
                    BaseDeDatos.tablas!!.eliminarLibro(id)
                    mostrarSnackbar("Se eliminó el libro: $nombreLibroSeleccionada")
                    actualizarListaLibros()
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.cl_libros),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }
}
