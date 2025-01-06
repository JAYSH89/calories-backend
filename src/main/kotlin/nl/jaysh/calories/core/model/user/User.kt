package nl.jaysh.calories.core.model.user

import java.time.LocalDateTime
import java.util.*

data class User(
  val id: UUID,
  val email: String,
  val password: String,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
)
