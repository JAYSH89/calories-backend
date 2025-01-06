package nl.jaysh.calories.features.authentication

import nl.jaysh.calories.core.configuration.PathConfig.BASE_PATH_V1
import nl.jaysh.calories.features.authentication.model.AuthenticationRequest
import nl.jaysh.calories.features.authentication.model.AuthenticationResponse
import nl.jaysh.calories.features.authentication.model.RefreshRequest
import nl.jaysh.calories.features.authentication.model.RefreshResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("$BASE_PATH_V1/auth")
class AuthenticationController(private val service: AuthenticationService) {

  @PostMapping("/register")
  fun register(@RequestBody request: AuthenticationRequest): AuthenticationResponse = service.register(email = request.email, password = request.password)

  @PostMapping("/login")
  fun login(@RequestBody request: AuthenticationRequest): AuthenticationResponse = service.login(email = request.email, password = request.password)

  @PostMapping("/refresh")
  fun refresh(@RequestBody request: RefreshRequest): RefreshResponse = service.refresh(request.token)
}
