package org.example

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    println("--- LISTAR TODOS LOS VIDEOJUEGOS ---")
    var catalogo = VideojuegoDAO.listarVideojuegos()
    catalogo.forEach { println(it) }
    println()

    println("--- CONSULTAR VIDEOJUEGO POR ID ---")
    val juegoObenido = VideojuegoDAO.consultarVideojuegoporID(1)
    if (juegoObenido != null) {
        println("Videojuego consultado: $juegoObenido")
    } else {
        println("No hay ningun videojuego con ID 1")
    }
    println()

    println("--- INSERTAR VIDEOJUEGO NUEVO ---")
    val nuevoJuego = Videojuego(
        titulo = "Elden Ring",
        genero = "Souls-like",
        anio = 2022,
        nota = 9.6
    )
    if (VideojuegoDAO.insertarVideojuego(nuevoJuego)) {
        println("El Videojuego ${nuevoJuego.titulo} se ha integrado correctamente")
    } else {
        println("Ha habido un error al insertar el videojuego")
    }
    println()

    println("--- ACTUALIZAR NOTA DEL VIDEOJUEGO ---")
    val juegoActualizar = VideojuegoDAO.consultarVideojuegoporID(5)
    if (juegoActualizar != null) {
        val juegoModificado = juegoActualizar.copy(nota = 9.4)
        if (VideojuegoDAO.actualizaVideojuego(juegoModificado)) {
            println("La nota del juego: $juegoModificado se ha insertado correctamente")
        } else {
            println("No se ha podido actualizar la nota del videojuego")
        }
    }
    println()

    println("--- ELIMINAR VIDEOJUEGO POR ID ---")
    val juegoEliminar = 3
    if (VideojuegoDAO.eliminarVideojuego(juegoEliminar)) {
        println("Videojuego con ID $juegoEliminar se ha eliminado correctamente")
    } else {
        println("No se ha podido eliminar le videojuego con ID $juegoEliminar")
    }
    println()

    println("--- ESTADO FINAL DEL CATALOGO ---")
    catalogo = VideojuegoDAO.listarVideojuegos()
    catalogo.forEach { println(it) }

}