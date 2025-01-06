package nl.jaysh.calories.core.data.local.entities

import nl.jaysh.calories.core.data.local.table.UserTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserEntityTest {

  private val testEmail = "test@example.com"
  private val testPassword = "testPass123$"

  @BeforeEach
  fun setup() {
    Database.connect(
      url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
      driver = "org.h2.Driver",
    )

    transaction {
      SchemaUtils.create(UserTable)
    }
  }

  @AfterEach
  fun tearDown() {
    transaction {
      SchemaUtils.drop(UserTable)
    }
  }

  @Test
  fun `should not allow duplicate email address`() {
    assertThrows<ExposedSQLException> {
      transaction {
        createTestUser()
        createTestUser()
      }
    }
  }

  private fun createTestUser(email: String = testEmail, password: String = testPassword): UserEntity = UserEntity.new {
    this.email = email
    this.password = password
  }
}
