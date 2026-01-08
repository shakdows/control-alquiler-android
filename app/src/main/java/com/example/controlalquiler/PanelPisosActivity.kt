package com.example.controlalquiler

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PanelPisosActivity : AppCompatActivity() {

    private lateinit var tvTitulo: TextView
    private lateinit var rv: RecyclerView
    private lateinit var btnAgregar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel_pisos)

        tvTitulo = findViewById(R.id.tvTitulo)          // OJO: tu XML usa tvTitulo (no tvTituloPisos)
        rv = findViewById(R.id.rvPisos)
        btnAgregar = findViewById(R.id.btnAgregarPiso)

        rv.layoutManager = LinearLayoutManager(this)

        btnAgregar.setOnClickListener {
            // crear piso
            startActivity(Intent(this, PisoActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        cargar()
    }

    private fun safeName(s: String?): String = s?.takeIf { it.isNotBlank() } ?: ""

    private fun cargar() {
        val casas = Storage.getCasas(this)

        // IMPORTANTE: convertir String? a String
        val casaId = Storage.getCurrentCasaId(this) ?: ""
        if (casaId.isBlank()) return

        val casa = Storage.findCasa(casas, casaId) ?: return

        tvTitulo.text = safeName(casa.nombre).ifBlank { "Casa" }

        val adapter = PisosAdapter(
            items = casa.pisos,
            onOpen = { piso ->
                Storage.setCurrentPisoId(this, piso.id)
                startActivity(Intent(this, PanelHabitacionesActivity::class.java))
            },
            onEdit = { piso ->
                val i = Intent(this, PisoActivity::class.java)
                i.putExtra("PISO_ID", piso.id)
                startActivity(i)
            },
            onDelete = { piso ->
                casa.pisos.removeAll { it.id == piso.id }
                Storage.saveCasas(this, casas)
                cargar()
            }
        )

        rv.adapter = adapter
    }
}
