package org.example

data class Videojuego(
    val id: Int? = null,
    val titulo: String,
    val genero: String,
    val anio: Int,
    val nota: Double
)