package com.example.proyecto_mangafox.Activities.RecyclerViews

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_mangafox.R

class RVAdapterExplorar(
    private val context: Context,
    private val contenido: List<Pair<String, Int>>
) : RecyclerView.Adapter<RVAdapterExplorar.SectionViewHolder>() {

    inner class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreManga: TextView = view.findViewById(R.id.tv_nombre_manga_itmExplorar)
        val portada: ImageView = view.findViewById(R.id.iv_portada_itmExplorar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_explorar, parent, false)
        return SectionViewHolder(itemView)
    }

    override fun getItemCount(): Int = contenido.size

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val (textos, image) = contenido[position]
        holder.nombreManga.text = textos
        holder.portada.setImageResource(image)
    }
}