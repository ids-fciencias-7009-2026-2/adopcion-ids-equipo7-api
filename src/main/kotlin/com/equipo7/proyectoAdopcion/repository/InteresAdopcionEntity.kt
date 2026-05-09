package com.equipo7.proyectoAdopcion.repository

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "intereses_adopcion",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["usuario_id", "animal_id"])
    ]
)
data class InteresAdopcionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interes_id")
    val interesId: Long? = null,

    @Column(name = "usuario_id", nullable = false)
    val usuarioId: String = "",

    @Column(name = "animal_id", nullable = false)
    val animalId: Long = 0,

    @Column(name = "fecha_interes", nullable = false)
    val fechaInteres: LocalDateTime = LocalDateTime.now()
)