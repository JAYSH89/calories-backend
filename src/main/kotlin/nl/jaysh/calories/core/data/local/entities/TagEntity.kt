package nl.jaysh.calories.core.data.local.entities

import nl.jaysh.calories.core.data.local.table.TagTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class TagEntity(id: EntityID<UUID>) : UUIDEntity(id) {
  var name by TagTable.name
  var updatedAt by TagTable.updatedAt
  var createdAt by TagTable.createdAt

  var user by UserEntity referencedOn TagTable.user

  companion object : UUIDEntityClass<TagEntity>(TagTable)
}
