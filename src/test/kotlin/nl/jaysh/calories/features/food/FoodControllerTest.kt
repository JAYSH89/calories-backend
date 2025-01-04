package nl.jaysh.calories.features.food

import nl.jaysh.calories.features.profile.ProfileService
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import

@WebMvcTest(controllers = [FoodController::class])
@Import(ProfileService::class)
class FoodControllerTest {

}
