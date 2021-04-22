/**
 * 
 */
package org.ssor.boss.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @author Christian Angeles
 *
 */
@ControllerAdvice
public class UserControllerAdvice {

	@ExceptionHandler(UserDataAccessException.class)
	public ResponseEntity<String> serviceException(UserDataAccessException udaEx) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(udaEx.getMessage() + "Internal server error, please contact the administrator.");
	}
	
	@ExceptionHandler(ForgotPassTokenException.class)
	public ResponseEntity<String> tokenException(ForgotPassTokenException fptEx) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(fptEx.getMessage() + "Internal server error, please contact the administrator.");
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> typeMismatchException() {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Malformed request syntax.");
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> invalidMethodArgumentException(BindingResult bindResult) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindResult.getFieldError().getDefaultMessage());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> messageNotReableException() {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request body.");
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<String> methodNotAllowedException() {
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Method not allowed.");
	}
	
	@ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
	public ResponseEntity<String> mediaNotAcceptedException() {
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Media type not accepted.");
	}
	
}
