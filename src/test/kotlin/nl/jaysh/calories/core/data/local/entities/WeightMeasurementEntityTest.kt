package nl.jaysh.calories.core.data.local.entities

import nl.jaysh.calories.core.data.local.table.UserTable
import nl.jaysh.calories.core.data.local.table.WeightMeasurementTable
import nl.jaysh.calories.helper.DatabaseTestHelper
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class WeightMeasurementEntityTest {

  private val testEmail = "test@example.com"
  private val testPassword = "testPass123$"

  @BeforeEach
  fun setup() {
    DatabaseTestHelper.cleanDatabase()
  }

  @Test
  fun `create weightMeasurement successful`() {
    transaction {
      val user = createTestUser()
      val weight = 100.0

      WeightMeasurementEntity.new {
        this.weight = weight
        this.user = user
      }

      val result = WeightMeasurementEntity.find { WeightMeasurementTable.user eq user.id }
      assertEquals(1, result.toList().size)
      assertEquals(weight, result.first().weight)
    }
  }

  @Test
  fun `delete user should remove weightMeasurement`() {
    transaction {
      val user = createTestUser()
      val weight = 100.0

      WeightMeasurementEntity.new {
        this.weight = weight
        this.user = user
      }

      assertEquals(1, WeightMeasurementEntity.all().toList().size)
      UserTable.deleteWhere { UserTable.id eq user.id }
      assertEquals(0, WeightMeasurementEntity.all().toList().size)
    }
  }

  private fun createTestUser(email: String = testEmail, password: String = testPassword): UserEntity = UserEntity.new {
    this.email = email
    this.password = password
  }
}
