package nl.jaysh.calories.core.data.local.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object UserTable : UUIDTable(name = "users") {
  val email = varchar(name = "email", length = 100)
  val password = varchar(name = "password", length = 100)
  val updatedAt = datetime(name = "updated_at").defaultExpression(CurrentDateTime)
  val createdAt = datetime(name = "created_at").defaultExpression(CurrentDateTime)
}
