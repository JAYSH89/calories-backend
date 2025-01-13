package nl.jaysh.calories.core.data.repository

import nl.jaysh.calories.core.data.local.dao.UserDao
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserRepository(private val dao: UserDao) {

  fun findByEmail(email: String) = dao.findByEmail(email)

  fun get(email: String, password: String) = dao.get(email = email, password = password)

  fun insert(email: String, password: String) = dao.insert(email = email, password = password)

  fun update(id: UUID, email: String, password: String, newEmail: String, newPassword: String) = dao.update(
    id = id,
    email = email,
    password = password,
    newEmail = newEmail,
    newPassword = newPassword,
  )

  fun delete(id: UUID, email: String, password: String) = dao.delete(
    id = id,
    email = email,
    password = password,
  )
}
