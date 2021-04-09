package org.ssor.boss.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.ssor.boss.dto.ErrorResponseDTO;

import java.time.LocalDateTime;

@RestControllerAdvice
public class UserControllerAdvice extends ResponseEntityExceptionHandler
{
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<Object> userAlreadyExists(UserAlreadyExistsException exc)
  {
    final var errorResponse = ErrorResponseDTO.builder()
      .timestamp(LocalDateTime.now())
      .status(HttpStatus.CONFLICT.value())
      .error(HttpStatus.CONFLICT.getReasonPhrase())
      .message(exc.getMessage())
      .path("/api/v1/users")
      .build();
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                HttpStatus status, WebRequest request)
  {
    final var exception = new Exception(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    return createErrorResponse(status, exception, request);
  }

  private ResponseEntity<Object> createErrorResponse(HttpStatus status, Exception exception, WebRequest request)
  {
    final var requestPath = ((ServletWebRequest)request).getRequest().getRequestURI();
    final var errorResponse = ErrorResponseDTO.builder()
                                              .timestamp(LocalDateTime.now())
                                              .status(status.value())
                                              .error(status.getReasonPhrase())
                                              .message(exception.getMessage())
                                              .path(requestPath)
                                              .build();
    return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), status, request);
  }
}
