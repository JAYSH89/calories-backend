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
  fun `get user successful`() {
    val newUserId = insertUser()

    val result = dao.get(email = testEmail, password = testPassword)
    assertThat(newUserId.toString()).isEqualTo(result.id.toString())
    assertThat(testEmail).isEqualTo(result.email)
    assertThat(testPassword).isEqualTo(result.password)
  }

  @Test
  fun `get user not exists should throw error`() {
    val exception = assertThrows<IllegalArgumentException> {
      dao.get(email = testEmail, password = testPassword)
    }

    assertThat(exception.message).isEqualTo("resource not found")
  }

  @Test
  fun `get user password incorrect should throw error`() {
    insertUser()

    val exception = assertThrows<IllegalArgumentException> {
      dao.get(email = testEmail, password = "invalidPassword")
    }

    assertThat(exception.message).isEqualTo("invalid credentials")
  }

  @Test
  fun `insert user successful`() {
    val newUser = dao.insert(email = testEmail, password = testPassword)

    val result = transaction {
      UserTable.selectAll()
        .where { UserTable.email eq testEmail }
        .single()
    }

    assertThat(result[UserTable.id]).isNotNull()
    assertThat(result[UserTable.email]).isEqualTo(testEmail)
    assertThat(result[UserTable.password]).isEqualTo(testPassword)
    assertThat(newUser?.email).isEqualTo(testEmail)
    assertThat(newUser?.password).isEqualTo(testPassword)
  }

  @Test
  fun `insert should throw ExposedSQLException duplicate email`() {
    insertUser()

    assertThrows<ExposedSQLException> {
      dao.insert(email = testEmail, password = testPassword)
    }
  }

  @Test
  fun `update user successful`() {
    val newUserId = insertUser()

    val updatedUser = dao.update(
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
    assertThat(updatedUser.email).isEqualTo("update@example.com")
    assertThat(updatedUser.password).isEqualTo("updatedPass123$")
  }

  @Test
  fun `update should ExposedSQLException duplicate email`() {
    insertUser()
    val anotherUser = insertUser(newEmail = "another@example.com", newPassword = testPassword)

    assertThrows<ExposedSQLException> {
      dao.update(
        id = anotherUser,
        email = "another@example.com",
        password = testPassword,
        newEmail = testEmail,
        newPassword = testPassword,
      )
    }
  }

  @Test
  fun `update should throw IllegalArgumentException resource not found if not exists`() {
    val notExistsId = "F103D351-989A-4FC6-B2DD-2FA768162CDA"
    val exception = assertThrows<IllegalArgumentException> {
      dao.update(
        id = UUID.fromString(notExistsId),
        email = testEmail,
        password = testPassword,
        newEmail = "update@example.com",
        newPassword = "updatedPass123$",
      )
    }

    assertThat(exception.message).isEqualTo("resource not found")
  }

  @Test
  fun `update should throw IllegalArgumentException invalid credentials if password incorrect`() {
    val newUserId = insertUser()

    val exception = assertThrows<IllegalArgumentException> {
      dao.update(
        id = newUserId,
        email = testEmail,
        password = "invalidPassword",
        newEmail = "update@example.com",
        newPassword = "updatedPass123$",
      )
    }

    assertThat(exception.message).isEqualTo("invalid credentials")
  }

  @Test
  fun `update should throw IllegalArgumentException if given UUID and password match but incorrect email`() {
    val newUserId = insertUser()

    val exception = assertThrows<IllegalArgumentException> {
      dao.update(
        id = newUserId,
        email = "invalidEmail@example.com",
        password = testPassword,
        newEmail = "update@example.com",
        newPassword = "updatedPass123$",
      )
    }

    assertThat(exception.message).isEqualTo("invalid credentials")
  }

  @Test
  fun `update should throw  not found if given email and password match but incorrect UUID`() {
    insertUser()
    val notExistsId = "F103D351-989A-4FC6-B2DD-2FA768162CDA"

    val exception = assertThrows<IllegalArgumentException> {
      dao.update(
        id = UUID.fromString(notExistsId),
        email = testEmail,
        password = testPassword,
        newEmail = "update@example.com",
        newPassword = "updatedPass123$",
      )
    }

    assertThat(exception.message).isEqualTo("resource not found")
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
  fun `delete user not found should throw error`() {
    insertUser()
    val notExistsId = "F103D351-989A-4FC6-B2DD-2FA768162CDA"

    val exception = assertThrows<IllegalArgumentException> {
      dao.delete(id = UUID.fromString(notExistsId), email = testEmail, password = testPassword)
    }

    assertThat(exception.message).isEqualTo("resource not found")
  }

  @Test
  fun `delete user invalid password should throw error`() {
    val newUserId = insertUser()

    val exception = assertThrows<IllegalArgumentException> {
      dao.delete(id = newUserId, email = testEmail, password = "invalid password")
    }

    assertThat(exception.message).isEqualTo("invalid credentials")
  }

  @Test
  fun `delete user invalid email should throw error`() {
    val newUserId = insertUser()

    val exception = assertThrows<IllegalArgumentException> {
      dao.delete(id = newUserId, email = "invalidEmail@example.com", password = testPassword)
    }

    assertThat(exception.message).isEqualTo("invalid credentials")
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
