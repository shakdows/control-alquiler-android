package com.example.controlalquiler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PisosAdapter(
    private val items: List<Piso>,
    private val onOpen: (Piso) -> Unit,
    private val onEdit: (Piso) -> Unit,
    private val onDelete: (Piso) -> Unit
) : RecyclerView.Adapter<PisosAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val nombre: TextView = v.findViewById(R.id.tvPisoNombre)
        val btnEdit: ImageView = v.findViewById(R.id.btnEditPiso)
        val btnDelete: ImageView = v.findViewById(R.id.btnDeletePiso)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_piso, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, position: Int) {
        val p = items[position]

        h.nombre.text = p.nombre.ifBlank { "Piso" }

        h.itemView.setOnClickListener { onOpen(p) }
        h.btnEdit.setOnClickListener { onEdit(p) }
        h.btnDelete.setOnClickListener { onDelete(p) }
    }

    override fun getItemCount(): Int = items.size
}
