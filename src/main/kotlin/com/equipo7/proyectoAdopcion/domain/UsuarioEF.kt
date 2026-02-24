package com.equipo7.proyectoAdopcion.domain

import com.equipo7.proyectoAdopcion.dto.request.CreateUsuarioRequest
import java.util.UUID

/**
 * Extension Function que convierte un DTO de tipo CreateUsuarioRequest
 * en un objeto de dominio Usuario.
 *
 * ---------------------------------------------------------
 * ¿Qué es una Extension Function?
 * ---------------------------------------------------------
 *
 * Es una característica de Kotlin que permite "agregar"
 * funciones a una clase existente sin modificar su código original.
 *
 * En este caso, estamos agregando la función toUsuario()
 * al DTO CreateUsuarioRequest.
 *
 * Esto nos permite escribir:
 *
 *     createUsuarioRequest.toUsuario()
 *
 * en lugar de:
 *
 *     Usuario(...)
 *
 * ---------------------------------------------------------
 * ¿Por qué hacemos esta conversión?
 * ---------------------------------------------------------
 *
 * Porque un DTO y un objeto de dominio NO son lo mismo:
 *
 * - El DTO representa datos que vienen del cliente (JSON).
 * - El objeto de dominio representa el modelo interno del sistema.
 *
 * Separarlos nos permite:
 *  - Mantener arquitectura limpia.
 *  - No mezclar lógica con transporte de datos.
 *  - Cambiar la estructura interna sin afectar la API externa.
 *
 * ---------------------------------------------------------
 * ¿Qué hace esta función?
 * ---------------------------------------------------------
 *
 * 1. Genera un identificador único simulado.
 * 2. Crea un objeto Usuario usando los datos del DTO.
 *
 * Nota: En un sistema real, el ID lo generaría la base de datos.
 */
fun CreateUsuarioRequest.toUsuario(): Usuario {

    // Generamos un identificador único de forma simulada
    val id = "id-random-" + UUID.randomUUID().toString()

    // Creamos el objeto de dominio usando los datos del DTO
    return Usuario(
        id = id,
        nombre = this.nombre,
        email = this.email,
        codigoPostal = this.codigoPostal
    )
}