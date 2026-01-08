package com.example.controlalquiler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CasasAdapter(
    private val items: List<Casa>,
    private val onOpen: (Casa) -> Unit,
    private val onEdit: (Casa) -> Unit,
    private val onDelete: (Casa) -> Unit
) : RecyclerView.Adapter<CasasAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val nombre: TextView = v.findViewById(R.id.tvNombreCasa)
        val btnEdit: ImageButton = v.findViewById(R.id.btnEditCasa)
        val btnDelete: ImageButton = v.findViewById(R.id.btnDeleteCasa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_casa, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, position: Int) {
        val casa = items[position]
        h.nombre.text = casa.nombre.ifBlank { "Casa" }

        h.itemView.setOnClickListener { onOpen(casa) }
        h.btnEdit.setOnClickListener { onEdit(casa) }
        h.btnDelete.setOnClickListener { onDelete(casa) }
    }

    override fun getItemCount(): Int = items.size
}
