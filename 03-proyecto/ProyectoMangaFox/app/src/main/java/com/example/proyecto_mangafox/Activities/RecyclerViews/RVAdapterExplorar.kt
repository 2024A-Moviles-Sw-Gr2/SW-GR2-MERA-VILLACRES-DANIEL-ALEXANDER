package com.example.proyecto_mangafox.Activities.RecyclerViews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto_mangafox.R

class RVAdapterExplorar(
    private val itemClickListener: InterfaceOnClick.ItemClickListener,
    private val contenido: List<Pair<String, Int>>
) : RecyclerView.Adapter<RVAdapterExplorar.SectionViewHolder>() {

    inner class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreManga: TextView = view.findViewById(R.id.tv_nombre_manga_itmExplorar)
        val portada: ImageView = view.findViewById(R.id.iv_portada_itmExplorar)

        init {
            view.setOnClickListener {
                itemClickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manga_explorar, parent, false) // Asegúrate de usar un layout de ítem adecuado
        return SectionViewHolder(itemView)
    }

    override fun getItemCount(): Int = contenido.size

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val (texto, image) = contenido[position]
        holder.nombreManga.text = texto
        holder.portada.setImageResource(image)
    }
}
