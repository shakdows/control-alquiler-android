package com.example.controlalquiler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MesCobroAdapter(
    private val hab: Habitacion,
    private val meses: MutableList<MesCobro>,
    private val onCambio: () -> Unit
) : RecyclerView.Adapter<MesCobroAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvMes: TextView = v.findViewById(R.id.tvMes)
        val tvEstado: TextView = v.findViewById(R.id.tvEstado)
        val inPago: EditText = v.findViewById(R.id.inPago)
        val btnGuardar: Button = v.findViewById(R.id.btnGuardarPago)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mes, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, position: Int) {
        val mes = meses[position]

        h.tvMes.text = mes.mes
        h.tvEstado.text = "Pendiente: S/ ${"%.2f".format(mes.pendiente)}"
        h.inPago.setText("")

        h.btnGuardar.setOnClickListener {
            val pagoTxt = h.inPago.text.toString().trim()
            if (pagoTxt.isEmpty()) return@setOnClickListener

            val pago = pagoTxt.toDoubleOrNull() ?: return@setOnClickListener

            // OPCIÓN B: NO permitir pagos mayores al monto mensual
            val montoMax = hab.montoMensual
            val pagoFinal = pago.coerceAtMost(montoMax)

            mes.pagado += pagoFinal
            mes.pendiente = (hab.montoMensual - mes.pagado).coerceAtLeast(0.0)

            onCambio()
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = meses.size
}
