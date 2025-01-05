package nl.jaysh.calories.features.journal

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import

@WebMvcTest(controllers = [JournalController::class])
@Import(JournalService::class)
class JournalControllerTest
