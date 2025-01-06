package nl.jaysh.calories.core

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException::class)
  fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
    val httpStatus = HttpStatus.BAD_REQUEST

    val errorResponse = ErrorResponse(
      status = httpStatus.value(),
      message = e.message ?: "An unexpected error occurred",
      timestamp = System.currentTimeMillis(),
    )
    return ResponseEntity(errorResponse, httpStatus)
  }

  @ExceptionHandler
  fun handleGlobalException(e: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
    val httpStatus = HttpStatus.INTERNAL_SERVER_ERROR

    val errorResponse = ErrorResponse(
      status = httpStatus.value(),
      message = e.message ?: "An unexpected error occurred",
      timestamp = System.currentTimeMillis(),
    )
    return ResponseEntity(errorResponse, httpStatus)
  }
}

data class ErrorResponse(
  val status: Int,
  val message: String,
  val timestamp: Long,
)
