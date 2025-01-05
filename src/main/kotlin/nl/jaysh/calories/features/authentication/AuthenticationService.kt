package nl.jaysh.calories.features.authentication

import nl.jaysh.calories.core.data.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class AuthenticationService(private val repository: UserRepository) {

}