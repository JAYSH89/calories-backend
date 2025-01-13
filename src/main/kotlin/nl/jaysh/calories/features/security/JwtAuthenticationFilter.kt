package nl.jaysh.calories.features.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(private val tokenService: TokenService) : OncePerRequestFilter() {

  @Throws(Throwable::class)
  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    filterChain: FilterChain,
  ) {
    try {
      val token = extractToken(request)

      if (token != null) {
        val userDetails = tokenService.validateToken(token)
        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

        SecurityContextHolder.getContext().authentication = authentication

        if (userDetails is CaloriesUserDetails) request.setAttribute("userId", userDetails.getId())
      }
    } catch (e: Exception) {
      println("received invalid token")
    }

    filterChain.doFilter(request, response)
  }

  private fun extractToken(request: HttpServletRequest): String? {
    val authHeader = request.getHeader("Authorization")

    return if (authHeader != null && authHeader.startsWith("Bearer ")) {
      authHeader.substring(7)
    } else {
      null
    }
  }
}
