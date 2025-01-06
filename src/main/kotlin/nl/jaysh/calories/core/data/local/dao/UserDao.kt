package nl.jaysh.calories.core.data.local.dao

import nl.jaysh.calories.core.data.local.entities.UserEntity
import nl.jaysh.calories.core.data.local.entities.toUser
import nl.jaysh.calories.core.data.local.table.UserTable
import nl.jaysh.calories.core.model.user.User
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserDao {

  fun get(email: String, password: String): User {
    val user = transaction {
      UserEntity
        .find { UserTable.email eq email }
        .singleOrNull()
    }

    requireNotNull(user) { "resource not found" }
    require(user.email == email && user.password == password) { "invalid credentials" }

    return user.toUser()
  }

  fun insert(email: String, password: String): User {
    transaction {
      UserEntity.new {
        this.email = email
        this.password = password
      }
    }

    return get(email, password)
  }

  fun update(
    id: UUID,
    email: String,
    password: String,
    newEmail: String,
    newPassword: String,
  ): User {
    val result = transaction {
      val entity = UserEntity.findById(id)

      requireNotNull(entity) { "resource not found" }
      require(entity.email == email && entity.password == password) { "invalid credentials" }

      UserEntity.findSingleByAndUpdate(UserTable.id eq id) {
        it.email = newEmail
        it.password = newPassword
      }
    }

    requireNotNull(result)
    return result.toUser()
  }

  fun delete(id: UUID, email: String, password: String) {
    transaction {
      val entity = UserEntity.findById(id)

      requireNotNull(entity) { "resource not found" }
      require(entity.email == email && entity.password == password) { "invalid credentials" }

      entity.delete()
    }
  }
}
