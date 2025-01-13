package nl.jaysh.calories.features.authentication

import nl.jaysh.calories.core.data.repository.UserRepository
import nl.jaysh.calories.features.authentication.model.AuthenticationResponse
import nl.jaysh.calories.features.security.TokenService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
class AuthenticationService(
  private val tokenService: TokenService,
  private val repository: UserRepository,
  private val passwordEncoder: PasswordEncoder,
) {

  fun register(email: String, password: String): AuthenticationResponse {
    require(isValidEmail(email)) { "not a valid email address" }
    require(isValidPassword(password)) { "not a valid password" }

    repository.insert(email, passwordEncoder.encode(password))

    val userDetails = tokenService.authenticate(email, password)
    val token = tokenService.generateToken(userDetails)

    return AuthenticationResponse(token = token, expiresIn = 3600)
  }

  fun login(email: String, password: String): AuthenticationResponse {
    require(isValidEmail(email)) { "not a valid email address" }
    require(isValidPassword(password)) { "not a valid password" }

    val userDetails = tokenService.authenticate(email, password)
    val token = tokenService.generateToken(userDetails)

    return AuthenticationResponse(token, 3600)
  }

  fun refresh(token: String) {
  }

  private fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    val pattern: Pattern = Pattern.compile(emailRegex)
    return pattern.matcher(email).matches()
  }

  private fun isValidPassword(password: String) =
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
