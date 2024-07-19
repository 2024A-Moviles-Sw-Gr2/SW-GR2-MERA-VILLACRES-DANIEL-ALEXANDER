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

class MainActivity : AppCompatActivity() {

    val arreglo = BaseDeDatos.tablaBiblioteca!!.consultarListaBiblioteca()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonCrearBiblioteca = findViewById<Button>(R.id.btn_crear_biblioteca)
        botonCrearBiblioteca.setOnClickListener {
                irActividad(FormularioBiblioteca("crear", -1)::class.java)
            }

        // Manejo List view
        val listView = findViewById<ListView>(R.id.lv_bibliotecas)
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
        inflater.inflate(R.menu.menu_bibliotecas, menu)
        // Obtener id
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }
    override fun onContextItemSelected(
        item: MenuItem
    ): Boolean {
        val listView = findViewById<ListView>(R.id.lv_bibliotecas)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arreglo
        )
        val nombreBibliotecaSeleccionada = adaptador.getItem(posicionItemSeleccionado)!!.nombre
        return when (item.itemId){
            R.id.mi_editar_biblioteca -> {
                val id = BaseDeDatos.tablaBiblioteca!!.obtenerIDBiblioteca(nombreBibliotecaSeleccionada)
                if(id != null)irActividad(FormularioBiblioteca("actualizar", id)::class.java)
                return true
            }
            R.id.mi_eliminar_biblioteca -> {
                val id = BaseDeDatos.tablaBiblioteca!!.obtenerIDBiblioteca(nombreBibliotecaSeleccionada)
                if (id != null) {
                    BaseDeDatos.tablaBiblioteca!!.eliminarBiblioteca(id)
                    mostrarSnackbar("Se eliminó la biblioteca: $nombreBibliotecaSeleccionada")
                }
                return true
            }
            R.id.mi_ver_biblioteca -> {
                irActividad(LibrosActivity(nombreBibliotecaSeleccionada)::class.java)
                return true
            }
            else -> super.onContextItemSelected(item)
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

    fun irActividad(
        clase: Class<*>
    ){
        val intent = Intent(this,clase)
        startActivity(intent)
    }
}