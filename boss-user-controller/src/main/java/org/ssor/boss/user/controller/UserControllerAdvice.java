package org.ssor.boss.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.ssor.boss.core.exception.UserAlreadyExistsException;
import org.ssor.boss.core.transfer.ApiRequestResponse;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import static org.springframework.http.HttpStatus.CONFLICT;

@RestControllerAdvice
public class UserControllerAdvice
{
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ApiRequestResponse> onUserAlreadyExistsException(UserAlreadyExistsException exception,
                                                                         HttpServletRequest request)
  {
    final var response = new ApiRequestResponse();
    response.setTimestamp(Instant.now().toEpochMilli());
    response.setStatus(CONFLICT.value());
    response.setReason(CONFLICT.getReasonPhrase());
    response.setMessage(exception.getMessage());
    response.setPath(request.getServletPath());
    response.setValidationErrors(new HashMap<>());
    return ResponseEntity.status(CONFLICT).body(response);
  }
}
