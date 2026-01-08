package com.example.controlalquiler

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class PisoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_piso)

        val inPiso = findViewById<EditText>(R.id.inPiso)
        val btn = findViewById<Button>(R.id.btnPiso)

        val casas = Storage.getCasas(this)

        // FIX: esto viene nullable, lo convertimos a String seguro
        val casaId = Storage.getCurrentCasaId(this) ?: ""
        if (casaId.isBlank()) return

        val casa = Storage.findCasa(casas, casaId) ?: return

        // Si viene PISO_ID es edición; si no, es nuevo
        val pisoIdEdit = intent.getStringExtra("PISO_ID")
        val pisoEdit = pisoIdEdit?.let { id -> casa.pisos.find { it.id == id } }

        // Cargar datos si es edición
        pisoEdit?.let { p ->
            inPiso.setText(p.nombre)
        }

        btn.setOnClickListener {
            val nombre = inPiso.text.toString().trim()
            if (nombre.isEmpty()) return@setOnClickListener

            if (pisoEdit != null) {
                // EDITAR
                pisoEdit.nombre = nombre
            } else {
                // CREAR
                casa.pisos.add(
                    Piso(
                        id = Storage.newId(),
                        nombre = nombre,
                        habitaciones = mutableListOf()
                    )
                )
            }

            Storage.saveCasas(this, casas)
            finish()
        }
    }
}
