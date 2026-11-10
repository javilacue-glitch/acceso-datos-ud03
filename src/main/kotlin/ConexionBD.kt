package org.example

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object ConexionBD {
    private const val DB_PATH = "datos/videojuegos.sqlite"
    private val DB_FILE = File(DB_PATH)
    private const val URL = "jdbc:sqlite:$DB_PATH"


    fun getConnection(): Connection? {
        return try {
            DriverManager.getConnection(URL)
        } catch (e: SQLException) {
            println("Error al conectar con la base de datos: ${e.message}")
            null
        }
    }

    fun testConnection(): Boolean {
        return getConnection()?.use { conn ->
            println("Conexión establecida con éxito a ${DB_FILE.absolutePath}")
            true
        } ?: false
    }

    fun closeConnection(conn: Connection?) {
        try {
            conn?.close()
            println("Conexión cerrada correctamente.")
        } catch (e: SQLException) {
            println("Error al cerrar la conexión: ${e.message}")
        }
    }
}