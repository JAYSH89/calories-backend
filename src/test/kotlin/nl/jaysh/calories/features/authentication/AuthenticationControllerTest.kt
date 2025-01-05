package nl.jaysh.calories.features.authentication

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import

@WebMvcTest(controllers = [AuthenticationController::class])
@Import(AuthenticationService::class)
class AuthenticationControllerTest
