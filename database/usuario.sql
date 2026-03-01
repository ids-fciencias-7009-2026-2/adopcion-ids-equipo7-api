/*
Creación de la tabla usuario con las especificaciones mínimas, más unos agregados del equipo
@usuario siendo la entidad usuario que se quiere registrar
*/
CREATE TABLE usuario (
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    codigo_postal VARCHAR(10) NOT NULL,
    password VARCHAR(255),
    token VARCHAR(255)
);
