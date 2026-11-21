package org.example

import java.sql.SQLException

object ListaVideojuegoDAO {

    // Inserta un videojuego en una lista en una misma linea
    fun anyadirALista(idLista: Int, idVideojuego: Int, fecha: String): Boolean {
        val checkVideojuegoSql = "SELECT id FROM videojuegos WHERE id = ?"
        val insertSql = "INSERT INTO listas_videojuegos (id_lista, id_videojuego, fecha_añadido) VALUES (?, ?, ?)"

        return try {
            ConexionBD.getConnection()?.use { conn ->
                try {
                    conn.autoCommit = false

                    conn.prepareStatement(checkVideojuegoSql).use { checkStmt ->
                        checkStmt.setInt(1, idVideojuego)
                        val rs = checkStmt.executeQuery()
                        if (!rs.next()) {
                            println("Error: El videojuego con ID $idVideojuego no existe.")
                            conn.rollback()
                            return false
                        }
                    }

                    conn.prepareStatement(insertSql).use { insertStmt ->
                        insertStmt.setInt(1, idLista)
                        insertStmt.setInt(2, idVideojuego)
                        insertStmt.setString(3, fecha)

                        if (insertStmt.executeUpdate() > 0) {
                            conn.commit()
                            true
                        } else {
                            conn.rollback()
                            false
                        }
                    }
                } catch (e: Exception) {
                    println("Fallo en la transacción (haciendo rollback): ${e.message}")
                    conn.rollback()
                    false
                } finally {
                    conn.autoCommit = true
                }
            } ?: false
        } catch (e: SQLException) {
            println("Error de conexión: ${e.message}")
            false
        }
    }

    //Devuelve todos los videojuegos que pertenecen a una lista con INNER JOIN.
    fun obtenerVideojuegosDeLista(idLista: Int): List<Videojuego> {
        val videojuegos = mutableListOf<Videojuego>()
        val sql = """
            SELECT v.id, v.titulo, v.genero, v.anio, v.nota
            FROM videojuegos v
            INNER JOIN listas_videojuegos lv ON v.id = lv.id_videojuego
            WHERE lv.id_lista = ?
        """.trimIndent()

        try {
            ConexionBD.getConnection()?.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setInt(1, idLista)
                    val rs = stmt.executeQuery()
                    while (rs.next()) {
                        videojuegos.add(
                            Videojuego(
                                id = rs.getInt("id"),
                                titulo = rs.getString("titulo"),
                                genero = rs.getString("genero"),
                                anio = rs.getInt("anio"),
                                nota = rs.getDouble("nota")
                            )
                        )
                    }
                }
            }
        } catch (e: SQLException) {
            println("Error al obtener los videojuegos de la lista: ${e.message}")
        }
        return videojuegos
    }
}