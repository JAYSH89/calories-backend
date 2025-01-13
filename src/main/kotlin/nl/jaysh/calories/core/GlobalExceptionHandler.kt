package nl.jaysh.calories.core

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(IllegalStateException::class)
  fun handleIllegalStateException(e: IllegalStateException) = ResponseEntity(
    ErrorResponse(
      status = HttpStatus.CONFLICT.value(),
      message = e.message ?: e.localizedMessage,
      timestamp = System.currentTimeMillis(),
    ),
    HttpStatus.CONFLICT,
  )

  @ExceptionHandler(BadCredentialsException::class)
  fun handleBadCredentialsException(e: BadCredentialsException) = ResponseEntity(
    ErrorResponse(
      status = HttpStatus.UNAUTHORIZED.value(),
      message = "incorrect username or password",
      timestamp = System.currentTimeMillis(),
    ),
    HttpStatus.UNAUTHORIZED,
  )

  @ExceptionHandler(IllegalArgumentException::class)
  fun handleIllegalArgumentException(e: IllegalArgumentException) = ResponseEntity(
    ErrorResponse(
      status = HttpStatus.BAD_REQUEST.value(),
      message = e.message ?: "An unexpected error occurred",
      timestamp = System.currentTimeMillis(),
    ),
    HttpStatus.BAD_REQUEST,
  )

  @ExceptionHandler
  fun handleGlobalException(e: Exception) = ResponseEntity(
    ErrorResponse(
      status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
      message = e.message ?: "An unexpected error occurred",
      timestamp = System.currentTimeMillis(),
    ),
    HttpStatus.INTERNAL_SERVER_ERROR,
  )
}

data class ErrorResponse(
  val status: Int,
  val message: String,
  val timestamp: Long,
)
