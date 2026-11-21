# Catálogo de Videojuegos

Programa de consola en Kotlin para gestionar un catálogo de videojuegos conectado a una base de datos relacional en MySQL. Permite realizar operaciones CRUD sobre videojuegos, gestionar jugadores y crear listas personalizadas de juegos por usuario utilizando el patrón DAO, consultas parametrizadas y transacciones.

## 1. Estructura de datos y Base de Datos

### Data Classes:
Cada tabla de la base de datos tiene su propia `data class` en Kotlin para mover los datos entre la aplicación y MySQL:

```kotlin
data class Videojuego(
    val id: Int? = null,
    val titulo: String,
    val genero: String,
    val anio: Int,
    val nota: Double
)

data class Jugador(
    val idJugador: Int? = null,
    val nombre: String,
    val nickname: String,
    val email: String
)

data class Lista(
    val idLista: Int? = null,
    val nombre: String,
    val idJugador: Int
)

data class ListaVideojuego(
    val idLista: Int,
    val idVideojuego: Int,
    val fechaAnadido: String
)
```

### Tablas de la Base de Datos (`videojuegos`):
El esquema relacional consta de cuatro tablas conectadas mediante claves primarias (PK) y claves foráneas (FK):

| Tabla | Columnas | Claves y Restricciones |
| :--- | :--- | :--- |
| **`videojuegos`** | `id`, `titulo`, `genero`, `anio`, `nota` | `id` es `PK` y `AUTO_INCREMENT` |
| **`jugadores`** | `id_jugador`, `nombre`, `nickname`, `email` | `id_jugador` es `PK` (`AUTO_INCREMENT`), `nickname` es `UNIQUE` |
| **`listas`** | `id_lista`, `nombre`, `id_jugador` | `id_lista` es `PK` (`AUTO_INCREMENT`), `id_jugador` es `FK` hacia `jugadores(id_jugador)` con `ON DELETE CASCADE` |
| **`listas_videojuegos`** | `id_lista`, `id_videojuego`, `fecha_añadido` | Clave primaria compuesta `(id_lista, id_videojuego)`, ambas son `FK` hacia `listas` y `videojuegos` con `ON DELETE CASCADE` |

Ejemplo de inserción inicial en `datos/videojuegos.sql`:
```sql
INSERT INTO videojuegos (titulo, genero, anio, nota) VALUES
('The Legend of Zelda: Breath of the Wild', 'Aventura', 2017, 9.5);
```

### Carpetas y ficheros:
```text
ud03/
├── datos/
│   └── videojuegos.sql          <- script con los CREATE TABLE e INSERT iniciales
├── src/main/kotlin/
│   ├── ConexionBD.kt            <- configuración y apertura de conexión JDBC con MySQL
│   ├── Videojuego.kt            <- modelo de datos de videojuegos
│   ├── Jugador.kt               <- modelo de datos de jugadores
│   ├── Lista.kt                 <- modelo de datos de listas
│   ├── ListaVideojuego.kt       <- modelo de datos de la relación listas-videojuegos
│   ├── VideojuegoDAO.kt         <- CRUD completo de videojuegos
│   ├── JugadorDAO.kt            <- inserción y borrado de jugadores
│   ├── ListaDAO.kt              <- inserción (validando jugador) y borrado de listas
│   ├── ListaVideojuegoDAO.kt    <- inserción con transacción y consulta JOIN de listas
│   └── Main.kt                  <- menú principal por consola y validación de entradas
├── build.gradle.kts             <- dependencias del proyecto (conector MySQL)
└── LEEME.md                     <- documentación del proyecto
```

## 2. Instrucciones de ejecución

- **Requisitos previos:** JDK 17 o superior instalado y un servidor MySQL activo en `localhost:3306`.
- **Base de datos inicial:** ejecuta el script `datos/videojuegos.sql` en tu servidor MySQL. Este script crea la base de datos `videojuegos`, las cuatro tablas y carga los datos iniciales (5 videojuegos, 3 jugadores y 3 listas asignadas a cada jugador).
- **Configuración de la conexión:** revisa las constantes en `ConexionBD.kt` (`HOST`, `PORT`, `DATABASE`, `USER` y `PASSWORD`) para asegurarte de que coinciden con las credenciales de tu servidor MySQL.
- **Compilación:** abre la carpeta `ud03` en IntelliJ IDEA y deja que Gradle sincronice las dependencias de `build.gradle.kts` (`mysql-connector-j`).
- **Ejecución:** ejecuta la función `main` de `Main.kt`. Se muestra este menú:

```text
===== CATÁLOGO DE VIDEOJUEGOS =====
--- VIDEOJUEGOS ---
1. Ver todos los videojuegos
2. Buscar videojuego por ID
3. Añadir videojuego
4. Modificar videojuego
5. Eliminar videojuego
--- JUGADORES ---
6. Crear jugador
7. Eliminar jugador
--- LISTAS ---
8. Crear lista
9. Eliminar lista
10. Añadir videojuego a una lista
11. Ver videojuegos de una lista
0. Salir
====================================
Escoge una opción:
```

- **Bloque Videojuegos (1 a 5):** lista todo el catálogo, busca un juego por su ID, inserta uno nuevo, modifica todos sus datos por ID o lo elimina.
- **Bloque Jugadores (6 y 7):** crea un jugador nuevo o elimina uno existente por su ID (borrando también sus listas asociadas).
- **Bloque Listas (8 a 11):** crea una lista asociada a un jugador existente, elimina una lista por ID, añade un videojuego a una lista dentro de una transacción y muestra todos los videojuegos de una lista concreta.

### Organización del código
- `Main.kt`: interfaz de consola con el menú de tres bloques y las funciones auxiliares que validan que los números y textos introducidos sean correctos.
- `ConexionBD.kt`: objeto Singleton que gestiona la URL de conexión y devuelve objetos `Connection` mediante `DriverManager`.
- `Videojuego.kt`, `Jugador.kt`, `Lista.kt`, `ListaVideojuego.kt`: las cuatro `data class` que representan las tablas.
- `VideojuegoDAO.kt`: operaciones `SELECT`, `INSERT`, `UPDATE` y `DELETE` sobre la tabla `videojuegos`.
- `JugadorDAO.kt`: operaciones `INSERT` y `DELETE` sobre la tabla `jugadores`.
- `ListaDAO.kt`: comprueba que el jugador exista antes de hacer el `INSERT` en `listas`, y permite eliminar listas por ID.
- `ListaVideojuegoDAO.kt`: contiene la función transaccional `anyadirALista` y la consulta `obtenerVideojuegosDeLista` con `INNER JOIN`.

## 3. Decisiones de diseño

- Elegí **MySQL** como SGBD cliente-servidor para la versión final tras desarrollar las primeras prácticas en SQLite. Gracias al patrón DAO y a centralizar la conexión en `ConexionBD.kt`, la migración se hizo cambiando únicamente la URL y las credenciales sin tocar las consultas SQL.
- Puse el campo `id` de las `data class` (`id`, `idJugador`, `idLista`) como nulable (`Int? = null`) porque al crear un objeto nuevo en Kotlin todavía no tiene identificador; es MySQL quien lo genera automáticamente mediante `AUTO_INCREMENT`.
- Para la tabla `jugadores` diseñé los campos `id_jugador` (clave primaria), `nombre`, `nickname` (con restricción `UNIQUE` para que no haya dos usuarios con el mismo alias) y `email`.
- Configuré las claves foráneas con **`ON DELETE CASCADE`** en `listas` y `listas_videojuegos`. Así, cuando se elimina un jugador desde la opción 7 o una lista desde la opción 9, MySQL borra automáticamente los registros dependientes sin provocar errores de integridad referencial ni dejar datos huérfanos.
- En la opción 8 (*Crear lista*), `ListaDAO` ejecuta primero un `SELECT` para comprobar que el `id_jugador` existe en la base de datos; si no existe, avisa por consola y cancela la creación.
- En la opción 10 (*Añadir videojuego a una lista*), desactivé el guardado automático con `conn.autoCommit = false` para agrupar la comprobación de existencia del videojuego y la inserción en `listas_videojuegos` dentro de una única transacción. Si cualquiera de los dos pasos falla (por ejemplo, si el videojuego o la lista no existen, o si el juego ya estaba en esa lista), se ejecuta `conn.rollback()` y la base de datos queda intacta.
- Para mostrar los videojuegos de una lista (opción 11) utilicé un `INNER JOIN` entre `videojuegos` y `listas_videojuegos` filtrando por `id_lista`, obteniendo directamente todos los datos de cada videojuego en una sola consulta.
- Todas las consultas SQL usan `PreparedStatement` con interrogaciones (`?`) para evitar problemas con las comillas en los textos e impedir inyecciones SQL, y bloques `.use { ... }` para cerrar siempre conexiones y sentencias automáticamente.
- Al pedir datos por consola uso `toIntOrNull()` y `toDoubleOrNull()` dentro de bucles `while` (aceptando tanto coma como punto en los decimales) para que el programa nunca rompa con una excepción si el usuario escribe texto por error.