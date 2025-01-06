package nl.jaysh.calories.core

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler
  fun handleGlobalException(e: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
    val errorResponse = ErrorResponse(
      status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
      message = e.message ?: "An unexpected error occurred",
      timestamp = System.currentTimeMillis(),
    )
    return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
  }
}

data class ErrorResponse(
  val status: Int,
  val message: String,
  val timestamp: Long,
)
