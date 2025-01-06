package nl.jaysh.calories.features.authentication

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import nl.jaysh.calories.core.configuration.PathConfig.BASE_PATH_V1
import nl.jaysh.calories.core.model.user.User
import nl.jaysh.calories.features.authentication.model.AuthenticationRequest
import nl.jaysh.calories.features.authentication.model.AuthenticationResponse
import nl.jaysh.calories.features.authentication.model.RefreshRequest
import nl.jaysh.calories.features.authentication.model.RefreshResponse
import nl.jaysh.calories.helper.toJson
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.util.UUID

@WebMvcTest(controllers = [AuthenticationController::class])
@Import(AuthenticationService::class)
class AuthenticationControllerTest {

  @Autowired
  private lateinit var mockMvc: MockMvc

  @MockkBean
  private lateinit var service: AuthenticationService

  private val testRefreshResponse = RefreshResponse(
    refreshToken = "notImplemented",
    accessToken = "notImplemented",
    expiresIn = "3600",
  )

  private val testUser = User(
    id = UUID.fromString("F0406299-65DA-4660-9653-5AA44CAA1156"),
    email = "test@example.com",
    password = "testPass123$",
    createdAt = LocalDateTime.of(1990, 1, 1, 0, 0, 0, 0),
    updatedAt = LocalDateTime.of(1990, 1, 1, 0, 0, 0, 0),
  )

  private val testAuthenticationRequest = AuthenticationRequest(email = testUser.email, password = testUser.password)

  private val testAuthenticationResponse = AuthenticationResponse(id = testUser.id.toString(), email = testUser.email)

  @Test
  fun `register valid credential should 200 OK`() {
    every { service.register(email = any(), password = any()) } returns testAuthenticationResponse

    mockMvc.perform(
      post("$BASE_PATH_V1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(testAuthenticationRequest.toJson()),
    )
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.id").value(testUser.id.toString()))
      .andExpect(jsonPath("$.email").value(testUser.email))

    verify(exactly = 1) { service.register(email = testUser.email, password = testUser.password) }
  }

  @Test
  fun `register invalid email should 500 BAD REQUEST`() {
    val exception = IllegalArgumentException("not a valid email address")
    every { service.register(email = any(), password = any()) } throws exception

    val request = testAuthenticationRequest.copy(email = "invalidEmail")
    mockMvc.perform(
      post("$BASE_PATH_V1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(request.toJson()),
    ).andExpect(status().isBadRequest)

    verify(exactly = 1) { service.register(email = request.email, password = request.password) }
  }

  @Test
  fun `register invalid password should 500 BAD REQUEST`() {
    val exception = IllegalArgumentException("not a valid password")
    every { service.register(email = any(), password = any()) } throws exception

    val request = testAuthenticationRequest.copy(password = "invalidPassword")
    mockMvc.perform(
      post("$BASE_PATH_V1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(request.toJson()),
    ).andExpect(status().isBadRequest)

    verify(exactly = 1) { service.register(email = request.email, password = request.password) }
  }

  @Test
  fun `login valid credential should 200 OK`() {
    every { service.login(email = any(), password = any()) } returns testAuthenticationResponse

    mockMvc.perform(
      post("$BASE_PATH_V1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(testAuthenticationRequest.toJson()),
    )
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.id").value(testUser.id.toString()))
      .andExpect(jsonPath("$.email").value(testUser.email))

    verify(exactly = 1) { service.login(testAuthenticationRequest.email, testAuthenticationRequest.password) }
  }

  @Test
  fun `login invalid email should 500 BAD REQUEST`() {
    val exception = IllegalArgumentException("not a valid email address")
    every { service.login(email = any(), password = any()) } throws exception

    val request = testAuthenticationRequest.copy(email = "invalidEmail")
    mockMvc.perform(
      post("$BASE_PATH_V1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(request.toJson()),
    ).andExpect(status().isBadRequest)

    verify(exactly = 1) { service.login(email = request.email, password = request.password) }
  }

  @Test
  fun `login invalid password should 500 BAD REQUEST`() {
    val exception = IllegalArgumentException("not a valid password")
    every { service.login(email = any(), password = any()) } throws exception

    val request = testAuthenticationRequest.copy(password = "invalidPassword")
    mockMvc.perform(
      post("$BASE_PATH_V1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(request.toJson()),
    ).andExpect(status().isBadRequest)

    verify(exactly = 1) { service.login(email = request.email, password = request.password) }
  }

  @Test
  fun `refresh valid credential should 200 OK`() {
    every { service.refresh(token = any()) } returns testRefreshResponse

    val request = RefreshRequest(token = "test")

    mockMvc.perform(
      post("$BASE_PATH_V1/auth/refresh")
        .contentType(MediaType.APPLICATION_JSON)
        .content(request.toJson()),
    )
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.refreshToken").value(testRefreshResponse.refreshToken))
      .andExpect(jsonPath("$.accessToken").value(testRefreshResponse.accessToken))
      .andExpect(jsonPath("$.expiresIn").value(testRefreshResponse.expiresIn))

    verify { service.refresh(token = request.token) }
  }
}
