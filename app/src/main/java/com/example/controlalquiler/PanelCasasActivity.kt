package com.example.controlalquiler

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PanelCasasActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var btn: Button
    private var casas: MutableList<Casa> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel_casas)

        // Si tienes un título en tu XML, déjalo así (si no existe no pasa nada)
        runCatching {
            findViewById<TextView>(R.id.tvTitulo).text = "Mis Casas"
        }

        rv = findViewById(R.id.rvCasas)
        btn = findViewById(R.id.btnAgregarCasa) // <-- ESTE es el ID correcto

        rv.layoutManager = LinearLayoutManager(this)

        btn.setOnClickListener {
            startActivity(Intent(this, CasaActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        cargar()
    }

    private fun cargar() {
        casas = Storage.getCasas(this).toMutableList()

        val adapter = CasasAdapter(
            items = casas,
            onOpen = { casa ->
                Storage.setCurrentCasaId(this, casa.id)
                startActivity(Intent(this, PanelPisosActivity::class.java))
            },
            onEdit = { casa ->
                val i = Intent(this, CasaActivity::class.java)
                i.putExtra("CASA_ID", casa.id)
                startActivity(i)
            },
            onDelete = { casa ->
                casas.removeAll { it.id == casa.id }
                Storage.saveCasas(this, casas)
                cargar()
            }
        )

        rv.adapter = adapter
    }
}
