package com.example.deber03_damv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
    private val context: Context,
    private val sectionList: List<Pair<String, List<Int>>>
) : RecyclerView.Adapter<RecyclerViewAdapter.SectionViewHolder>() {

    inner class SectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sectionName: TextView = view.findViewById(R.id.tv_catalog_name)
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_movie_catalog)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_movie_catalog, parent, false)
        return SectionViewHolder(itemView)
    }

    override fun getItemCount(): Int = sectionList.size

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val (sectionName, imageList) = sectionList[position]
        holder.sectionName.text = sectionName

        // Configurar el RecyclerView horizontal
        holder.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.recyclerView.adapter = HorizontalRecyclerViewAdapter(imageList)
    }
}

