package nl.jaysh.calories.features.authentication

import nl.jaysh.calories.core.data.repository.UserRepository
import nl.jaysh.calories.features.authentication.model.AuthenticationResponse
import nl.jaysh.calories.features.authentication.model.RefreshResponse
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
class AuthenticationService(private val repository: UserRepository) {

  fun register(email: String, password: String): AuthenticationResponse {
    require(isValidEmail(email = email)) { "not a valid email address" }
    require(isValidPassword(password = password)) { "not a valid password" }

    val result = repository.insert(email = email, password = password)
    return AuthenticationResponse(id = result.id.toString(), email = result.email)
  }

  fun login(email: String, password: String): AuthenticationResponse {
    require(isValidEmail(email = email)) { "not a valid email address" }
    require(isValidPassword(password = password)) { "not a valid password" }

    val result = repository.get(email = email, password = password)
    return AuthenticationResponse(id = result.id.toString(), email = result.email)
  }

  fun refresh(token: String): RefreshResponse = RefreshResponse(
    refreshToken = "notImplemented",
    accessToken = "notImplemented",
    expiresIn = "3600",
  )

  private fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    val pattern: Pattern = Pattern.compile(emailRegex)
    return pattern.matcher(email).matches()
  }

  private fun isValidPassword(password: String): Boolean =
    password.isNotBlank() && password.validPasswordLength && password.containsSpecialCharacter && password.containsNumber && password.containsUppercaseCharacter

  private val String.containsSpecialCharacter: Boolean
    get() {
      val specialCharacters = " !\"#\$%&'()*+,-./:;<=>?@[\\]^_`{|}~"
      return this.any { it in specialCharacters }
    }

  private val String.containsUppercaseCharacter: Boolean
    get() = this.any { it.isUpperCase() }

  private val String.containsNumber: Boolean
    get() = any { it.isDigit() }

  private val String.validPasswordLength: Boolean
    get() = length in 6..32
}
