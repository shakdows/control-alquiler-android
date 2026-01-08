package com.example.controlalquiler

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HabitacionDetalleActivity : AppCompatActivity() {

    private lateinit var casas: MutableList<Casa>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habitacion_detalle)

        casas = Storage.getCasas(this)
        cargarUI()
    }

    private fun getHabitacionActual(): Triple<Casa, Piso, Habitacion>? {
        val casaId = Storage.getCurrentCasaId(this) ?: return null
        val pisoId = Storage.getCurrentPisoId(this) ?: return null
        val habId  = Storage.getCurrentHabitacionId(this) ?: return null

        val casa = casas.find { it.id == casaId } ?: return null
        val piso = casa.pisos.find { it.id == pisoId } ?: return null
        val hab  = piso.habitaciones.find { it.id == habId } ?: return null

        return Triple(casa, piso, hab)
    }

    private fun cargarUI() {
        val ref = getHabitacionActual() ?: return
        val hab = ref.third

        findViewById<TextView>(R.id.tvTitulo).text = "Habitación ${hab.codigo}"
        findViewById<TextView>(R.id.tvInquilino).text =
            "Inquilino: ${hab.inquilino.ifBlank { "Sin asignar" }}"

        findViewById<TextView>(R.id.tvMonto).text =
            "Monto mensual: S/ ${"%.2f".format(hab.montoMensual)}"

        findViewById<TextView>(R.id.tvFecha).text =
            "Fecha pago: ${hab.fechaPrimerPago.ifBlank { "No definido" }}"

        val rv = findViewById<RecyclerView>(R.id.rvMeses)
        rv.layoutManager = LinearLayoutManager(this)

        val adapter = MesCobroAdapter(
            hab = hab,
            meses = hab.meses,
            onCambio = {
                Storage.saveCasas(this, casas)
            }
        )

        rv.adapter = adapter

        findViewById<Button>(R.id.btnGenerarMes).setOnClickListener {
            hab.meses.add(Storage.generarMesSiguiente(hab))
            Storage.saveCasas(this, casas)
            adapter.notifyDataSetChanged()
        }
    }
}
