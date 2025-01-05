package nl.jaysh.calories.features.dashboard

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import

@WebMvcTest(controllers = [DashboardController::class])
@Import(DashboardService::class)
class DashboardControllerTest
