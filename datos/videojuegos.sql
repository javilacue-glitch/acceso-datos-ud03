CREATE DATABASE IF NOT EXISTS videojuegos;
USE videojuegos;

DROP TABLE IF EXISTS listas_videojuegos;
DROP TABLE IF EXISTS listas;
DROP TABLE IF EXISTS jugadores;
DROP TABLE IF EXISTS videojuegos;

CREATE TABLE videojuegos (
                             id     INT PRIMARY KEY AUTO_INCREMENT,
                             titulo VARCHAR(100) NOT NULL,
                             genero VARCHAR(50)  NOT NULL,
                             anio   INT          NOT NULL,
                             nota   DOUBLE       NOT NULL
);

CREATE TABLE jugadores (
                           id_jugador INT PRIMARY KEY AUTO_INCREMENT,
                           nombre     VARCHAR(100) NOT NULL,
                           nickname   VARCHAR(50)  NOT NULL UNIQUE,
                           email      VARCHAR(100) NOT NULL
);

CREATE TABLE listas (
                        id_lista   INT PRIMARY KEY AUTO_INCREMENT,
                        nombre     VARCHAR(100) NOT NULL,
                        id_jugador INT          NOT NULL,
                        FOREIGN KEY (id_jugador) REFERENCES jugadores(id_jugador) ON DELETE CASCADE
);

    CREATE TABLE listas_videojuegos (
                                    id_lista      INT         NOT NULL,
                                    id_videojuego INT         NOT NULL,
                                    fecha_añadido VARCHAR(20) NOT NULL,
                                    PRIMARY KEY (id_lista, id_videojuego),
                                    FOREIGN KEY (id_lista) REFERENCES listas(id_lista) ON DELETE CASCADE,
                                    FOREIGN KEY (id_videojuego) REFERENCES videojuegos(id) ON DELETE CASCADE
);

INSERT INTO videojuegos (titulo, genero, anio, nota) VALUES
                                                         ('The Legend of Zelda: Breath of the Wild', 'Aventura', 2017, 9.5),
                                                         ('Red Dead Redemption 2', 'Acción', 2018, 9.7),
                                                         ('Hades', 'Roguelike', 2020, 9.3),
                                                         ('Hollow Knight', 'Plataformas', 2017, 9.1),
                                                         ('Celeste', 'Plataformas', 2018, 8.9);

INSERT INTO jugadores (nombre, nickname, email) VALUES
                                                    ('Javier García', 'JaviGamer', 'javier@example.com'),
                                                    ('Laura Martínez', 'LauPlays', 'laura@example.com'),
                                                    ('Carlos Ruiz', 'Charly99', 'carlos@example.com');

INSERT INTO listas (nombre, id_jugador) VALUES
                                            ('Favoritos de Javi', 1),
                                            ('Pendientes de Laura', 2),
                                            ('Completados de Carlos', 3);

INSERT INTO listas_videojuegos (id_lista, id_videojuego, fecha_añadido) VALUES
                                                                            (1, 1, '2026-11-20'),
                                                                            (1, 4, '2026-11-20'),
                                                                            (2, 2, '2026-11-21'),
                                                                            (3, 3, '2026-11-21');