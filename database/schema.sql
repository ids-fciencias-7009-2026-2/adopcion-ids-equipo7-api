DROP DATABASE IF EXISTS adopcion;

DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'equipo7') THEN
        CREATE ROLE equipo7 LOGIN PASSWORD 'equipo7';
    ELSE
        ALTER ROLE equipo7 WITH LOGIN PASSWORD 'equipo7';
    END IF;
END
$$;

CREATE DATABASE adopcion;
GRANT CONNECT ON DATABASE adopcion TO equipo7;

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

CREATE TABLE IF NOT EXISTS animales (
    animal_id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT NOT NULL,
    foto_base64 TEXT NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    raza VARCHAR(100) NOT NULL,
    codigo_postal VARCHAR(10) NOT NULL,
    usuario_id VARCHAR(50) NOT NULL,
    estado_publicacion VARCHAR(50) NOT NULL DEFAULT 'DISPONIBLE'
);

CREATE TABLE IF NOT EXISTS intereses_adopcion (
    interes_id BIGSERIAL PRIMARY KEY,
    usuario_id VARCHAR(50) NOT NULL,
    animal_id BIGINT NOT NULL,
    fecha_interes TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_interes_usuario_animal UNIQUE (usuario_id, animal_id),
    CONSTRAINT fk_interes_animal FOREIGN KEY (animal_id)
        REFERENCES animales(animal_id)
        ON DELETE CASCADE
);

GRANT USAGE ON SCHEMA public TO equipo7;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO equipo7;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO equipo7;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO equipo7;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT USAGE, SELECT ON SEQUENCES TO equipo7;