package nl.jaysh.calories.core.data.local.entities

import nl.jaysh.calories.core.data.local.table.FoodTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class FoodEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    var name by FoodTable.name
    var carbs by FoodTable.carbs
    var proteins by FoodTable.proteins
    var fats by FoodTable.fats
    var amount by FoodTable.amount
    var amountType by FoodTable.amountType
    var updatedAt by FoodTable.updatedAt
    var createdAt by FoodTable.createdAt

    companion object : UUIDEntityClass<FoodEntity>(FoodTable)
}