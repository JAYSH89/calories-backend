package nl.jaysh.calories.core.data.local.entities

import nl.jaysh.calories.core.data.local.table.UserProfileTable
import nl.jaysh.calories.core.data.local.table.UserTable
import nl.jaysh.calories.core.model.ActivityType
import nl.jaysh.calories.core.model.Sex
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserProfileEntityTest {

  private val testEmail = "test@example.com"
  private val testPassword = "testPass123$"

  @BeforeEach
  fun setup() {
    Database.connect(
      url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
      driver = "org.h2.Driver",
    )

    transaction {
      SchemaUtils.create(UserTable, UserProfileTable)
    }
  }

  @AfterEach
  fun tearDown() {
    transaction {
      SchemaUtils.drop(UserProfileTable, UserTable)
    }
  }

  @Test
  fun `create weightMeasurement successful`() {
    transaction {
      val user = createTestUser()

      UserProfileEntity.new {
        this.sex = Sex.MALE
        this.height = 187
        this.activityType = ActivityType.SEDENTARY
        this.birthday = LocalDate.of(1990, 1, 1)
        this.bodyFat = 18
        this.user = user
      }

      val result = UserProfileEntity.find { UserProfileTable.user eq user.id }
      assertEquals(1, result.toList().size)
      assertEquals(Sex.MALE, result.first().sex)
      assertEquals(187, result.first().height)
      assertEquals(ActivityType.SEDENTARY, result.first().activityType)
      assertEquals(LocalDate.of(1990, 1, 1), result.first().birthday)
      assertEquals(18, result.first().bodyFat)
    }
  }

  @Test
  fun `create weightMeasurement without bodyFat successful`() {
    transaction {
      val user = createTestUser()

      UserProfileEntity.new {
        this.sex = Sex.MALE
        this.height = 187
        this.activityType = ActivityType.SEDENTARY
        this.birthday = LocalDate.of(1990, 1, 1)
        this.bodyFat = null
        this.user = user
      }

      val result = UserProfileEntity.find { UserProfileTable.user eq user.id }
      assertEquals(1, result.toList().size)
      assertNull(result.first().bodyFat)
    }
  }

  @Test
  fun `delete user should remove weightMeasurement`() {
    transaction {
      val user = createTestUser()

      UserProfileEntity.new {
        this.sex = Sex.MALE
        this.height = 187
        this.activityType = ActivityType.SEDENTARY
        this.birthday = LocalDate.of(1990, 1, 1)
        this.bodyFat = null
        this.user = user
      }

      assertEquals(1, UserProfileEntity.all().toList().size)
      UserTable.deleteWhere { UserTable.id eq user.id }
      assertEquals(0, UserProfileEntity.all().toList().size)
    }
  }

  @Test
  fun `user can only have a single profile`() {
    assertThrows<ExposedSQLException> {
      transaction {
        val user = createTestUser()

        repeat(times = 2) {
          UserProfileEntity.new {
            this.sex = Sex.MALE
            this.height = 187
            this.activityType = ActivityType.SEDENTARY
            this.birthday = LocalDate.of(1990, 1, 1)
            this.bodyFat = null
            this.user = user
          }
        }
      }
    }
  }

  private fun createTestUser(email: String = testEmail, password: String = testPassword): UserEntity = UserEntity.new {
    this.email = email
    this.password = password
  }
}
