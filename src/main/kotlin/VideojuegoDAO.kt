package org.example

import java.sql.SQLException

object VideojuegoDAO {

    fun listarVideojuegos(): List<Videojuego> {
        val lista = mutableListOf<Videojuego>()
        val sql = "SELECT * FROM videojuegos"
        ConexionBD.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    lista.add(Videojuego(
                        id = rs.getInt("id"),
                        titulo = rs.getString("titulo"),
                        genero = rs.getString("genero"),
                        anio = rs.getInt("anio"),
                        nota = rs.getDouble("nota")
                    ))
                }
            }
        }
        return lista
    }

    fun consultarVideojuegoporID(id: Int): Videojuego? {
        var videojuego: Videojuego? = null
        val sql = "SELECT * FROM videojuegos WHERE id = ?"
        ConexionBD.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    videojuego = Videojuego(
                        id = rs.getInt("id"),
                        titulo = rs.getString("titulo"),
                        genero = rs.getString("genero"),
                        anio = rs.getInt("anio"),
                        nota = rs.getDouble("nota")
                    )
                }
            }
        }
        return videojuego
    }

    fun insertarVideojuego(videojuego: Videojuego): Boolean {
        val sql = "INSERT INTO videojuegos (titulo, genero, anio, nota) VALUES (?, ?, ?, ?)"

        return try {
            ConexionBD.getConnection()?.use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.setString(1, videojuego.titulo)
                    pstmt.setString(2, videojuego.genero)
                    pstmt.setInt(3, videojuego.anio)
                    pstmt.setDouble(4, videojuego.nota)
                    pstmt.executeUpdate() > 0 // Devuelve true si se insertó al menos 1 fila
                }
            } ?: false
        } catch (e: SQLException) {
            println("Error al insertar videojuego: ${e.message}")
            false
        }
    }

    fun actualizaVideojuego(videojuego: Videojuego): Boolean {
        if (videojuego.id == null) {
            println("Error: No se puede actualizar un videojuego sin ID.")
            return false
        }

        val sql = "UPDATE videojuegos SET titulo = ?, genero = ?, anio = ?, nota = ? WHERE id = ?"
        return try {
            ConexionBD.getConnection()?.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, videojuego.titulo)
                    stmt.setString(2, videojuego.genero)
                    stmt.setInt(3, videojuego.anio)
                    stmt.setDouble(4, videojuego.nota)
                    stmt.setInt(5, videojuego.id)

                    val filasAfectadas = stmt.executeUpdate()
                    if (filasAfectadas > 0) {
                        println("Videojuego con ID ${videojuego.id} actualizado.")
                        true
                    } else {
                        println("No se encontró ningún videojuego con ID ${videojuego.id} para actualizar.")
                        false
                    }
                }
            } ?: false
        } catch (e: Exception) {
            println("Error al actualizar el videojuego: ${e.message}")
            false
        }
    }

    fun eliminarVideojuego(id: Int): Boolean {
        val sql = "DELETE FROM videojuegos WHERE id = ?"

        return try {
            ConexionBD.getConnection()?.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setInt(1, id)
                    val filasAfectadas = stmt.executeUpdate()
                    if (filasAfectadas > 0) {
                        println("Videojuego con ID $id eliminado.")
                        true
                    } else {
                        println("No se encontró ningún videojuego con ID $id para eliminar.")
                        false
                    }
                }
            } ?: false
        } catch (e: Exception) {
            println("Error al eliminar el videojuego: ${e.message}")
            false
        }
    }

    fun anyadirALista(idLista: Int, idVideojuego: Int, fecha: String): Boolean {
        val checksql = "SELECT * FROM videojuegos WHERE id = ?"
        val insertsql = "INSERT INTO listas_videojuegos (id_lista, id_videojuego, fecha) VALUES (?, ?, ?)"

        return try{
            ConexionBD.getConnection()?.use { conn ->
                try{
                    conn.autoCommit = false

                    conn.prepareStatement(checksql).use { checkStmt ->
                        checkStmt.setInt(1, idVideojuego)
                        val rs = checkStmt.executeQuery()

                        if (!rs.next()){
                            println("Error: El videojuego con ID $idVideojuego no existe")
                            conn.rollback()
                            return false
                        }
                    }

                    conn.prepareStatement(insertsql).use { insertStmt ->
                        insertStmt.setInt(1, idLista)
                        insertStmt.setInt(2, idVideojuego)
                        insertStmt.setString(3, fecha)

                        val filasModificadas = insertStmt.executeUpdate()

                        if (filasModificadas > 0){
                            conn.commit()
                            println("El videojuego se ha añadadido a la lista con exito")
                            true
                        } else {
                            println("Error: No se ha podido integrar el videojuego en la lista")
                            conn.rollback()
                            false
                        }
                    }
                } catch (e: Exception){
                    println("Excepcion que ha saltado mientras le ejecución: ${e.message}")
                    conn.rollback()
                    false
                } finally {
                    conn.autoCommit = true
                }
            } ?: false
        } catch (e: Exception){
            println("Error de conexion: ${e.message}")
            false
        }
    }
}