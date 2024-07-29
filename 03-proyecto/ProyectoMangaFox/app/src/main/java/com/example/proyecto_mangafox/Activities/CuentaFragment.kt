package com.example.proyecto_mangafox.Activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyecto_mangafox.R

class CuentaFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cuenta, container, false)
    }

    fun btn_ajustes(view: View) {}
    fun btn_acerda_de(view: View) {}
    fun btn_ayuda(view: View) {}

}