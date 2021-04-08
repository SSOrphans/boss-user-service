package org.ssor.boss.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice
{
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<String> userAlreadyExists(UserAlreadyExistsException exc)
  {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(exc.getMessage());
  }
}
