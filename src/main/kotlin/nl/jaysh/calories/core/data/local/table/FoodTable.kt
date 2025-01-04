package nl.jaysh.calories.core.data.local.table

import nl.jaysh.calories.core.model.AmountType
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object FoodTable : UUIDTable(name = "foods") {
    val name = varchar(name = "name", length = 64)
    val carbs = double(name = "carbs")
    val proteins = double(name = "proteins")
    val fats = double(name = "fats")
    val amount = double(name = "amount")
    val amountType = enumeration(name = "amount_type", AmountType::class)
    val updatedAt = datetime(name = "updated_at").defaultExpression(CurrentDateTime)
    val createdAt = datetime(name = "created_at").defaultExpression(CurrentDateTime)
}
