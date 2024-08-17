package com.example.proyecto_mangafox.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.example.proyecto_mangafox.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CuentaFragment : Fragment() {

    val db = Firebase.firestore
    var currentUserId = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cuenta, container, false)

        currentUserId = arguments?.getString("username") ?: "Mataso97"
        val saludo = view.findViewById<TextView>(R.id.tv_nombre_cuenta)

        val botonSalir = view.findViewById<FrameLayout>(R.id.fl_salir)

        db.collection("Usuario")
            .document(currentUserId)
            .get()
            .addOnSuccessListener { result ->
                val nombre = result.getString("nombre")
                saludo.text = "Hola, ${nombre}"
            }

        botonSalir.setOnClickListener {
            cerrarSesion()
        }

        return view
    }

    private fun cerrarSesion() {
        val intent = Intent(activity, Login::class.java)
        startActivity(intent)
        activity?.finish()
    }


}