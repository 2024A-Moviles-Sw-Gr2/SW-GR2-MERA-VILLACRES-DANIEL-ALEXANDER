package com.example.deber02_damv

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
import com.example.deber02_damv.SQLite.BaseDeDatos
import com.google.android.material.snackbar.Snackbar

class LibrosActivity(val biblioteca:String) : AppCompatActivity() {

    val arreglo = BaseDeDatos.tablaLibro!!.consultarListaLibro()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_libros)

        val botonCrearLibro = findViewById<Button>(R.id.btn_crear_libros)
        botonCrearLibro.setOnClickListener {
            irActividad(FormularioLibros("crear", -1, biblioteca)::class.java)
        }

        // Manejo List view
        val listView = findViewById<ListView>(R.id.lv_libros)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arreglo
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()
        registerForContextMenu(listView)
    }

    var posicionItemSeleccionado = -1
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ){
        super.onCreateContextMenu(menu,v,menuInfo)
        // llenamos opciones del menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_libros, menu)
        // Obtener id
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }
    override fun onContextItemSelected(
        item: MenuItem
    ): Boolean {
        val listView = findViewById<ListView>(R.id.lv_libros)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arreglo
        )
        val nombreLibroSeleccionada = adaptador.getItem(posicionItemSeleccionado)!!.titulo
        return when (item.itemId){
            R.id.mi_editar_libro -> {
                val id = BaseDeDatos.tablaLibro!!.obtenerIDLibro(nombreLibroSeleccionada)
                if (id != null) irActividad(FormularioLibros("actualizar", id, biblioteca)::class.java)
                return true
            }
            R.id.mi_eliminar_libro -> {
                val id = BaseDeDatos.tablaLibro!!.obtenerIDLibro(nombreLibroSeleccionada)
                if (id != null) {
                    BaseDeDatos.tablaLibro!!.eliminarLibro(id)
                    mostrarSnackbar("Se eliminó el libro: $nombreLibroSeleccionada")
                }
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun mostrarSnackbar(texto:String){
        val snack = Snackbar.make(
            findViewById(R.id.cl_libros),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }

    fun irActividad(
        clase: Class<*>
    ){
        val intent = Intent(this,clase)
        startActivity(intent)
    }
}