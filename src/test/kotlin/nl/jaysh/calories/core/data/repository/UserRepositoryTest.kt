package nl.jaysh.calories.core.data.repository

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import nl.jaysh.calories.core.data.local.dao.UserDao
import nl.jaysh.calories.core.model.user.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class UserRepositoryTest {

  private lateinit var dao: UserDao
  private lateinit var repository: UserRepository

  @BeforeEach
  fun setup() {
    dao = mockk()
    repository = UserRepository(dao)
  }

  @Test
  fun insert() {
    every { dao.insert(email = any(), password = any()) } returns getTestUser()
    val email = "test@example.com"
    val password = "testPass123$"

    repository.insert(email = email, password = password)

    verify { dao.insert(email = email, password = password) }
  }

  @Test
  fun delete() {
    every { dao.delete(id = any(), email = any(), password = any()) } just Runs
    val id = UUID.fromString("F0406299-65DA-4660-9653-5AA44CAA1156")
    val email = "test@example.com"
    val password = "testPass123$"

    repository.delete(id = id, email = email, password = password)

    verify { dao.delete(id = id, email = email, password = password) }
  }

  @Test
  fun update() {
    every {
      dao.update(id = any(), email = any(), password = any(), newEmail = any(), newPassword = any())
    } returns getTestUser()

    val id = UUID.fromString("F0406299-65DA-4660-9653-5AA44CAA1156")
    val email = "test@example.com"
    val password = "testPass123$"
    val newEmail = "updatedTest@example.com"
    val newPassword = "updatedPass123$"

    repository.update(id = id, email = email, password = password, newEmail = newEmail, newPassword = newPassword)

    verify { dao.update(id = id, email = email, password = password, newEmail = newEmail, newPassword = newPassword) }
  }

  fun getTestUser() = User(
    id = UUID.randomUUID(),
    email = "test@example.com",
    password = "testPass123$",
    createdAt = LocalDateTime.of(1990, 1, 1, 0, 0, 0, 0),
    updatedAt = LocalDateTime.of(1990, 1, 1, 0, 0, 0, 0),
  )
}
