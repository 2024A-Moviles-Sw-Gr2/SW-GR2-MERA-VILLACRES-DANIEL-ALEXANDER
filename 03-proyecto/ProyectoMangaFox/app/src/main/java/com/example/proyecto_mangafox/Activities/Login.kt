package com.example.proyecto_mangafox.Activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.proyecto_mangafox.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Login : AppCompatActivity() {

    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val botonLogin = findViewById<Button>(R.id.btn_login)
        val username = findViewById<TextInputEditText>(R.id.et_usuario_login)
        val password = findViewById<TextInputEditText>(R.id.et_contrasena_login)

        botonLogin.setOnClickListener {
            if (comprobarCamposVacios(username.text.toString(), password.text.toString())) {
                Toast.makeText(this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            autenticarUsuario(username.text.toString(), password.text.toString()) { existe ->
                if (existe) {
                    irActividad(username.text.toString(), MainActivity::class.java) // Reemplaza `MainActivity` con la actividad correcta
                } else {
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun irActividad(username: String, clase: Class<*>) {
        val intent = Intent(this, clase)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    fun comprobarCamposVacios(username: String, password: String): Boolean {
        return username.isEmpty() || password.isEmpty()
    }

    fun autenticarUsuario(username: String, password: String, callback: (Boolean) -> Unit) {
        db.collection("Usuario")
            .document(username)
            .get()
            .addOnSuccessListener { result ->
                val existe = result.get("contrasenia") == password
                Log.d("Login", "${result.id} => ${result.data}")
                callback(existe)
            }
            .addOnFailureListener { exception ->
                Log.d("Login", "Error al autenticar usuario: ", exception)
                callback(false)
            }
    }

    fun btn_registrarse(view: View) {
        val intent = Intent(this, Registrarse::class.java)
        startActivity(intent)
    }
}