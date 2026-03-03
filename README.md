# API Adopción — Equipo 7

Este repositorio contiene el proyecto de API Spring Boot con Kotlin para el sistema de adopción de perritos.

---

## Práctica 1 — Postman
- Video (pruebas de endpoints): https://drive.google.com/file/d/1nf5yK5B21EGwNlne6fQfNRqQ-xI63-3N/view?usp=sharing
- Colección Postman en: `postman/practica1-equipo7.postman_collection.json`

---

## Práctica 2 — Conexión a PostgreSQL con `.env`

### Requisitos
- Java 21
- PostgreSQL (Debian)
- Maven Wrapper incluido (`./mvnw`)

> **Convención del equipo:** PostgreSQL en **puerto 5432**.

---

## 1) Instalar PostgreSQL (Debian)
```bash
sudo apt update
sudo apt install -y postgresql postgresql-contrib
sudo systemctl enable --now postgresql
```

---

## 2) Crear base de datos y usuario (recomendado)
En esta guía usamos:
- Base de datos: `adopcion`
- Usuario: `equipo7`
- Password: `equipo7`

```bash
# Crear BD
sudo -u postgres psql -c "CREATE DATABASE adopcion;"

# Crear usuario para la app
sudo -u postgres psql -c "CREATE USER equipo7 WITH PASSWORD 'equipo7';"

# Permisos
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE adopcion TO equipo7;"
sudo -u postgres psql -d adopcion -c "GRANT ALL ON SCHEMA public TO equipo7;"
sudo -u postgres psql -d adopcion -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO equipo7;"
```

---

## 3) Crear tabla `usuario` con el script del repo
El script está en `database/usuario.sql`.

```bash
sudo -u postgres psql -d adopcion -f database/usuario.sql
sudo -u postgres psql -d adopcion -c "\dt"
sudo -u postgres psql -d adopcion -c "\d usuario"
```

---

## 4) Crear archivo `.env` (este por convención NO se sube al repositorio)
Crea un archivo `.env` en la raíz del proyecto (este archivo es ignorado por git).

`.env` (ejemplo):
```env
URL_DB=127.0.0.1:5432/adopcion
USER_DB=equipo7
PASSWORD_DB=equipo7
```

También existe `.env.example` como referencia de variables.

---

## 5) Correr el proyecto
```bash
./mvnw clean test
./mvnw spring-boot:run
```

Si todo está correcto, Spring Boot debe iniciar sin errores de conexión y en consola deberían verse mensajes de Hibernate/JPA y:
- `Tomcat started on port 8080`
- `Started ...Application...`

---

## Entregables Práctica 2 (en el repositorio)
Carpeta `database/`:
- `usuario.sql` (script de creación de tabla)
---

## Tag de versión `0.0.1`
Cuando:
- el proyecto levanta correctamente,
- la conexión a la BD es exitosa,
- la tabla `usuario` fue creada,
- y todo está en `main`,

crear el tag:

```bash
git checkout main
git pull origin main
git tag 0.0.1
git push origin 0.0.1
```

