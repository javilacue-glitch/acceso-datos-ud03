package org.example

import java.time.LocalDate

fun main() {
    var salir = false

    while (!salir) {
        println("\n===== CATÁLOGO DE VIDEOJUEGOS =====")
        println("--- VIDEOJUEGOS ---")
        println("1. Ver todos los videojuegos")
        println("2. Buscar videojuego por ID")
        println("3. Añadir videojuego")
        println("4. Modificar videojuego")
        println("5. Eliminar videojuego")
        println("--- JUGADORES ---")
        println("6. Crear jugador")
        println("7. Eliminar jugador")
        println("--- LISTAS ---")
        println("8. Crear lista")
        println("9. Eliminar lista")
        println("10. Añadir videojuego a una lista")
        println("11. Ver videojuegos de una lista")
        println("0. Salir")
        println("====================================")
        print("Escoge una opción: ")

        when (readln().trim()) {
            "1" -> {
                val lista = VideojuegoDAO.listarVideojuegos()
                if (lista.isEmpty()) println("No hay videojuegos registrados.")
                else lista.forEach { println(it) }
            }

            "2" -> {
                val id = pedirEnteroValido("Introduce el ID del videojuego: ")
                val juego = VideojuegoDAO.consultarVideojuegoPorId(id)
                if (juego != null) println("Encontrado: $juego")
                else println("No existe ningún videojuego con ID $id.")
            }

            "3" -> {
                val titulo = pedirTextoNoVacio("Título: ")
                val genero = pedirTextoNoVacio("Género: ")
                val anio = pedirEnteroValido("Año de lanzamiento: ")
                val nota = pedirDoubleValido("Nota: ")
                if (VideojuegoDAO.insertarVideojuego(Videojuego(titulo = titulo, genero = genero, anio = anio, nota = nota))) {
                    println("Videojuego '$titulo' añadido correctamente.")
                } else {
                    println("No se pudo añadir el videojuego.")
                }
            }

            "4" -> {
                val id = pedirEnteroValido("ID del videojuego a modificar: ")
                val existente = VideojuegoDAO.consultarVideojuegoPorId(id)
                if (existente == null) {
                    println("No existe ningún videojuego con ID $id.")
                } else {
                    println("Actual: $existente")
                    val titulo = pedirTextoNoVacio("Nuevo título: ")
                    val genero = pedirTextoNoVacio("Nuevo género: ")
                    val anio = pedirEnteroValido("Nuevo año: ")
                    val nota = pedirDoubleValido("Nueva nota: ")
                    val actualizado = existente.copy(titulo = titulo, genero = genero, anio = anio, nota = nota)
                    if (VideojuegoDAO.actualizarVideojuego(actualizado)) {
                        println("Videojuego actualizado correctamente.")
                    } else {
                        println("Error al actualizar el videojuego.")
                    }
                }
            }

            "5" -> {
                val id = pedirEnteroValido("ID del videojuego a eliminar: ")
                if (VideojuegoDAO.eliminarVideojuego(id)) {
                    println("Videojuego con ID $id eliminado correctamente.")
                } else {
                    println("No se encontró ningún videojuego con ID $id.")
                }
            }

            "6" -> {
                val nombre = pedirTextoNoVacio("Nombre del jugador: ")
                val nickname = pedirTextoNoVacio("Nickname: ")
                val email = pedirTextoNoVacio("Email: ")
                if (JugadorDAO.insertarJugador(Jugador(nombre = nombre, nickname = nickname, email = email))) {
                    println("Jugador '$nickname' creado correctamente.")
                } else {
                    println("No se pudo crear el jugador.")
                }
            }

            "7" -> {
                val idJugador = pedirEnteroValido("ID del jugador a eliminar: ")
                if (JugadorDAO.eliminarJugador(idJugador)) {
                    println("Jugador con ID $idJugador y sus listas asociadas eliminados correctamente.")
                } else {
                    println("No se encontró ningún jugador con ID $idJugador.")
                }
            }

            "8" -> {
                val nombreLista = pedirTextoNoVacio("Nombre de la nueva lista: ")
                val idJugador = pedirEnteroValido("ID del jugador propietario: ")
                if (ListaDAO.insertarLista(Lista(nombre = nombreLista, idJugador = idJugador))) {
                    println("Lista '$nombreLista' creada correctamente para el jugador $idJugador.")
                }
            }

            "9" -> {
                val idLista = pedirEnteroValido("ID de la lista a eliminar: ")
                if (ListaDAO.eliminarLista(idLista)) {
                    println("Lista con ID $idLista eliminada correctamente.")
                } else {
                    println("No se encontró ninguna lista con ID $idLista.")
                }
            }

            "10" -> {
                val idLista = pedirEnteroValido("ID de la lista: ")
                val idVideojuego = pedirEnteroValido("ID del videojuego a añadir: ")
                val fechaHoy = LocalDate.now().toString()
                if (ListaVideojuegoDAO.anyadirALista(idLista, idVideojuego, fechaHoy)) {
                    println("Videojuego $idVideojuego añadido a la lista $idLista (fecha: $fechaHoy).")
                } else {
                    println("No se pudo añadir el videojuego a la lista (transacción revertida).")
                }
            }

            "11" -> {
                val idLista = pedirEnteroValido("ID de la lista a consultar: ")
                val juegos = ListaVideojuegoDAO.obtenerVideojuegosDeLista(idLista)
                if (juegos.isEmpty()) {
                    println("La lista $idLista no existe o no tiene videojuegos asignados.")
                } else {
                    println("--- Videojuegos en la lista $idLista ---")
                    juegos.forEach { println(it) }
                }
            }

            "0" -> {
                println("Saliendo del catálogo...")
                salir = true
            }

            else -> println("Opción no válida. Escoge un número del 0 al 11.")
        }
    }
}

//Funciones auxiliares para validacion

private fun pedirEnteroValido(mensaje: String): Int {
    while (true) {
        print(mensaje)
        val valor = readln().trim().toIntOrNull()
        if (valor != null) return valor
        println("Error: Debes introducir un número entero válido.")
    }
}

private fun pedirDoubleValido(mensaje: String): Double {
    while (true) {
        print(mensaje)
        val valor = readln().trim().replace(",", ".").toDoubleOrNull()
        if (valor != null) return valor
        println("Error: Debes introducir un número decimal válido.")
    }
}

private fun pedirTextoNoVacio(mensaje: String): String {
    while (true) {
        print(mensaje)
        val texto = readln().trim()
        if (texto.isNotEmpty()) return texto
        println("Error: Este campo no puede quedar vacío.")
    }
}