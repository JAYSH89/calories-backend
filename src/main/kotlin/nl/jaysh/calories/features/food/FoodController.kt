package nl.jaysh.calories.features.food

import nl.jaysh.calories.core.configuration.PathConfig.BASE_PATH_V1
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("$BASE_PATH_V1/food")
class FoodController(private val foodService: FoodService) {

}
