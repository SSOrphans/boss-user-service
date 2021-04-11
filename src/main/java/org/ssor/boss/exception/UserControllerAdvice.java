package org.ssor.boss.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestControllerAdvice
public class UserControllerAdvice
{
  @ResponseStatus(CONFLICT)
  @RequestMapping(produces = APPLICATION_JSON_VALUE)
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ApiRequestException onUserAlreadyExistsException(UserAlreadyExistsException exc, HttpServletRequest request)
  {
    return createErrorResponse(CONFLICT, Optional.empty(), exc, request);
  }

  @ResponseStatus(BAD_REQUEST)
  @RequestMapping(produces = APPLICATION_JSON_VALUE)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ApiRequestException handleMethodArgumentNotValidException(MethodArgumentNotValidException exc,
                                                                   HttpServletRequest request)
  {
    final var optionalBindingRequest = Optional.of(exc.getBindingResult());
    return createErrorResponse(BAD_REQUEST, optionalBindingRequest, "Invalid input/output data", request);
  }

  private ApiRequestException createErrorResponse(HttpStatus status, Optional<BindingResult> optionalBindingResult,
                                                  Exception exception, HttpServletRequest request)
  {
    final var errorResponse = new ApiRequestException();
    final var validationErrors = new HashMap<String, String>();
    errorResponse.setTimestamp(LocalDateTime.now());
    errorResponse.setStatus(status.value());
    errorResponse.setError(status.getReasonPhrase());
    errorResponse.setMessage(exception.getMessage());
    if (optionalBindingResult.isPresent())
    {
      final var bindingResult = optionalBindingResult.get();
      for (var error : bindingResult.getFieldErrors())
        validationErrors.putIfAbsent(error.getField(), error.getDefaultMessage());
    }
    errorResponse.setValidationErrors(validationErrors);
    errorResponse.setPath(request.getServletPath());
    return errorResponse;
  }

  private ApiRequestException createErrorResponse(HttpStatus status, Optional<BindingResult> optionalBindingResult,
                                                 String message, HttpServletRequest request)
  {
    final var exception = new Exception(message);
    return createErrorResponse(status, optionalBindingResult, exception, request);
  }
}
