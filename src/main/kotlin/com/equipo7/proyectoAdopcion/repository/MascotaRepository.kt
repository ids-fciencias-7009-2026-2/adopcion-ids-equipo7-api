package com.equipo7.proyectoAdopcion.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MascotaRepository : JpaRepository<MascotaEntity, Long>