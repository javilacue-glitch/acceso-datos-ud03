package org.example

import java.sql.SQLException

object JugadorDAO {

    fun insertarJugador(jugador: Jugador): Boolean {
        val sql = "INSERT INTO jugadores (nombre, nickname, email) VALUES (?, ?, ?)"
        return try {
            ConexionBD.getConnection()?.use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1, jugador.nombre)
                    pstmt.setString(2, jugador.nickname)
                    pstmt.setString(3, jugador.email)
                    pstmt.executeUpdate() > 0
                }
            } ?: false
        } catch (e: SQLException) {
            println("Error al crear el jugador (puede que el nickname ya exista): ${e.message}")
            false
        }
    }

    fun eliminarJugador(idJugador: Int): Boolean {
        val sql = "DELETE FROM jugadores WHERE id_jugador = ?"
        return try {
            ConexionBD.getConnection()?.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setInt(1, idJugador)
                    stmt.executeUpdate() > 0
                }
            } ?: false
        } catch (e: SQLException) {
            println("Error al eliminar el jugador: ${e.message}")
            false
        }
    }
}