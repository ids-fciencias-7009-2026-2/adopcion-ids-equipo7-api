# API Adopción — Equipo 7

Este repositorio contiene el proyecto de API Spring Boot con Kotlin para el sistema de adopción de perritos.
**Versión:** 3.0.0 (Iteración 3) - *Publicaciones de mascotas, búsqueda e interacción de adopción.*

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

## Iteración 1 - Backend funcional y Usuario
En esta versión, la API gestiona el ciclo de vida de registro y autenticación de la entidad `Usuario` mediante tokens:
* **Registro de Usuarios:** Creación y persistencia de cuentas con contraseñas cifradas.
* **Inicio de sesión:** Validación de credenciales y generación de tokens.
* **Consulta de información:** Acceso seguro a la información del usuario que este autenticado.
* **Cierre de sesión:** Invalidación del token activo.

---

## Práctica 3 - Script común de base de datos
En esta práctica se consolidó la creación de la base de datos en un único script reproducible, con el objetivo de que cualquier integrante del equipo pueda reconstruir el entorno de backend desde cero.

---

## Iteración 2 - Integración con el frontend
En esta versión, el backend evoluciona para integrarse completamente con el frontend adoptando una arquitecura general cliente-servidor:
* **Actualización:** Implementación del endpoint que permite modificar la información del usuario.
* **Comunicación con el frontend:** Configuración nativa para recibir peticiones asíncronas de manera segura desde el frontend.

---

## Iteración 3 - Publicaciones e interacciones de adopción
En esta versión, el sistema se expande más allá del módulo de usuarios e incorpora funcionalidades principales relacionadas con la adopción de mascotas.

Se agregan nuevos casos de uso relacionados con la entidad Mascota y con la interacción entre usuarios y publicaciones:

* **Publicación de mascotas**: Permite registrar animales en adopción dentro de la plataforma. 
* **Detalle de publicación**: Permite consultar la información completa de una mascota publicada. 
* **Listado y búsqueda de publicaciones**: Permite consultar publicaciones, buscar por nombre y aplicar filtros. 
* **Mis publicaciones**: Permite consultar las mascotas publicadas por el usuario autenticado. 
* **Me interesa**: Permite registrar interés en adoptar una mascota. 
* **Mascotas de interés**: Permite consultar las mascotas que el usuario marcó como interés.



---
## Levantamiento del proyecto

### Requisitos 
Java 21
PostgreSQL
Maven Wrapper 
Archivo .env en la raíz del proyecto (este se debera crear con un ejemplo mas adelante se especifica en el archivo)

### 1) Instalar PostgreSQL (Debian)
```bash
sudo apt update
sudo apt install -y postgresql postgresql-contrib
sudo systemctl enable --now postgresql
```

---

### 2) Creación de base de datos y usuario
El archiv principal para esta práctica es : database/schema.sql
Este script
- Elimina la base de datos anterior si existe, crea nuevamente la base `adopcion`, crea el rol `equipo7` si no existe, asigna permisos al usuario de aplicación, y crea la tabla `usuario` `animales` `intereses_adopcion`

En esta guía usamos:
- Base de datos: `adopcion`
- Usuario: `equipo7`
- Password: `equipo7`

```bash
# Ejecutar el script desde cero
sudo -u postgres psql -f database/schema.sql

```

---

### 3) Verificar que la base fue creada correctamente
El script está en `database/schema.sql`.

```bash
sudo -u postgres psql -d adopcion -c "\dt"
```
Tablas esperadas: 
`usuario`
`animales`
`intereses_adopcion`

```bash
sudo -u postgres psql -d adopcion -c "\d usuario"
sudo -u postgres psql -d adopcion -c "\d animales"
sudo -u postgres psql -d adopcion -c "\d interes_adopcion"

```
También puede verificarse el acceso con el usuario de la aplicación:
```bash
psql -h 127.0.0.1 -p 5432 -U equipo7 -d adopcion

```
Si la instalación es correcta, el usuario `equipo7` debe poder conectarse y consultar la tabla `usuario`


### 4) Permisos sobre la base de datos
En la Práctica 3, los permisos del usuario equipo7 ya se configuran automáticamente dentro de database/schema.sql.  
Por ello, ya no es necesario ejecutar manualmente comandos adicionales de GRANT como parte del flujo normal de instalación.

---

### 5) Crear archivo `.env` (este por convención NO se sube al repositorio)
Crea un archivo `.env` en la raíz del proyecto (este archivo es ignorado por git).

`.env` (ejemplo):
```env
URL_DB=127.0.0.1:5432/adopcion
USER_DB=equipo7
PASSWORD_DB=equipo7
```

También existe `.env.example` como referencia de variables.

---

### 6) Correr el proyecto
```bash
./mvnw clean compile
./mvnw spring-boot:run
```

Si todo está correcto, Spring Boot debe iniciar sin errores de conexión y en consola deberían verse mensajes de Hibernate/JPA y:
- `Tomcat started on port 8080`
- `Started ...Application...`

---

### 7) Prueba minima de funcionamiento 
```bash
curl -X POST http://localhost:8080/usuarios/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Prueba",
    "email": "prueba@ciencias.unam.mx",
    "codigoPostal": "04360",
    "password": "clave123"
  }'

curl -X POST http://localhost:8080/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "prueba@ciencias.unam.mx",
    "password": "clave123"
  }'

```

### 8) Prueba minima de funcionamineto - Mascotas
Se tendra que agregar el userId obtenido en el login

```bash
curl -X POST http://localhost:8080/mascotas/publicar \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Luna",
    "descripcion": "Perrita tranquila color café",
    "fotoBase64": "imagen-base64-de-prueba",
    "tipo": "Perro",
    "raza": "Mestiza",
    "codigoPostal": "03000",
    "usuarioId": "cambiar_por_el_userId"
  }'
```
**Resultado esperado :** 
```bash
{
  "animalId": 1,
  "nombre": "Luna",
  "descripcion": "Perrita tranquila color café",
  "fotoBase64": "imagen-base64-de-prueba",
  "tipo": "Perro",
  "raza": "Mestiza",
  "codigoPostal": "03000",
  "usuarioId": "userId del login",
  "estadoPublicacion": "DISPONIBLE"
}
```
* **Listar publicaciones**
```bash
curl http://localhost:8080/mascotas

```

**Resultado esperado:**
```bash
[
  {
    "animalId": 1,
    "nombre": "Luna",
    "descripcion": "Perrita tranquila color café",
    "fotoBase64": "imagen-base64-de-prueba",
    "tipo": "Perro",
    "raza": "Mestiza",
    "codigoPostal": "03000",
    "usuarioId": "userId del login",
    "estadoPublicacion": "DISPONIBLE"
  }
]
```

**Buscar por nombre**
```bash
curl "http://localhost:8080/mascotas?nombre=luna"
```

**Resultado esperado:**
```bash
[
  {
    "animalId": 1,
    "nombre": "Luna",
    "descripcion": "Perrita tranquila color café",
    "fotoBase64": "imagen-base64-de-prueba",
    "tipo": "Perro",
    "raza": "Mestiza",
    "codigoPostal": "03000",
    "usuarioId": "userId del login",
    "estadoPublicacion": "DISPONIBLE"
  }
]
```

**Consultar mis publicaciones:**
Se tendra que agregar el token obtenido en login
```bash
curl "http://localhost:8080/mascotas?filtro=mis-publicaciones" \
  -H "Authorization: cambiar_token"
```

**Resultado esperado:**
```bash
[
  {
    "animalId": 1,
    "nombre": "Luna",
    "descripcion": "Perrita tranquila color café",
    "fotoBase64": "imagen-base64-de-prueba",
    "tipo": "Perro",
    "raza": "Mestiza",
    "codigoPostal": "03000",
    "usuarioId": "userId del login",
    "estadoPublicacion": "DISPONIBLE"
  }
]
```
**Registrar interés en una mascota**
Se tendra que cambiar 1 por el animalId real y el token obtenido en login.
```bash
curl -X POST http://localhost:8080/mascotas/1/interes \
  -H "Authorization: PEGA_AQUI_EL_TOKEN"
```
**Resultado esperado:**
```bash
{
  "mensaje": "Interés registrado correctamente",
  "animalId": 1,
  "usuarioId": "userId del login"
}
```

**Validar interés duplicado**
```bash
curl -i -X POST http://localhost:8080/mascotas/1/interes \
  -H "Authorization: cambiar_token"
```
**Resultado esperado:**
```bash
HTTP/1.1 409

{
  "error": "Ya registraste interés en esta mascota"
}
```


**Consultar mascotas marcadascomo interés**
```bash
curl "http://localhost:8080/mascotas?filtro=me-interesa" \
  -H "Authorization: cambiar_token"
```

**Resultado esperado:**
```bash
[
  {
    "animalId": 1,
    "nombre": "Luna",
    "descripcion": "Perrita tranquila color café",
    "fotoBase64": "imagen-base64-de-prueba",
    "tipo": "Perro",
    "raza": "Mestiza",
    "codigoPostal": "03000",
    "usuarioId": "userId del login",
    "estadoPublicacion": "DISPONIBLE"
  }
]
```

**Buscar dentro de mascotas marcadas como interés:**
```bash
curl "http://localhost:8080/mascotas?filtro=me-interesa&nombre=luna" \
  -H "Authorization: cambiar_token"
```
**Resultado esperado:**
```bash
[
  {
    "animalId": 1,
    "nombre": "Luna",
    "descripcion": "Perrita tranquila color café",
    "fotoBase64": "imagen-base64-de-prueba",
    "tipo": "Perro",
    "raza": "Mestiza",
    "codigoPostal": "03000",
    "usuarioId": "userId del login",
    "estadoPublicacion": "DISPONIBLE"
  }
]
```
**Validar errores esperados:**
**Filtro protegido sin token**
```bash
curl -i "http://localhost:8080/mascotas?filtro=me-interesa"
```

**Resultado esperado**
```bash
HTTP/1.1 401

{
  "error": "Token no proporcionado"
}
```

**Interés sobre publicación inexistente:**
```bash
curl -i -X POST http://localhost:8080/mascotas/999/interes \
  -H "Authorization: cambiar_token"
```

**Resultado esperado:**
```bash

HTTP/1.1 404

{
  "error": "Publicación no encontrada"
}
```


**Resultado esperado final:**
1. el script `schema.sql` crea correctamente la base adopcion. 
2. el usuario `equipo7` puede conectarse sin errores de permisos.
3. Las tablas usuario, animales e intereses_adopcion existen correctamente. 
4. el backend compila y levanta correctamente. 
5. el registro y login de usuarios funciona. 
6. el login responde exitosamente.
7. La consulta y búsqueda de mascotas funciona.
8. Los filtros mis-publicaciones y me-interesa funcionan con token.
9. El sistema registra correctamente el interés en una mascota.
10. El sistema controla errores esperados como token faltante, publicación inexistente e interés duplicado.


## Versionamiento
Cuando el proyecto levanta correctamente, se crea el tag:

```bash
git checkout main
git pull origin main
git tag 3.0.0
git push origin 3.0.0
```

