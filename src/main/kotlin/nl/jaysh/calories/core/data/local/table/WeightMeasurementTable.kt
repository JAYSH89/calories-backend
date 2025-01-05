package nl.jaysh.calories.core.data.local.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object WeightMeasurementTable : UUIDTable(name = "weight_measurements") {
  val weight = double(name = "weight")
  val updatedAt = datetime(name = "updated_at").defaultExpression(CurrentDateTime)
  val createdAt = datetime(name = "created_at").defaultExpression(CurrentDateTime)

  val user = reference(name = "user_id", foreign = UserTable, onDelete = CASCADE)
}
