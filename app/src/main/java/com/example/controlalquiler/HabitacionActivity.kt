package com.example.controlalquiler

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class HabitacionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habitacion)

        val inHab = findViewById<EditText>(R.id.inHabitacion)
        val inInquilino = findViewById<EditText>(R.id.inInquilino)
        val inMonto = findViewById<EditText>(R.id.inMonto)
        val inFecha = findViewById<EditText>(R.id.inFechaPrimerPago)
        val chkActivo = findViewById<CheckBox>(R.id.chkActivo)
        val btnGuardar = findViewById<Button>(R.id.btnHabitacion)

        // ===== CONTEXTO (NO NULL) =====
        val casas = Storage.getCasas(this)
        val casaId = Storage.getCurrentCasaId(this) ?: return
        val pisoId = Storage.getCurrentPisoId(this) ?: return

        val casa = Storage.findCasa(casas, casaId) ?: return
        val piso = Storage.findPiso(casa, pisoId) ?: return

        // ===== EDICIÓN (si viene HAB_ID) =====
        val habIdEdit = intent.getStringExtra("HAB_ID")
        val habEdit = habIdEdit?.let { id -> piso.habitaciones.firstOrNull { it.id == id } }

        habEdit?.let { h ->
            inHab.setText(h.codigo)
            inInquilino.setText(h.inquilino)
            inMonto.setText(if (h.montoMensual == 0.0) "" else h.montoMensual.toString())
            inFecha.setText(h.fechaPrimerPago)
            chkActivo.isChecked = h.activo
        }

        btnGuardar.setOnClickListener {
            val codigo = inHab.text.toString().trim()
            val nombreInq = inInquilino.text.toString().trim()
            val montoTxt = inMonto.text.toString().trim()
            val fecha = inFecha.text.toString().trim()

            if (codigo.isEmpty() || montoTxt.isEmpty()) return@setOnClickListener
            val monto = montoTxt.toDoubleOrNull() ?: return@setOnClickListener

            if (habEdit != null) {
                // ===== ACTUALIZAR =====
                habEdit.codigo = codigo
                habEdit.inquilino = nombreInq
                habEdit.montoMensual = monto
                habEdit.fechaPrimerPago = fecha
                habEdit.activo = chkActivo.isChecked

                // Si no tenía meses creados, crea el primero
                if (habEdit.meses.isEmpty()) {
                    habEdit.meses.add(
                        MesCobro(
                            mes = if (fecha.matches(Regex("\\d{4}-\\d{2}"))) fecha else "2026-01",
                            pagado = 0.0,
                            pendiente = monto
                        )
                    )
                }
            } else {
                // ===== CREAR NUEVA =====
                val nueva = Habitacion(
                    id = Storage.newId(),
                    codigo = codigo,
                    inquilino = nombreInq,
                    montoMensual = monto,
                    fechaPrimerPago = fecha,
                    activo = chkActivo.isChecked
                )

                // Crea el primer mes
                nueva.meses.add(
                    MesCobro(
                        mes = if (fecha.matches(Regex("\\d{4}-\\d{2}"))) fecha else "2026-01",
                        pagado = 0.0,
                        pendiente = monto
                    )
                )

                piso.habitaciones.add(nueva)
            }

            Storage.saveCasas(this, casas)
            finish()
        }
    }
}
