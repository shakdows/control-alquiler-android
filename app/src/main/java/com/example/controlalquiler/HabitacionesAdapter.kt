package com.example.controlalquiler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HabitacionesAdapter(
    private val items: List<Habitacion>,
    private val onOpen: (Habitacion) -> Unit,
    private val onEdit: (Habitacion) -> Unit,
    private val onDelete: (Habitacion) -> Unit
) : RecyclerView.Adapter<HabitacionesAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvCodigo: TextView = v.findViewById(R.id.tvHabCodigo)
        val tvInq: TextView = v.findViewById(R.id.tvHabInquilino)
        val tvPend: TextView = v.findViewById(R.id.tvHabPendiente)
        val btnEdit: ImageView = v.findViewById(R.id.btnHabEdit)
        val btnDelete: ImageView = v.findViewById(R.id.btnHabDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habitacion_panel, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, position: Int) {
        val hab = items[position]

        val inq = hab.inquilino.ifBlank { "Sin inquilino" }
        val pendienteTotal = hab.meses.sumOf { it.pendiente.coerceAtLeast(0.0) }

        h.tvCodigo.text = "Habitación ${hab.codigo}"
        h.tvInq.text = inq
        h.tvPend.text = "Pendiente total: S/ ${"%.2f".format(pendienteTotal)}"

        h.itemView.setOnClickListener { onOpen(hab) }
        h.btnEdit.setOnClickListener { onEdit(hab) }
        h.btnDelete.setOnClickListener { onDelete(hab) }
    }

    override fun getItemCount(): Int = items.size
}
