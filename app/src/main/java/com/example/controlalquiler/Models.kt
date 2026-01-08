package com.example.controlalquiler

data class Casa(
    val id: String,
    var nombre: String,
    val pisos: MutableList<Piso> = mutableListOf()
)

data class Piso(
    val id: String,
    var nombre: String,
    val habitaciones: MutableList<Habitacion> = mutableListOf()
)

data class Habitacion(
    val id: String,
    var codigo: String,
    var inquilino: String,
    var montoMensual: Double,
    var fechaPrimerPago: String,
    var activo: Boolean = true,
    val meses: MutableList<MesCobro> = mutableListOf()
)

data class MesCobro(
    val mes: String,
    var pagado: Double,
    var pendiente: Double
)
