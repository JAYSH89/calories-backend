package nl.jaysh.calories.features.authentication.model

data class AuthenticationRequest(
  val email: String,
  val password: String,
)
