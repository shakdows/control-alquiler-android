package com.example.controlalquiler

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PanelHabitacionesActivity : AppCompatActivity() {

    private lateinit var tvTitulo: TextView
    private lateinit var rv: RecyclerView
    private lateinit var btnAgregar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel_habitaciones)

        tvTitulo = findViewById(R.id.tvTitulo)
        rv = findViewById(R.id.rvHabitaciones)
        btnAgregar = findViewById(R.id.btnAgregarHabitacion)

        rv.layoutManager = LinearLayoutManager(this)

        btnAgregar.setOnClickListener {
            startActivity(Intent(this, HabitacionActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        cargar()
    }

    private fun safeName(s: String?): String {
        return s?.takeIf { it.isNotBlank() } ?: ""
    }

    private fun cargar() {
        val casas = Storage.getCasas(this)

        val casaId = Storage.getCurrentCasaId(this) ?: ""
        val pisoId = Storage.getCurrentPisoId(this) ?: ""

        if (casaId.isBlank() || pisoId.isBlank()) return

        val casa = Storage.findCasa(casas, casaId) ?: return
        val piso = Storage.findPiso(casa, pisoId) ?: return

        val casaNombre = safeName(casa.nombre)
        val pisoNombre = safeName(piso.nombre)

        tvTitulo.text = if (casaNombre.isNotBlank() && pisoNombre.isNotBlank()) {
            "$casaNombre - $pisoNombre"
        } else if (casaNombre.isNotBlank()) {
            casaNombre
        } else {
            "Habitaciones"
        }

        val adapter = PanelHabitacionesAdapter(
            items = piso.habitaciones,
            onOpen = { hab ->
                Storage.setCurrentHabitacionId(this, hab.id)
                startActivity(Intent(this, HabitacionDetalleActivity::class.java))
            },
            onEdit = { hab ->
                val i = Intent(this, HabitacionActivity::class.java)
                i.putExtra("HAB_ID", hab.id)
                startActivity(i)
            },
            onDelete = { hab ->
                piso.habitaciones.removeAll { it.id == hab.id }
                Storage.saveCasas(this, casas)
                cargar()
            }
        )

        rv.adapter = adapter
    }
}
