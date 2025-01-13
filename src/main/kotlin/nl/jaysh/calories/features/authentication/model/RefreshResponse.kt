package nl.jaysh.calories.features.authentication.model

data class RefreshResponse(
  val refreshToken: String,
  val accessToken: String,
  val expiresIn: Long,
)
