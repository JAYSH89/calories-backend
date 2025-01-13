package nl.jaysh.calories.features.authentication

import nl.jaysh.calories.core.configuration.PathConfig.BASE_PATH_V1
import nl.jaysh.calories.features.authentication.model.AuthenticationRequest
import nl.jaysh.calories.features.authentication.model.RefreshRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("$BASE_PATH_V1/auth")
class AuthenticationController(private val service: AuthenticationService) {

  @PostMapping("/register")
  fun register(@RequestBody request: AuthenticationRequest) = ResponseEntity.ok(
    service.register(request.email, request.password),
  )

  @PostMapping("/login")
  fun login(@RequestBody request: AuthenticationRequest) = ResponseEntity.ok(
    service.login(request.email, request.password),
  )

  @PostMapping("/refresh")
  fun refresh(@RequestBody request: RefreshRequest) = ResponseEntity.ok(
    service.refresh(request.token),
  )
}
