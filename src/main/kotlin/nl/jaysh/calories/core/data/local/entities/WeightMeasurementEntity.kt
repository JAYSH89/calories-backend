package nl.jaysh.calories.core.data.local.entities

import nl.jaysh.calories.core.data.local.table.WeightMeasurementTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class WeightMeasurementEntity(id: EntityID<UUID>) : UUIDEntity(id) {
  var weight by WeightMeasurementTable.weight
  var updatedAt by WeightMeasurementTable.updatedAt
  var createdAt by WeightMeasurementTable.createdAt

  var user by UserEntity referencedOn WeightMeasurementTable.user

  companion object : UUIDEntityClass<WeightMeasurementEntity>(WeightMeasurementTable)
}
