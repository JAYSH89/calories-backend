package nl.jaysh.calories.core.data.local.entities

import nl.jaysh.calories.core.data.local.table.CategoryTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class CategoryEntity(id: EntityID<UUID>) : UUIDEntity(id) {
  var name by CategoryTable.name
  var updatedAt by CategoryTable.updatedAt
  var createdAt by CategoryTable.createdAt

  var user by UserEntity referencedOn CategoryTable.user

  companion object : UUIDEntityClass<CategoryEntity>(CategoryTable)
}
