package com.example.controlalquiler

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class CasaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_casa)

        val inNombre = findViewById<EditText>(R.id.inNombreCasa)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarCasa)

        val casas = Storage.getCasas(this)
        val casaIdEdit = intent.getStringExtra("CASA_ID")

        val casaEdit = casaIdEdit?.let { id ->
            casas.find { it.id == id }
        }

        casaEdit?.let {
            inNombre.setText(it.nombre)
        }

        btnGuardar.setOnClickListener {
            val nombre = inNombre.text.toString().trim()
            if (nombre.isEmpty()) return@setOnClickListener

            if (casaEdit != null) {
                casaEdit.nombre = nombre
            } else {
                casas.add(
                    Casa(
                        id = Storage.newId(),
                        nombre = nombre
                    )
                )
            }

            Storage.saveCasas(this, casas)
            finish()
        }
    }
}
