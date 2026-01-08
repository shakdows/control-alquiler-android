package com.example.controlalquiler

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

object Storage {

    private const val PREF = "control_alquiler"
    private const val KEY_CASAS = "casas"

    private const val KEY_CASA = "current_casa"
    private const val KEY_PISO = "current_piso"
    private const val KEY_HAB = "current_habitacion"

    private val gson = Gson()

    // ========= CASAS =========
    fun getCasas(ctx: Context): MutableList<Casa> {
        val sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val json = sp.getString(KEY_CASAS, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Casa>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveCasas(ctx: Context, casas: List<Casa>) {
        val sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        sp.edit().putString(KEY_CASAS, gson.toJson(casas)).apply()
    }

    // ========= HELPERS FIND =========
    fun findCasa(casas: List<Casa>, casaId: String): Casa? =
        casas.firstOrNull { it.id == casaId }

    fun findPiso(casa: Casa, pisoId: String): Piso? =
        casa.pisos.firstOrNull { it.id == pisoId }

    fun findHabitacion(piso: Piso, habitacionId: String): Habitacion? =
        piso.habitaciones.firstOrNull { it.id == habitacionId }

    // ========= IDS =========
    fun newId(): String = UUID.randomUUID().toString()

    // ========= CURRENT IDS =========
    fun setCurrentCasaId(ctx: Context, id: String) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit().putString(KEY_CASA, id).apply()
    }

    fun getCurrentCasaId(ctx: Context): String? =
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_CASA, null)

    fun setCurrentPisoId(ctx: Context, id: String) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit().putString(KEY_PISO, id).apply()
    }

    fun getCurrentPisoId(ctx: Context): String? =
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_PISO, null)

    fun setCurrentHabitacionId(ctx: Context, id: String) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit().putString(KEY_HAB, id).apply()
    }

    fun getCurrentHabitacionId(ctx: Context): String? =
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_HAB, null)

    // ========= MES =========
    fun generarMesSiguiente(hab: Habitacion): MesCobro {
        val ultimo = hab.meses.lastOrNull()?.mes
        val nuevoMes = if (ultimo.isNullOrBlank()) {
            "2026-01"
        } else {
            val p = ultimo.split("-")
            val y = p[0].toInt()
            val m = p[1].toInt() + 1
            if (m > 12) "${y + 1}-01" else "%04d-%02d".format(y, m)
        }

        return MesCobro(
            mes = nuevoMes,
            pagado = 0.0,
            pendiente = hab.montoMensual
        )
    }
}
