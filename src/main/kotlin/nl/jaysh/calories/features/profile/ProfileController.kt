package nl.jaysh.calories.features.profile

import nl.jaysh.calories.core.configuration.PathConfig.BASE_PATH_V1
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("$BASE_PATH_V1/profile")
class ProfileController(private val profileService: ProfileService) {

}
