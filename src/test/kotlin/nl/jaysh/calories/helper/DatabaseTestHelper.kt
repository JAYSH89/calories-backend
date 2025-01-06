package nl.jaysh.calories.helper

import nl.jaysh.calories.core.data.local.table.CategoryTable
import nl.jaysh.calories.core.data.local.table.FoodTable
import nl.jaysh.calories.core.data.local.table.TagTable
import nl.jaysh.calories.core.data.local.table.UserProfileTable
import nl.jaysh.calories.core.data.local.table.UserTable
import nl.jaysh.calories.core.data.local.table.WeightMeasurementTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseTestHelper {

  private val tables = listOf(
    UserTable,
    UserProfileTable,
    FoodTable,
    WeightMeasurementTable,
    CategoryTable,
    TagTable,
  )

  init {
    Database.connect(
      url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
      driver = "org.h2.Driver",
    )

    transaction {
      createTables()
    }
  }

  fun cleanDatabase() {
    transaction {
      dropTables()
      createTables()
    }
  }

  private fun createTables() = SchemaUtils.create(*tables.toTypedArray())

  private fun dropTables() = SchemaUtils.drop(*tables.toTypedArray())
}
