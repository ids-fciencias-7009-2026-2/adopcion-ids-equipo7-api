package com.equipo7.proyectoAdopcion

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProyectoAdopcionApplication

fun main(args: Array<String>) {
	dotenv().entries().forEach {
		System.setProperty(it.key, it.value)
	}
	runApplication<ProyectoAdopcionApplication>(*args)
}