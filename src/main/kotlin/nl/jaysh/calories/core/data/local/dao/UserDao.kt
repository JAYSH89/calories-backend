package nl.jaysh.calories.core.data.local.dao

import nl.jaysh.calories.core.data.local.entities.UserEntity
import nl.jaysh.calories.core.data.local.table.UserTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserDao {

  fun insert(email: String, password: String) {
    transaction {
      val newUser = UserEntity.new {
        this.email = email
        this.password = password
      }
    }
  }

  fun update(
    id: UUID,
    email: String,
    password: String,
    newEmail: String,
    newPassword: String,
  ) {
    val result = transaction {
      UserEntity.findSingleByAndUpdate(
        op = (UserTable.id eq id) and (UserTable.email eq email) and (UserTable.password eq password),
        block = {
          it.email = newEmail
          it.password = newPassword
        },
      )
    }

    requireNotNull(result)
  }

  fun delete(id: UUID, email: String, password: String) {
    transaction {
      UserEntity.find { (UserTable.id eq id) and (UserTable.email eq email) and (UserTable.password eq password) }
        .single()
        .delete()
    }
  }
}
