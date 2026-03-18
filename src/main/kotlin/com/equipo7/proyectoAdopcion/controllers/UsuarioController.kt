package com.equipo7.proyectoAdopcion.controllers


import com.equipo7.proyectoAdopcion.domain.Usuario
import com.equipo7.proyectoAdopcion.dto.request.LoginRequest
import com.equipo7.proyectoAdopcion.dto.response.LogoutResponse
import com.equipo7.proyectoAdopcion.dto.response.LoginResponse
import com.equipo7.proyectoAdopcion.dto.request.CreateUsuarioRequest
import com.equipo7.proyectoAdopcion.dto.request.UpdateUsuarioRequest
import com.equipo7.proyectoAdopcion.domain.toUsuario
import com.equipo7.proyectoAdopcion.services.UsuarioService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

/**
 * Controlador para la gestión de usuarios del sistema de adopción.
 */
@RestController
@RequestMapping("/usuarios") // Prefijo base para todos los endpoints de este controlador
class UsuarioController {
    @Autowired
    lateinit var usuarioService: UsuarioService
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
     * URL: http://localhost:8080/usuarios/me
     * Metodo: GET
     *
     * @return ResponseEntity con un objeto Usuario y código HTTP 200 (OK). ResponseEntity<Any> con el usuario encontrado sin contraseña
     *         HTTP 401 si el usuario no cuenta con un token activo.
     */
    @GetMapping("/me")
    fun retrieveUsuario(
        @RequestHeader("Authorization", required = false) token: String?
    ): ResponseEntity<Any> {
      if (token.isNullOrBlank()) {
          return ResponseEntity.status(401).body(mapOf("error" to "Token no proporcionado"))
      }
      val usuario = usuarioService.findByToken(token)
      return if (usuario != null) {
        //se oculta la contraseña en la respuesta para noexponer datos sensibles del usuario
        ResponseEntity.ok(usuario.copy(password = null))
      } else {
          ResponseEntity.status(401).body(mapOf("error" to "Token no encontrado. Inicie sesion."))
      }
    }

    /**
     * Endpoint encargado de registrar un nuevo usuario.
     *
     * Recibe un JSON con los datos necesarios para crear el usuario
     * y los transforma en un objeto de dominio.
     *
     * URL:    http://localhost:8080/usuarios/register
     * Metodo: POST
     * Si el registro es exitoso, devuelve el usuario sin contraseña.
     *
     * @param createUsuarioRequest DTO que representa el body del request.
     * @return ResponseEntity con el usuario creado sin contraseña, o un mensaje de error si el correo ya existe, y código HTTP 200 (OK).
     */
    @PostMapping("/register")
    fun agregaUsuario(
        @RequestBody createUsuarioRequest: CreateUsuarioRequest
    ): ResponseEntity<Any> {
        //Conversion de DTO a objeto de dominio usando una estension function
        val usuarioParaAgregar = createUsuarioRequest.toUsuario()
        //Logica del registro, validación y hash de contraseña
        val usuarioGuardado = usuarioService.addNewUsuario(usuarioParaAgregar)

    return if (usuarioGuardado != null) {
        //No se expone la contraseña ni su hash en la respuesta HTTP
        ResponseEntity.ok(usuarioGuardado.copy(password = null))
    } else {
        //Si el correo ya existe, evita registrps duplicados
        ResponseEntity.status(409).body(mapOf("error" to "El correo ya está registrado"))
    }

    }

    /**
    * Endpoint que hace el proceso de autenticación del usuario
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
       logger.info("try make login with: $loginRequest")
       val authenticatedUsuario = usuarioService.authenticate(loginRequest)
       return if (authenticatedUsuario != null && authenticatedUsuario.token != null){
           logger.info("Login successful")
           // HTTP 200 → autenticación exitosa
           ResponseEntity.ok(mapOf("message" to "Welcome", "userId" to authenticatedUsuario.id,
               "token" to authenticatedUsuario.token))
       } else {
           logger.error("Login failed for: $loginRequest")
           // HTTP 401 → Unauthorized (credenciales inválidas)
           ResponseEntity.status(401).body(mapOf("error" to "Invalid Credentials"))
       }
   }

   /**
       * Endpoint que cierra la sesión del usuario.
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
      fun logout(@RequestHeader("Authorization", required = false) token: String?): ResponseEntity<Any> {
            if (token.isNullOrBlank()) {
                return ResponseEntity.status(401).body(mapOf("error" to "Token no proporcionado"))
            }
            // Si el usuario es nulo (no se encuentra por token), mandamos error
            val usuario = usuarioService.findByToken(token) ?: return ResponseEntity.status(401).body(mapOf("error" to "Token inválido"))
            // De lo contrario, borramos el token y realizamos el logout
            usuarioService.logout(token)
            logger.info("Cierre de sesión solicitado para el usuario ID: ${usuario.id}")

      val logoutResponse = LogoutResponse(
          userId = usuario.id,
          logoutDateTime = LocalDateTime.now().toString()
          )
      return ResponseEntity.ok(logoutResponse)
      }



    /**
     * Endpoint que  actualiza la información del usuario.
     *
     * Permite modificar correo y contraseña.
     *
     * URL:    http://localhost:8080/usuarios
     * Metodo: PUT
     *
     * @param updateUsuarioRequest DTO con los nuevos datos.
     * @return ResponseEntity con el usuario actualizado.
     */
    @PutMapping("/{email}")
    fun updateInfoUsuario(
        @PathVariable email: String,
        @RequestBody updateUsuarioRequest: UpdateUsuarioRequest
    ): ResponseEntity<Any> {
    logger.info("Solicitud de actualización para el usuario: $email")
        val usuarioActualizado = usuarioService.updateUsuario(email, updateUsuarioRequest)

        return if (usuarioActualizado != null) {
            //Aunque se actulice la contraseña, no se devuelve en la respuesta
            logger.info("Usuario $email actualizado con éxito")
            ResponseEntity.ok(usuarioActualizado.copy(password = null))
        } else {
            logger.error("No se pudo encontrar al usuario $email para actualizar")
            ResponseEntity.status(404).body(mapOf("error" to "Usuario no encontrado"))
        }
    }
}
