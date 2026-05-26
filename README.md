# API Adopción — Equipo 7

Este repositorio contiene el proyecto de API **Spring Boot + Kotlin** para el sistema de adopción de mascotas del Equipo 7.

**Versión:** `4.0.0` — Iteración 4  
**Estado:** Consolidación final del backend: edición de publicaciones, confirmación de adopción, panel de solicitudes y eliminación de publicaciones propias.

---

## Tabla de contenido

1. [Descripción general](#descripción-general)
2. [Documentación del proyecto](#documentación-del-proyecto)
3. [Tecnologías utilizadas](#tecnologías-utilizadas)
4. [Historial por iteración](#historial-por-iteración)
5. [Arquitectura del backend](#arquitectura-del-backend)
6. [Requisitos previos](#requisitos-previos)
7. [Configuración de base de datos](#configuración-de-base-de-datos)
8. [Variables de entorno](#variables-de-entorno)
9. [Levantamiento del proyecto](#levantamiento-del-proyecto)
10. [Endpoints principales](#endpoints-principales)
11. [Pruebas rápidas con cURL](#pruebas-rápidas-con-curl)
12. [Validaciones importantes](#validaciones-importantes)
13. [Versionamiento](#versionamiento)

---

## Descripción general

La API permite gestionar usuarios, publicaciones de mascotas en adopción e interacciones entre usuarios interesados y publicaciones.

El sistema permite:

- Registrar usuarios.
- Iniciar y cerrar sesión mediante token.
- Consultar y actualizar información del usuario autenticado.
- Publicar mascotas en adopción.
- Consultar catálogo de mascotas disponibles.
- Buscar mascotas por nombre.
- Consultar detalle de una publicación.
- Registrar interés en una mascota.
- Consultar publicaciones propias.
- Consultar mascotas marcadas como interés.
- Editar publicaciones propias.
- Confirmar adopción marcando una mascota como `ADOPTADO`.
- Consultar solicitudes de interés recibidas por el publicador.
- Eliminar publicaciones propias disponibles.

> **Importante:** El backend espera el token en el encabezado `Authorization` sin prefijo `Bearer`.

Ejemplo correcto:

```http
Authorization: 123e4567-e89b-12d3-a456-426614174000
```

Ejemplo incorrecto:

```http
Authorization: Bearer 123e4567-e89b-12d3-a456-426614174000
```

---

## Documentación del proyecto

La documentación evolutiva del sistema se encuentra en el siguiente enlace:

- **Documentación Iteración 4:** `[https://www.notion.so/Sistema-Adopcion-Equipo7-36bde1a623d78086a808cb4a422f185e?source=copy_link]`

Este documento concentra la especificación de requerimientos, actores, casos de uso, diagramas, arquitectura, flujo de autenticación, modelo de datos, limitaciones conocidas y entregables de la versión final.


---

## Tecnologías utilizadas

- **Kotlin**
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven Wrapper**
- **dotenv-kotlin**
- **MessageDigest / SHA-256** para hash de contraseñas
- **SLF4J** para registros

---

## Historial por iteración

### Iteración 1 — Backend funcional y usuario

La API gestiona el ciclo de vida básico de la entidad `Usuario`:

- Registro de usuarios.
- Inicio de sesión.
- Generación de token.
- Consulta de información del usuario autenticado.
- Cierre de sesión e invalidación del token.

---

### Iteración 2 — Integración con frontend

El backend se integra con el frontend mediante una arquitectura cliente-servidor.

Cambios principales:

- Endpoint de actualización de datos de usuario.
- Configuración de CORS para permitir peticiones desde el frontend.
- Uso del token en el encabezado `Authorization`.

---

### Iteración 3 — Publicaciones e interacción de adopción

Se incorporan funcionalidades principales relacionadas con la adopción de mascotas:

- Publicación de mascotas.
- Consulta de catálogo.
- Búsqueda por nombre.
- Consulta de detalle.
- Mis publicaciones.
- Registro de interés.
- Consulta de mascotas marcadas como interés.

---

### Iteración 4 — Consolidación final

Se agregan funcionalidades de gestión y cierre del proceso de adopción:

- **Edición de publicaciones propias:** `PUT /mascotas/editar/{id}`
- **Confirmación de adopción:** `PUT /mascotas/{id}/adoptar`
- **Panel de solicitudes recibidas:** `GET /solicitudes`
- **Eliminación de publicaciones propias:** `DELETE /mascotas/{id}`

Estas funcionalidades validan token, propiedad de la publicación, existencia del recurso y estado de la mascota antes de modificar la base de datos.

---

## Arquitectura del backend

El backend mantiene una arquitectura por capas:

```text
Controller → Service → Repository
```

### Controller

Recibe peticiones HTTP, extrae parámetros, cuerpo de la petición y encabezados, y devuelve respuestas HTTP.

Ejemplos:

- `UsuarioController`
- `MascotaController`
- `SolicitudController`

### Service

Contiene la lógica de negocio, validaciones y reglas del sistema.

Ejemplos:

- validar token;
- verificar dueño de publicación;
- impedir edición de mascotas adoptadas;
- registrar interés;
- agrupar solicitudes por mascota.

### Repository

Se encarga del acceso a base de datos mediante Spring Data JPA.

Ejemplos:

- `UsuarioRepository`
- `MascotaRepository`
- `InteresAdopcionRepository`

### DTOs y Mappers

El sistema utiliza DTOs y mappers para separar:

- datos recibidos por la API;
- modelos de dominio;
- entidades de persistencia;
- respuestas enviadas al frontend.

---

## Requisitos previos

- Java 21
- PostgreSQL
- Maven Wrapper incluido (`./mvnw`)
- Archivo `.env` en la raíz del proyecto

> **Convención del equipo:** PostgreSQL en puerto `5432`.

---

## Configuración de base de datos

### 1. Instalar PostgreSQL en Debian

```bash
sudo apt update
sudo apt install -y postgresql postgresql-contrib
sudo systemctl enable --now postgresql
```

---

### 2. Crear base de datos y usuario

El archivo principal es:

```text
database/schema.sql
```

Este script:

- elimina la base anterior si existe;
- crea la base `adopcion`;
- crea el rol `equipo7` si no existe;
- asigna permisos al usuario de aplicación;
- crea las tablas necesarias.

Configuración usada:

```text
Base de datos: adopcion
Usuario: equipo7
Password: equipo7
Puerto: 5432
```

Ejecutar:

```bash
sudo -u postgres psql -f database/schema.sql
```

---

### 3. Verificar tablas

```bash
sudo -u postgres psql -d adopcion -c "\dt"
```

Tablas esperadas:

```text
usuario
animales
intereses_adopcion
```

Ver estructura de tablas:

```bash
sudo -u postgres psql -d adopcion -c "\d usuario"
sudo -u postgres psql -d adopcion -c "\d animales"
sudo -u postgres psql -d adopcion -c "\d intereses_adopcion"
```

Probar conexión con el usuario de aplicación:

```bash
psql -h 127.0.0.1 -p 5432 -U equipo7 -d adopcion
```

---

## Variables de entorno

Crear archivo `.env` en la raíz del proyecto.

> Este archivo no debe subirse al repositorio.

Ejemplo:

```env
URL_DB=127.0.0.1:5432/adopcion
USER_DB=equipo7
PASSWORD_DB=equipo7
```

También existe `.env.example` como referencia.

---

## Levantamiento del proyecto

Compilar:

```bash
./mvnw clean compile
```

Levantar la API:

```bash
./mvnw spring-boot:run
```

Si todo está correcto, debe aparecer en consola algo similar a:

```text
Tomcat started on port 8080
Started ...Application...
```

La API queda disponible en:

```text
http://localhost:8080
```

---

## Endpoints principales

### Usuarios

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/usuarios/register` | Registrar usuario |
| `POST` | `/usuarios/login` | Iniciar sesión |
| `GET` | `/usuarios/me` | Consultar usuario autenticado |
| `PUT` | `/usuarios` | Actualizar datos del usuario |
| `POST` | `/usuarios/logout` | Cerrar sesión |

---

### Mascotas

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/mascotas/publicar` | Publicar mascota |
| `GET` | `/mascotas` | Consultar catálogo de mascotas disponibles |
| `GET` | `/mascotas?nombre={nombre}` | Buscar mascotas por nombre |
| `GET` | `/mascotas?filtro=mis-publicaciones` | Consultar publicaciones propias |
| `GET` | `/mascotas?filtro=me-interesa` | Consultar mascotas marcadas con interés |
| `GET` | `/mascotas/detalle/{id}` | Consultar detalle de mascota |
| `POST` | `/mascotas/{id}/interes` | Registrar interés en una mascota |
| `PUT` | `/mascotas/editar/{id}` | Editar publicación propia |
| `PUT` | `/mascotas/{id}/adoptar` | Marcar mascota como adoptada |
| `DELETE` | `/mascotas/{id}` | Eliminar publicación propia |

---

### Solicitudes

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/solicitudes` | Consultar solicitudes de interés recibidas por el publicador |

---

## Pruebas rápidas con cURL

Las pruebas siguientes asumen que la API está corriendo en:

```text
http://localhost:8080
```

---

### 1. Registrar usuario

```bash
curl -i -X POST http://localhost:8080/usuarios/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Prueba",
    "email": "prueba@ciencias.unam.mx",
    "codigoPostal": "04360",
    "password": "clave123"
  }'
```

---

### 2. Login

```bash
LOGIN=$(curl -s -X POST http://localhost:8080/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "prueba@ciencias.unam.mx",
    "password": "clave123"
  }')

echo "$LOGIN"
```

Extraer token y userId:

```bash
TOKEN=$(python3 -c 'import sys,json; print(json.load(sys.stdin)["token"])' <<< "$LOGIN")
USER_ID=$(python3 -c 'import sys,json; print(json.load(sys.stdin)["userId"])' <<< "$LOGIN")

echo "$TOKEN"
echo "$USER_ID"
```

---

### 3. Consultar usuario autenticado

```bash
curl -i http://localhost:8080/usuarios/me \
  -H "Authorization: $TOKEN"
```

---

### 4. Publicar mascota

```bash
MASCOTA=$(curl -s -X POST http://localhost:8080/mascotas/publicar \
  -H "Authorization: $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"nombre\": \"Luna\",
    \"descripcion\": \"Perrita tranquila color café\",
    \"fotoBase64\": \"imagen-base64-de-prueba\",
    \"tipo\": \"Perro\",
    \"raza\": \"Mestiza\",
    \"codigoPostal\": \"03000\",
    \"usuarioId\": \"$USER_ID\"
  }")

echo "$MASCOTA"
```

Extraer `animalId`:

```bash
ANIMAL_ID=$(python3 -c 'import sys,json; print(json.load(sys.stdin)["animalId"])' <<< "$MASCOTA")
echo "$ANIMAL_ID"
```

---

### 5. Listar publicaciones disponibles

```bash
curl -i http://localhost:8080/mascotas
```

El catálogo general debe mostrar publicaciones en estado:

```text
DISPONIBLE
```

---

### 6. Buscar por nombre

```bash
curl -i "http://localhost:8080/mascotas?nombre=luna"
```

---

### 7. Consultar detalle

```bash
curl -i http://localhost:8080/mascotas/detalle/$ANIMAL_ID
```

---

### 8. Consultar mis publicaciones

```bash
curl -i "http://localhost:8080/mascotas?filtro=mis-publicaciones" \
  -H "Authorization: $TOKEN"
```

---

### 9. Registrar interés

Para probar interés correctamente, lo ideal es usar un segundo usuario distinto al publicador.  
Con el mismo token puede funcionar o fallar dependiendo de las reglas que el equipo haya decidido aplicar.

```bash
curl -i -X POST http://localhost:8080/mascotas/$ANIMAL_ID/interes \
  -H "Authorization: $TOKEN"
```

Resultado esperado si se registra correctamente:

```json
{
  "mensaje": "Interés registrado correctamente",
  "animalId": 1,
  "usuarioId": "id-del-usuario"
}
```

---

### 10. Validar interés duplicado

```bash
curl -i -X POST http://localhost:8080/mascotas/$ANIMAL_ID/interes \
  -H "Authorization: $TOKEN"
```

Resultado esperado:

```text
HTTP/1.1 409
```

---

### 11. Consultar mascotas marcadas como interés

```bash
curl -i "http://localhost:8080/mascotas?filtro=me-interesa" \
  -H "Authorization: $TOKEN"
```

---

### 12. Editar publicación propia

```bash
curl -i -X PUT http://localhost:8080/mascotas/editar/$ANIMAL_ID \
  -H "Authorization: $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"nombre\": \"Luna actualizada\",
    \"descripcion\": \"Perrita tranquila, sociable y vacunada\",
    \"fotoBase64\": \"imagen-base64-actualizada\",
    \"tipo\": \"Perro\",
    \"raza\": \"Mestiza\",
    \"codigoPostal\": \"03100\",
    \"usuarioId\": \"$USER_ID\"
  }"
```

Resultado esperado:

```text
HTTP/1.1 200
```

y la respuesta debe incluir la mascota actualizada.

---

### 13. Consultar panel de solicitudes recibidas

```bash
curl -i http://localhost:8080/solicitudes \
  -H "Authorization: $TOKEN"
```

Resultado esperado:

- lista de mascotas publicadas por el usuario autenticado;
- interesados agrupados por mascota;
- si no hay solicitudes, lista vacía o listas de interesados vacías según corresponda.

---

### 14. Confirmar adopción

```bash
curl -i -X PUT http://localhost:8080/mascotas/$ANIMAL_ID/adoptar \
  -H "Authorization: $TOKEN"
```

Resultado esperado:

```text
HTTP/1.1 200
```

La mascota debe cambiar su estado a:

```text
ADOPTADO
```

Después de esto, no debe aparecer en el catálogo general de mascotas disponibles.

---

### 15. Intentar confirmar adopción nuevamente

```bash
curl -i -X PUT http://localhost:8080/mascotas/$ANIMAL_ID/adoptar \
  -H "Authorization: $TOKEN"
```

Resultado esperado:

```text
HTTP/1.1 409
```

---

### 16. Eliminar publicación propia disponible

Para probar eliminación, se recomienda publicar otra mascota que siga en estado `DISPONIBLE`.

```bash
curl -i -X DELETE http://localhost:8080/mascotas/$ANIMAL_ID \
  -H "Authorization: $TOKEN"
```

Resultado esperado si la publicación está disponible y pertenece al usuario:

```text
HTTP/1.1 200
```

Respuesta esperada:

```json
{
  "mensaje": "Publicación eliminada correctamente"
}
```

---

## Validaciones importantes

### Rutas protegidas sin token

```bash
curl -i "http://localhost:8080/mascotas?filtro=me-interesa"
curl -i http://localhost:8080/solicitudes
curl -i -X PUT http://localhost:8080/mascotas/editar/1 \
  -H "Content-Type: application/json" \
  -d '{}'
curl -i -X DELETE http://localhost:8080/mascotas/1
```

Resultado esperado:

```text
HTTP/1.1 401
```

---

### Publicación inexistente

```bash
curl -i http://localhost:8080/mascotas/detalle/999
curl -i -X POST http://localhost:8080/mascotas/999/interes \
  -H "Authorization: $TOKEN"
curl -i -X PUT http://localhost:8080/mascotas/editar/999 \
  -H "Authorization: $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{}'
curl -i -X DELETE http://localhost:8080/mascotas/999 \
  -H "Authorization: $TOKEN"
```

Resultado esperado:

```text
HTTP/1.1 404
```

---

### Usuario no dueño

Para probar `403 Forbidden`, se debe:

1. crear una mascota con un usuario;
2. iniciar sesión con otro usuario;
3. intentar editar, adoptar o eliminar la mascota del primer usuario.

Endpoints a probar con el segundo token:

```bash
curl -i -X PUT http://localhost:8080/mascotas/editar/$ANIMAL_ID \
  -H "Authorization: $TOKEN_OTRO_USUARIO" \
  -H "Content-Type: application/json" \
  -d '{}'

curl -i -X PUT http://localhost:8080/mascotas/$ANIMAL_ID/adoptar \
  -H "Authorization: $TOKEN_OTRO_USUARIO"

curl -i -X DELETE http://localhost:8080/mascotas/$ANIMAL_ID \
  -H "Authorization: $TOKEN_OTRO_USUARIO"
```

Resultado esperado:

```text
HTTP/1.1 403
```

---

## Resultado esperado final

Al terminar la validación:

1. `schema.sql` crea correctamente la base `adopcion`.
2. El usuario `equipo7` puede conectarse sin errores de permisos.
3. Las tablas `usuario`, `animales` e `intereses_adopcion` existen correctamente.
4. El backend compila y levanta correctamente.
5. Registro, login, consulta, actualización y logout funcionan.
6. La publicación de mascotas funciona.
7. La consulta, búsqueda y detalle de mascotas funcionan.
8. Los filtros `mis-publicaciones` y `me-interesa` funcionan con token.
9. El sistema registra correctamente el interés en una mascota.
10. El sistema consulta solicitudes recibidas mediante `GET /solicitudes`.
11. El sistema permite editar publicaciones propias mediante `PUT /mascotas/editar/{id}`.
12. El sistema permite confirmar adopción mediante `PUT /mascotas/{id}/adoptar`.
13. El sistema permite eliminar publicaciones propias mediante `DELETE /mascotas/{id}`.
14. El sistema controla errores esperados: token faltante, token inválido, publicación inexistente, interés duplicado, usuario no dueño y publicación no disponible.

---

## Versionamiento

Cuando el proyecto esté integrado en `main`, compile correctamente y pase las pruebas principales, se crea el tag de backend:

```bash
git checkout main
git pull origin main
git tag -a 4.0.0 -m "Release backend 4.0.0 - Iteración 4"
git push origin 4.0.0
```

Si ya se creó un tag local por error y se necesita corregirlo antes de subirlo:

```bash
git tag -d 4.0.0
```

Si el tag remoto ya existe, no se debe sobrescribir sin acuerdo del equipo.
