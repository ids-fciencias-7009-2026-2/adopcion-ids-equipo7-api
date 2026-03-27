DROP DATABASE IF EXISTS adopcion;
CREATE DATABASE adopcion;
\connect adopcion;

DROP TABLE IF EXISTS usuario;

CREATE TABLE usuario (
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    codigo_postal VARCHAR(10) NOT NULL,
    password VARCHAR(255),
    token VARCHAR(255)
);