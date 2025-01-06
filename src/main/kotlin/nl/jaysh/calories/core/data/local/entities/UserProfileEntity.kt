package nl.jaysh.calories.core.data.local.entities

import nl.jaysh.calories.core.data.local.table.UserProfileTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class UserProfileEntity(id: EntityID<UUID>) : UUIDEntity(id) {
  var sex by UserProfileTable.sex
  var height by UserProfileTable.height
  var activityType by UserProfileTable.activityType
  var birthday by UserProfileTable.birthday
  var bodyFat by UserProfileTable.bodyFat
  var updatedAt by UserProfileTable.updatedAt
  var createdAt by UserProfileTable.createdAt

  var user by UserEntity referencedOn UserProfileTable.user

  companion object : UUIDEntityClass<UserProfileEntity>(UserProfileTable)
}
