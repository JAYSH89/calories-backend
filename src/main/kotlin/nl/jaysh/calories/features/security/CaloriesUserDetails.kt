package nl.jaysh.calories.features.security

import nl.jaysh.calories.core.model.user.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

class CaloriesUserDetails(private val user: User) : UserDetails {

  fun getId(): UUID = user.id

  override fun getUsername() = user.email

  override fun getPassword() = user.password

  override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_USER"))
}
