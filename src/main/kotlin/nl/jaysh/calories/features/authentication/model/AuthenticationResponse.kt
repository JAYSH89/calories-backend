package nl.jaysh.calories.features.authentication.model

data class AuthenticationResponse(
  val token: String,
  val expiresIn: Long,
)
