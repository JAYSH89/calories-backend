package nl.jaysh.calories.features.journal

import nl.jaysh.calories.core.configuration.PathConfig.BASE_PATH_V1
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("$BASE_PATH_V1/journal")
class JournalController(private val journalService: JournalService)
