package org.example

import java.sql.SQLException

object ListaDAO {

    fun insertarLista(lista: Lista): Boolean {
        val checkJugadorSql = "SELECT id_jugador FROM jugadores WHERE id_jugador = ?"
        val insertSql = "INSERT INTO listas (nombre, id_jugador) VALUES (?, ?)"

        return try {
            ConexionBD.getConnection()?.use { conn ->
                conn.prepareStatement(checkJugadorSql).use { checkStmt ->
                    checkStmt.setInt(1, lista.idJugador)
                    val rs = checkStmt.executeQuery()
                    if (!rs.next()) {
                        println("Error: No existe ningún jugador con ID ${lista.idJugador}. No se creará la lista.")
                        return false
                    }
                }

                conn.prepareStatement(insertSql).use { insertStmt ->
                    insertStmt.setString(1, lista.nombre)
                    insertStmt.setInt(2, lista.idJugador)
                    insertStmt.executeUpdate() > 0
                }
            } ?: false
        } catch (e: SQLException) {
            println("Error al crear la lista: ${e.message}")
            false
        }
    }

    fun eliminarLista(idLista: Int): Boolean {
        val sql = "DELETE FROM listas WHERE id_lista = ?"
        return try {
            ConexionBD.getConnection()?.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setInt(1, idLista)
                    stmt.executeUpdate() > 0
                }
            } ?: false
        } catch (e: SQLException) {
            println("Error al eliminar la lista: ${e.message}")
            false
        }
    }
}