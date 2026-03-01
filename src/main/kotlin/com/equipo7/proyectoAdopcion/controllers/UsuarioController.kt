package com.equipo7.proyectoAdopcion.controllers


import com.equipo7.proyectoAdopcion.domain.Usuario
import com.equipo7.proyectoAdopcion.dto.request.LoginRequest
import com.equipo7.proyectoAdopcion.dto.response.LogoutResponse
import com.equipo7.proyectoAdopcion.dto.request.CreateUsuarioRequest
import com.equipo7.proyectoAdopcion.dto.request.UpdateUsuarioRequest
import com.equipo7.proyectoAdopcion.domain.toUsuario
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

/**
 * Controlador para la gestión de usuarios del sistema de adopción.
 */
@Controller
@RequestMapping("/usuarios") // Prefijo base para todos los endpoints de este controlador
class UsuarioController {

    /**
     * Logger para registrar eventos importantes del flujo de ejecución.
     *
     * Permite imprimir información útil en consola para depuración,
     * auditoría y análisis del comportamiento del sistema.
     */
    private val logger: Logger = LoggerFactory.getLogger(UsuarioController::class.java)

    /**
     * Endpoint para obtener la información del usuario actual.
     *
     * Utiliza datos simulados (fake) para esta etapa de la practica.
     *
     * URL: http://localhost:8080/usuarios/me
     * Metodo: GET
     *
     * @return ResponseEntity con un objeto Usuario y código HTTP 200 (OK).
     */
    @GetMapping("/me")
    fun retrieveUsuario(): ResponseEntity<Usuario> {

        // Simulación de un usuario registrado en la plataforma
        // Se debe proporcionar como minimo esta información
        val usuarioFake = Usuario(
            id = "123",
            nombre = "Usuario1",
            email = "usuario1@ciencias.unam.mx",
            codigoPostal = "04510"
        )

        logger.info("User found in database: ${usuarioFake.nombre}")

        // Retorna HTTP 200 junto con el usuario encontrado
        return ResponseEntity.ok(usuarioFake)
    }

    /**
     * Endpoint encargado de registrar un nuevo usuario.
     *
     * Recibe un JSON con los datos necesarios para crear el usuario
     * y los transforma en un objeto de dominio.
     *
     * URL:    http://localhost:8080/usuarios/register
     * Metodo: POST
     *
     * @param createUsuarioRequest DTO que representa el body del request.
     * @return ResponseEntity con el usuario creado y código HTTP 200 (OK).
     */
    @PostMapping("/register")
    fun agregaUsuario(
        @RequestBody createUsuarioRequest: CreateUsuarioRequest
    ): ResponseEntity<Usuario> {

        /**
         *   Se espera un JSON de la forma:
         *   {
         *      "nombre": "Nombre",
         *      "email": "alguien@ciencias.unam.mx",
         *      "codigoPostal": "00000",
         *      "password": "myPassword"
         *  }
         */
        // Conversión de DTO a objeto de dominio usando una extension function
        val usuarioParaAgregar = createUsuarioRequest.toUsuario()


        logger.info("Nuevo usuario agregado: $usuarioParaAgregar")

        // En esta etapa no se guarda en BD, solo se simula la creación
        return ResponseEntity.ok(usuarioParaAgregar)
    }

    /**
    * Endpoint que simula el proceso de autenticación de un usuario.
    *
    * Recibe correo y contraseña y los compara contra un usuario ficticio.
    *
    * URL:    http://localhost:8080/usuarios/login
    * Metodo: POST
    *
    * @param loginRequest DTO con las credenciales del usuario.
    * @return HTTP 200 si las credenciales son correctas,
    *         HTTP 401 si son incorrectas.
    */
   @PostMapping("/login")
   fun login(
       @RequestBody loginRequest: LoginRequest
   ): ResponseEntity<Any> {

       // Usuario simulado obtenido de la base de datos.
       val usuarioFake = Usuario(
           id = "456",
           nombre = "usuario2",
           email = "usuario2@ciencias.unam.mx",
           codigoPostal = "08200",
           password = "Test123."
       )
       logger.info("try make login with: $loginRequest")
       return if (usuarioFake.password == loginRequest.password) {
           logger.info("Login successful")
           // HTTP 200 → autenticación exitosa
           ResponseEntity.ok(mapOf("message" to "Welcome", "userId" to usuarioFake.id))
       } else {
           logger.error("Login failed for: $loginRequest")
           // HTTP 401 → Unauthorized (credenciales inválidas)
           ResponseEntity.status(401).body(mapOf("error" to "Invalid Credentials"))
       }
   }

   /**
       * Endpoint que simula el cierre de sesión del usuario.
       *
       * Genera una respuesta con el identificador del usuario
       * y la fecha/hora del logout.
       *
       * URL:    http://localhost:8080/usuarios/logout
       * Metodo: POST
       *
       * @return ResponseEntity con información del logout.
       */
      @PostMapping("/logout")
      fun logout(): ResponseEntity<Any> {

          val usuarioFake = Usuario(
            id = "789",
            nombre = "usuario3",
            email = "usuario3@ciencias.unam.mx",
            codigoPostal = "08000"
          )

          val logoutResponse = LogoutResponse(
              usuarioFake.id,
              LocalDateTime.now().toString()
          )

          return ResponseEntity.ok(logoutResponse)
      }



    /**
     * Endpoint que simula la actualización de la información del usuario.
     *
     * Permite modificar correo y contraseña.
     *
     * URL:    http://localhost:8080/usuarios
     * Metodo: PUT
     *
     * @param updateUsuarioRequest DTO con los nuevos datos.
     * @return ResponseEntity con el usuario actualizado.
     */
    @PutMapping
    fun updateInfoUsuario(
        @RequestBody updateUsuarioRequest: UpdateUsuarioRequest
    ): ResponseEntity<Any> {

        /**
         *   Se espera un JSON de la forma:
         *   {
         *      "nombre": "Nombre",
         *      "email": "alguien@ciencias.unam.mx",
         *      "codigoPostal": "00000",
         *      "password": "myPassword"
         *  }
         */
        // Simulación del usuario encontrado en el sistema
        val usuarioFake = Usuario(
            id = "123",
            nombre = "Usuario1",
            email = "usuario1@ciencias.unam.mx",
            codigoPostal = "04510"
        )

        logger.info("usuario encontrado: $usuarioFake")
        logger.info("Info a actualizar: $updateUsuarioRequest")

        // Simulación de actualización de datos
        usuarioFake.nombre = updateUsuarioRequest.nombre
        usuarioFake.email = updateUsuarioRequest.email
        usuarioFake.codigoPostal = updateUsuarioRequest.codigoPostal
        usuarioFake.password = updateUsuarioRequest.password

        return ResponseEntity.ok(usuarioFake)
    }
}
