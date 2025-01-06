package nl.jaysh.calories.features.authentication

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import nl.jaysh.calories.core.data.repository.UserRepository
import nl.jaysh.calories.core.model.user.User
import nl.jaysh.calories.features.authentication.model.AuthenticationResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.LocalDateTime
import java.util.*

class AuthenticationServiceTest {

  private lateinit var repository: UserRepository
  private lateinit var service: AuthenticationService

  private val testUser = User(
    id = UUID.fromString("79F57949-05FA-4101-B094-6C1C97121B88"),
    email = "test@example.com",
    password = "testPass123$",
    createdAt = LocalDateTime.of(1990, 1, 1, 0, 0, 0, 0),
    updatedAt = LocalDateTime.of(1990, 1, 1, 0, 0, 0, 0),
  )

  @BeforeEach
  fun setUp() {
    repository = mockk()
    service = AuthenticationService(repository)
  }

  @Test
  fun `register successful`() {
    every { repository.insert(email = any(), password = any()) } returns testUser

    val validEmail = "test@example.com"
    val validPassword = "testPass123$"
    val result = service.register(email = validEmail, password = validPassword)

    val expected = AuthenticationResponse(id = testUser.id.toString(), email = validEmail)
    assertThat(result.id).isEqualTo(expected.id)
    assertThat(result.email).isEqualTo(expected.email)

    verify { repository.insert(email = validEmail, password = validPassword) }
  }

  @ParameterizedTest
  @CsvSource(
    "plainaddress",
    "missingatsign.com",
    "@missingusername.com",
    "username@.com",
    "username@domain.c",
    "'',",
    "'         '",
  )
  fun `register should validate email format`(email: String) {
    every { repository.insert(email = any(), password = any()) } returns testUser

    val validPassword = "testPass123$"

    val exception = assertThrows<IllegalArgumentException> {
      service.register(email = email, password = validPassword)
    }

    assertThat(exception.message).isEqualTo("not a valid email address")

    verify(exactly = 0) { repository.insert(email = email, password = validPassword) }
  }

  @ParameterizedTest
  @CsvSource(
    "thispasswordcontainsonlycharacters",
    "Testpass$$$$",
    "Testpass123",
    "testpass123$",
    "Te1$",
    "'',",
    "'         '",
  )
  fun `register should validate password format`(password: String) {
    every { repository.insert(email = any(), password = any()) } returns testUser

    val validEmail = "test@example.com"

    val exception = assertThrows<IllegalArgumentException> {
      service.register(email = validEmail, password = password)
    }

    assertThat(exception.message).isEqualTo("not a valid password")

    verify(exactly = 0) { repository.insert(email = validEmail, password = password) }
  }

  @Test
  fun `login successful`() {
    every { repository.get(email = any(), password = any()) } returns testUser

    val validEmail = "test@example.com"
    val validPassword = "testPass123$"
    val result = service.login(email = validEmail, password = validPassword)

    val expected = AuthenticationResponse(id = testUser.id.toString(), email = validEmail)
    assertThat(result.id).isEqualTo(expected.id)
    assertThat(result.email).isEqualTo(expected.email)

    verify(exactly = 1) { repository.get(email = validEmail, password = validPassword) }
  }

  @ParameterizedTest
  @CsvSource(
    "plainaddress",
    "missingatsign.com",
    "@missingusername.com",
    "username@.com",
    "username@domain.c",
    "'',",
    "'         '",
  )
  fun `login should validate email format`(email: String) {
    every { repository.get(email = any(), password = any()) } returns testUser

    val validPassword = "testPass123$"

    val exception = assertThrows<IllegalArgumentException> {
      service.login(email = email, password = validPassword)
    }

    assertThat(exception.message).isEqualTo("not a valid email address")

    verify(exactly = 0) { repository.get(email = email, password = validPassword) }
  }

  @ParameterizedTest
  @CsvSource(
    "thispasswordcontainsonlycharacters",
    "Testpass$$$$",
    "Testpass123",
    "testpass123$",
    "Te1$",
    "'',",
    "'         '",
  )
  fun `login should validate password format`(password: String) {
    every { repository.get(email = any(), password = any()) } returns testUser

    val validEmail = "test@example.com"

    val exception = assertThrows<IllegalArgumentException> {
      service.login(email = validEmail, password = password)
    }

    assertThat(exception.message).isEqualTo("not a valid password")

    verify(exactly = 0) { repository.get(email = validEmail, password = password) }
  }

  @Test
  fun testRefreshToken() {
    val result = service.refresh(token = "")

    assertThat(result.refreshToken).isEqualTo("notImplemented")
    assertThat(result.accessToken).isEqualTo("notImplemented")
    assertThat(result.expiresIn).isEqualTo("3600")
  }
}
