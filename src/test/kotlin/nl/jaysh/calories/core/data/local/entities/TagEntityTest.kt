package nl.jaysh.calories.core.data.local.entities

import nl.jaysh.calories.core.data.local.table.TagTable
import nl.jaysh.calories.core.data.local.table.UserTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TagEntityTest {

  private val testEmail = "test@example.com"
  private val testPassword = "testPass123$"

  @BeforeEach
  fun setup() {
    Database.connect(
      url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
      driver = "org.h2.Driver",
    )

    transaction {
      SchemaUtils.create(UserTable, TagTable)
    }
  }

  @AfterEach
  fun tearDown() {
    transaction {
      SchemaUtils.drop(TagTable, UserTable)
    }
  }

  @Test
  fun `create tag successful`() {
    transaction {
      val user = createTestUser()

      TagEntity.new {
        this.name = "Tag"
        this.user = user
      }

      assertEquals(1, TagEntity.all().toList().size)
    }
  }

  @Test
  fun `creating duplicate tag name should fail`() {
    assertThrows<ExposedSQLException> {
      transaction {
        val user = createTestUser()
        createTag(user = user)
        createTag(user = user)
      }
    }
  }

  @Test
  fun `delete user should remove tag`() {
    transaction {
      val user = createTestUser()
      createTag(user = user)

      assertEquals(1, TagEntity.all().toList().size)
      UserTable.deleteWhere { UserTable.id eq user.id }
      assertEquals(0, TagEntity.all().toList().size)
    }
  }

  private fun createTestUser(email: String = testEmail, password: String = testPassword): UserEntity = UserEntity.new {
    this.email = email
    this.password = password
  }

  private fun createTag(user: UserEntity, name: String = "tag"): TagEntity = TagEntity.new {
    this.name = name
    this.user = user
  }
}
