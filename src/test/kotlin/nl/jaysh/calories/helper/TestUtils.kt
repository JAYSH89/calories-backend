package nl.jaysh.calories.helper

import com.fasterxml.jackson.databind.ObjectMapper

fun Any.toJson(): String {
  val objectMapper = ObjectMapper()
  return objectMapper.writeValueAsString(this)
}
