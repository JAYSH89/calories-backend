package nl.jaysh.calories

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CaloriesApplication

fun main(args: Array<String>) {
	runApplication<CaloriesApplication>(*args)
}
