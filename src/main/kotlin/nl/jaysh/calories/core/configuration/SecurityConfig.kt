package nl.jaysh.calories.core.configuration

import nl.jaysh.calories.core.configuration.PathConfig.BASE_PATH_V1
import nl.jaysh.calories.core.data.repository.UserRepository
import nl.jaysh.calories.features.security.CaloriesUserDetailsService
import nl.jaysh.calories.features.security.JwtAuthenticationFilter
import nl.jaysh.calories.features.security.TokenService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.lang.Exception

@Configuration
class SecurityConfig {

  @Bean
  fun jwtAuthenticationFilter(service: TokenService) = JwtAuthenticationFilter(service)

  @Bean
  fun userDetailsService(repository: UserRepository): UserDetailsService = CaloriesUserDetailsService(repository)

  @Bean
  @Throws(Exception::class)
  fun securityFilterChain(
    httpSecurity: HttpSecurity,
    jwtAuthenticationFilter: JwtAuthenticationFilter,
  ): SecurityFilterChain = httpSecurity
    .authorizeHttpRequests { auth ->
      auth
        .requestMatchers(HttpMethod.POST, "$BASE_PATH_V1/auth/**").permitAll()
        .anyRequest().authenticated()
    }
    .csrf { csrf -> csrf.disable() }
    .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
    .build()

  @Bean
  fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

  @Bean
  fun authenticationManager(conf: AuthenticationConfiguration): AuthenticationManager = conf.authenticationManager
}
