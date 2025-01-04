package nl.jaysh.calories.core.data.local.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object TagTable : UUIDTable(name = "tags") {
    val name = varchar(name = "name", length = 64)
    val updatedAt = datetime(name = "updated_at").defaultExpression(CurrentDateTime)
    val createdAt = datetime(name = "created_at").defaultExpression(CurrentDateTime)
}
