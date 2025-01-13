package nl.jaysh.calories.features.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import kotlin.collections.HashMap

@Service
class TokenService(
  private val authenticationManager: AuthenticationManager,
  private val userDetailsService: UserDetailsService,
) {

  @Value("\${jwt.secret}")
  private lateinit var secretKey: String

  private val jwtExpirationMillis = 86_400_000L

  private val signingKey: Key
    get() = Keys.hmacShaKeyFor(secretKey.toByteArray())

  fun authenticate(email: String, password: String): UserDetails {
    val auth = UsernamePasswordAuthenticationToken(email, password)
    authenticationManager.authenticate(auth)

    return userDetailsService.loadUserByUsername(email)
  }

  fun generateToken(userDetails: UserDetails): String = Jwts.builder()
    .claims(HashMap<String, Any>())
    .subject(userDetails.username)
    .issuedAt(Date(System.currentTimeMillis()))
    .expiration(Date(System.currentTimeMillis() + jwtExpirationMillis))
    .signWith(signingKey)
    .compact()

  fun validateToken(token: String): UserDetails {
    val username = extractUsername(token)
    return userDetailsService.loadUserByUsername(username)
  }

  private fun extractUsername(token: String): String = Jwts.parser()
    .setSigningKey(secretKey)
    .build()
    .parseClaimsJws(token)
    .body
    .subject
}
