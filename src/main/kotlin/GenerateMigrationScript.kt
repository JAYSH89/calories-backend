@file:OptIn(ExperimentalDatabaseMigrationApi::class)

import nl.jaysh.calories.core.data.local.table.CategoryTable
import nl.jaysh.calories.core.data.local.table.FoodTable
import nl.jaysh.calories.core.data.local.table.TagTable
import nl.jaysh.calories.core.data.local.table.UserProfileTable
import nl.jaysh.calories.core.data.local.table.UserTable
import nl.jaysh.calories.core.data.local.table.WeightMeasurementTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>) {
  val message = "provide migration script name using --args=\"<YOUR_SCRIPT_NAME>\""
  val scriptName = args.getOrNull(0) ?: throw IllegalArgumentException(message)

  val database = Database.connect(
    url = "jdbc:postgresql://localhost:5432/postgres",
    driver = "org.postgresql.Driver",
    user = "postgres",
    password = "changeme!",
  )

  transaction(database) {
    MigrationUtils.generateMigrationScript(
      UserTable,
      UserProfileTable,
      WeightMeasurementTable,
      CategoryTable,
      TagTable,
      FoodTable,
      scriptDirectory = "src/main/resources/db/migration",
      scriptName = scriptName,
    )
  }
}
