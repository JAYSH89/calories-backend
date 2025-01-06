package nl.jaysh.calories.core.data.local.entities

import nl.jaysh.calories.core.data.local.table.UserTable
import nl.jaysh.calories.helper.DatabaseTestHelper
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CategoryEntityTest {

  private val testEmail = "test@example.com"
  private val testPassword = "testPass123$"

  @BeforeEach
  fun setup() {
    DatabaseTestHelper.cleanDatabase()
  }

  @Test
  fun `create category successful`() {
    transaction {
      val user = createTestUser()

      CategoryEntity.new {
        this.name = "Category"
        this.user = user
      }

      assertEquals(1, CategoryEntity.all().toList().size)
    }
  }

  @Test
  fun `creating duplicate category name should fail`() {
    assertThrows<ExposedSQLException> {
      transaction {
        val user = createTestUser()
        createCategory(user = user)
        createCategory(user = user)
      }
    }
  }

  @Test
  fun `delete user should remove category`() {
    transaction {
      val user = createTestUser()
      createCategory(user = user)

      assertEquals(1, CategoryEntity.all().toList().size)
      UserTable.deleteWhere { UserTable.id eq user.id }
      assertEquals(0, CategoryEntity.all().toList().size)
    }
  }

  private fun createTestUser(email: String = testEmail, password: String = testPassword): UserEntity = UserEntity.new {
    this.email = email
    this.password = password
  }

  private fun createCategory(user: UserEntity, category: String = "category") = CategoryEntity.new {
    this.name = category
    this.user = user
  }
}
