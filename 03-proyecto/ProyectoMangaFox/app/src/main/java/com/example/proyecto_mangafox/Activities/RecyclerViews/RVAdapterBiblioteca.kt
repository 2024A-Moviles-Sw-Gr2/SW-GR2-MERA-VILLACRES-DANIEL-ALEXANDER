package com.example.proyecto_mangafox.Activities.RecyclerViews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_mangafox.R

class RVAdapterBiblioteca(
    private val itemClickListener: InterfaceOnClick.ItemClickListener,
    val contenido: List<Pair<List<String>, String>>
) : RecyclerView.Adapter<RVAdapterBiblioteca.SectionViewHolder>() {

    inner class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreManga: TextView = view.findViewById(R.id.tv_nombre_manga_itmbiblioteca)
        val portada: ImageView = view.findViewById(R.id.iv_portada_itmbiblioteca)
        val numCapitulos: TextView = view.findViewById(R.id.tv_num_paginas_itmbiblioteca)

        init {
            view.setOnClickListener {
                itemClickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVAdapterBiblioteca.SectionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manga_biblioteca, parent, false)
        return SectionViewHolder(itemView)
    }

    override fun getItemCount(): Int = contenido.size

    override fun onBindViewHolder(holder: RVAdapterBiblioteca.SectionViewHolder, position: Int) {
        val (texto, imageUrl) = contenido[position]
        holder.nombreManga.text = texto[0]
        holder.numCapitulos.text = texto[2]
        // Usa Glide para cargar la imagen en el ImageView
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.portada)
    }

}
