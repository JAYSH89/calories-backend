package nl.jaysh.calories.core.data.local.table

import nl.jaysh.calories.core.model.ActivityType
import nl.jaysh.calories.core.model.Sex
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption.CASCADE
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime

object UserProfileTable : UUIDTable(name = "user_profile") {
  val sex = enumeration("sex", Sex::class)
  val height = integer(name = "height")
  val activityType = enumeration("activity_type", ActivityType::class)
  val birthday = date("birthday")
  val bodyFat = integer(name = "body_fat").nullable()
  val updatedAt = datetime(name = "updated_at").defaultExpression(CurrentDateTime)
  val createdAt = datetime(name = "created_at").defaultExpression(CurrentDateTime)

  val user = reference(name = "user_id", foreign = UserTable, onDelete = CASCADE).uniqueIndex()
}
