package nl.jaysh.calories.core.data.local.entities

import nl.jaysh.calories.core.data.local.table.FoodTable
import nl.jaysh.calories.core.data.local.table.UserTable
import nl.jaysh.calories.core.model.AmountType
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

class FoodEntityTest {

  private val testEmail = "test@example.com"
  private val testPassword = "testPass123$"

  @BeforeEach
  fun setup() {
    Database.connect(
      url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
      driver = "org.h2.Driver",
    )

    transaction {
      SchemaUtils.create(UserTable, FoodTable)
    }
  }

  @AfterEach
  fun tearDown() {
    transaction {
      SchemaUtils.drop(FoodTable, UserTable)
    }
  }

  @Test
  fun `create food successful`() {
    transaction {
      val user = createTestUser()
      createFood(user = user, name = "testFood")
      assertEquals(1, FoodEntity.all().toList().size)
    }
  }

  @Test
  fun `creating duplicate food name should fail`() {
    assertThrows<ExposedSQLException> {
      transaction {
        val user = createTestUser()
        createFood(user = user, name = "testFood")
        createFood(user = user, name = "testFood")
      }
    }
  }

  @Test
  fun `delete user should remove food`() {
    transaction {
      val user = createTestUser()
      createFood(user = user, name = "testFood")

      assertEquals(1, FoodEntity.all().toList().size)
      UserTable.deleteWhere { UserTable.id eq user.id }
      assertEquals(0, FoodEntity.all().toList().size)
    }
  }

  private fun createTestUser(email: String = testEmail, password: String = testPassword): UserEntity = UserEntity.new {
    this.email = email
    this.password = password
  }

  private fun createFood(
    user: UserEntity,
    name: String,
    carbs: Double = 1.0,
    proteins: Double = 1.0,
    fats: Double = 1.0,
    amount: Double = 1.0,
    amountType: AmountType = AmountType.PIECE,
  ): FoodEntity = FoodEntity.new {
    this.name = name
    this.carbs = carbs
    this.proteins = proteins
    this.fats = fats
    this.amount = amount
    this.amountType = amountType
    this.user = user
  }
}
