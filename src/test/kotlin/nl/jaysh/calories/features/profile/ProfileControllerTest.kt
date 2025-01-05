package nl.jaysh.calories.features.profile

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import

@WebMvcTest(controllers = [ProfileController::class])
@Import(ProfileService::class)
class ProfileControllerTest
