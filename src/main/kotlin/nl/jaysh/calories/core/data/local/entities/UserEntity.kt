package nl.jaysh.calories.core.data.local.entities

import nl.jaysh.calories.core.data.local.table.UserTable
import nl.jaysh.calories.core.model.user.User
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
  var email by UserTable.email
  var password by UserTable.password
  var updatedAt by UserTable.updatedAt
  var createdAt by UserTable.createdAt

  companion object : UUIDEntityClass<UserEntity>(UserTable)
}

fun UserEntity.toUser() = User(
  id = this.id.value,
  email = this.email,
  password = this.password,
  createdAt = this.createdAt,
  updatedAt = this.updatedAt,
)
