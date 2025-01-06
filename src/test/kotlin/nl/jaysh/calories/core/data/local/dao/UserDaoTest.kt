package nl.jaysh.calories.core.data.local.dao

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import nl.jaysh.calories.core.data.local.table.UserTable
import nl.jaysh.calories.helper.DatabaseTestHelper
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals

class UserDaoTest {

  private val testEmail = "test@example.com"
  private val testPassword = "testPass123$"

  private lateinit var dao: UserDao

  @BeforeEach
  fun setup() {
    DatabaseTestHelper.cleanDatabase()
    dao = UserDao()
  }

  @Test
  fun `create user successful`() {
    dao.insert(email = testEmail, password = testPassword)

    val result = transaction {
      UserTable.selectAll()
        .where { UserTable.email eq testEmail }
        .single()
    }

    assertThat(result[UserTable.id]).isNotNull()
    assertThat(result[UserTable.email]).isEqualTo(testEmail)
    assertThat(result[UserTable.password]).isEqualTo(testPassword)
  }

  @Test
  fun `update user successful`() {
    val newUserId = insertUser()

    dao.update(
      id = newUserId,
      email = testEmail,
      password = testPassword,
      newEmail = "update@example.com",
      newPassword = "updatedPass123$",
    )

    val result = transaction {
      UserTable.selectAll().where { UserTable.id eq newUserId }.single()
    }

    val resultId = result[UserTable.id].toString()
    val expectedId = newUserId.toString()

    assertThat(resultId).isEqualTo(expectedId)
    assertThat(result[UserTable.email]).isEqualTo("update@example.com")
    assertThat(result[UserTable.password]).isEqualTo("updatedPass123$")
  }

  @Test
  fun `delete user successful`() {
    val newUserId = insertUser()

    dao.delete(id = newUserId, email = testEmail, password = testPassword)

    val result = transaction {
      UserTable.selectAll().toList()
    }

    assertThat(result.size).isEqualTo(0)
  }

  @Test
  fun `should throw exception duplicate email`() {
    insertUser()

    assertThrows<ExposedSQLException> {
      dao.insert(email = testEmail, password = testPassword)
    }
  }

  @Test
  fun `should throw InvalidCredential exception if password incorrect`() {
    TODO()
  }

  @Test
  fun `should throw InvalidCredential exception if given UUID and password match but incorrect email`() {
    TODO()
  }

  @Test
  fun `should throw InvalidCredential exception if given email and password match but incorrect UUID`() {
    TODO()
  }

  @Test
  fun `should throw NotFound exception if email not exists`() {
    TODO()
  }

  @Test
  fun `should throw NotFound exception if UUID not exists`() {
    TODO()
  }

  @Test
  fun `should throw InvalidCredential exception if given UUID and email do not match together`() {
    TODO()
  }

  private fun insertUser(
    newEmail: String = testEmail,
    newPassword: String = testPassword,
  ): UUID {
    val newUser = transaction {
      UserTable.insertAndGetId {
        it[email] = newEmail
        it[password] = newPassword
      }
    }

    return newUser.value
  }
}
