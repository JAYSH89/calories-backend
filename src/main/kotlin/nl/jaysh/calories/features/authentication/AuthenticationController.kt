package nl.jaysh.calories.features.authentication

import nl.jaysh.calories.core.configuration.PathConfig.BASE_PATH_V1
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("$BASE_PATH_V1/auth")
class AuthenticationController(private val service: AuthenticationService) {

  @PostMapping("/register")
  fun register() {
    service.register()
  }

  @PostMapping("/login")
  fun login() {
    service.login()
  }

  @PostMapping("/refresh")
  fun refresh() {
    service.refresh()
  }
}
