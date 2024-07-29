package com.example.proyecto_mangafox.Activities.RecyclerViews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_mangafox.R

class RVAdapterInfoManga(
    private val context: Context,
    private val contenido: List<Pair<Int, List<String>>>
) : RecyclerView.Adapter<RVAdapterInfoManga.SectionViewHolder>() {

    inner class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreCapitulo: TextView = view.findViewById(R.id.tv_nombre_capitulo)
        val fecha: TextView = view.findViewById(R.id.tv_fecha_capitulo)
        val numPaginas: TextView = view.findViewById(R.id.tv_num_paginas_itmbiblioteca)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_info_manga, parent, false)
        return SectionViewHolder(itemView)
    }

    override fun getItemCount(): Int = contenido.size

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val (num, textos) = contenido[position]
        holder.nombreCapitulo.text = textos[0]
        holder.fecha.text = textos[1]
        holder.numPaginas.text = textos[2]
    }
}