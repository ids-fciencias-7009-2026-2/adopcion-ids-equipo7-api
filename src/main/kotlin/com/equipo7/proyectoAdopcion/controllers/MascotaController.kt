    package com.equipo7.proyectoAdopcion.controllers

    import com.equipo7.proyectoAdopcion.dto.request.CreateMascotaRequest
    import com.equipo7.proyectoAdopcion.dto.response.MascotaResponse
    import com.equipo7.proyectoAdopcion.repository.MascotaMapper
    import com.equipo7.proyectoAdopcion.services.MascotaService
    import org.springframework.http.HttpStatus
    import org.springframework.http.ResponseEntity
    import org.springframework.web.bind.annotation.*

    @RestController
    @RequestMapping("/mascotas")
    class MascotaController(
        private val mascotaService: MascotaService,
        private val mascotaMapper: MascotaMapper
    ) {

        // Endpoint para CU1
        @PostMapping("/publicar")
        fun publicarMascota(@RequestBody request: CreateMascotaRequest): ResponseEntity<MascotaResponse> {
            val dominio = mascotaMapper.requestToDomain(request)
            val mascotaGuardada = mascotaService.publicarMascota(dominio)
            val response = mascotaMapper.domainToResponse(mascotaGuardada)

            return ResponseEntity.status(HttpStatus.CREATED).body(response)
        }

        // Endpoint para CU3
        @GetMapping("/detalle/{id}")
        fun obtenerDetalle(@PathVariable id: Long): ResponseEntity<Any> {
            val mascota = mascotaService.obtenerDetalleMascota(id)

            return if (mascota != null) {
                val response = mascotaMapper.domainToResponse(mascota)
                ResponseEntity.ok(response)
            } else {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("mensaje" to "Publicación no encontrada"))
            }
        }

        @GetMapping
        fun listarPublicaciones(
            @RequestParam(required = false) nombre: String?,
            @RequestParam(required = false) filtro: String?,
            @RequestHeader("Authorization", required = false) token: String?
        ): ResponseEntity<Any> {
            return try {
                val publicaciones = mascotaService.listarPublicaciones(nombre, filtro, token)
                .map { mascotaMapper.domainToResponse(it) }

            ResponseEntity.ok(publicaciones)
        } catch (e: SecurityException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(mapOf("error" to e.message))
            }
        }

        @PostMapping("/{id}/interes")
        fun registrarInteres(
            @PathVariable id: Long,
            @RequestHeader("Authorization", required = false) token: String?
        ): ResponseEntity<Any> {
            return try {
                val response = mascotaService.registrarInteres(id, token)
                ResponseEntity.status(HttpStatus.CREATED).body(response)
            } catch (e: SecurityException) {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(mapOf("error" to e.message))
            } catch (e: NoSuchElementException) {
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(mapOf("error" to e.message))
            } catch (e: IllegalStateException) {
                ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(mapOf("error" to e.message))
            }
        }

        @DeleteMapping("/{id}")
        fun eliminarPublicacion(
            @PathVariable id: Long,
            @RequestHeader("Authorization", required = false) token: String?
        ): ResponseEntity<Any> {
            return try {
                mascotaService.eliminarPublicacion(id, token)

                ResponseEntity.ok(
                    mapOf("mensaje" to "Publicación eliminada correctamente")
                )
            } catch (e: SecurityException) {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(mapOf("mensaje" to e.message))
            } catch (e: NoSuchElementException) {
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(mapOf("mensaje" to e.message))
            } catch (e: IllegalAccessException) {
                ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(mapOf("mensaje" to e.message))
            } catch (e: IllegalStateException) {
                ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(mapOf("mensaje" to e.message))
            }
        }

        @PutMapping("/editar/{id}")
        fun editarPublicacion(
            @PathVariable id: Long,
            @RequestBody request: CreateMascotaRequest,
            @RequestHeader("Authorization", required = false) token: String?
        ): ResponseEntity<Any> {
            return try {
                val mascotaModificada = mascotaMapper.requestToDomain(request)

                val mascotaActualizada = mascotaService.editarPublicacion(id, mascotaModificada, token)

                val response = mascotaMapper.domainToResponse(mascotaActualizada)

                ResponseEntity.ok(response)
            } catch (e: SecurityException) {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("mensaje" to e.message))
            } catch (e: NoSuchElementException) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("mensaje" to e.message))
            } catch (e: IllegalAccessException) {
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(mapOf("mensaje" to e.message))
            } catch (e: IllegalStateException) {
                ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("mensaje" to e.message))
            } catch (e: Exception) {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(mapOf("mensaje" to "Error interno del servidor"))
            }
        }

        @PutMapping("/{id}/adoptar")
        fun confirmarAdopcion(
            @PathVariable id: Long,
            @RequestHeader("Authorization", required = false) token: String?
        ): ResponseEntity<Any> {
            return try {
                val mascotaAdoptada = mascotaService.marcarComoAdoptada(id, token)

                val response = mascotaMapper.domainToResponse(mascotaAdoptada)

                ResponseEntity.ok(response)
            } catch (e: SecurityException) {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("mensaje" to e.message))
            } catch (e: NoSuchElementException) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("mensaje" to e.message))
            } catch (e: IllegalAccessException) {
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(mapOf("mensaje" to e.message))
            } catch (e: IllegalStateException) {
                ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("mensaje" to e.message))
            } catch (e: Exception) {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(mapOf("mensaje" to "Error interno del servidor"))
            }
        }
    }
