package nl.jaysh.calories.features.weight

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import

@WebMvcTest(controllers = [WeightController::class])
@Import(WeightService::class)
class WeightControllerTest
