package nl.jaysh.calories.features.security

import nl.jaysh.calories.core.data.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class CaloriesUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

  override fun loadUserByUsername(email: String): UserDetails {
    val user = userRepository.findByEmail(email) ?: throw UsernameNotFoundException("$email not found")
    return CaloriesUserDetails(user)
  }
}
