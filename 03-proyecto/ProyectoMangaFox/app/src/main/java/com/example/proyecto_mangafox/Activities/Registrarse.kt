package com.example.proyecto_mangafox.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.proyecto_mangafox.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Registrarse : AppCompatActivity() {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

        val botonRegistrarse = findViewById<Button>(R.id.btn_registrarse)
        val botonCancelar = findViewById<Button>(R.id.btn_cancelar_registrarse)
        val username = findViewById<TextInputEditText>(R.id.et_usuario_registrarse)
        val password = findViewById<TextInputEditText>(R.id.et_contrasena_registrarse)
        val nombre = findViewById<TextInputEditText>(R.id.et_nombre_registrarse)
        val correo = findViewById<TextInputEditText>(R.id.et_correo_registrarse)

        botonRegistrarse.setOnClickListener {
            registrarUsuario(username.text.toString(), password.text.toString(), nombre.text.toString(), correo.text.toString())
            irActividad(Login::class.java) }

        botonCancelar.setOnClickListener {
            irActividad(Login::class.java)
        }
    }

    fun registrarUsuario(username: String, password: String, nombre: String, correo: String) {
        // Crear un mapa con los datos del usuario
        val usuario = hashMapOf(
            "contrasenia" to password,
            "email" to correo,
            "nombre" to nombre
        )

        // Ingresar usuario a la base de datos
        db.collection("Usuario")
            .document(username)
            .set(usuario)
            .addOnSuccessListener {
                Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
            }
    }


    fun irActividad(
        clase: Class<*>
    ){
        val intent = Intent(this,clase)
        startActivity(intent)
    }
}
