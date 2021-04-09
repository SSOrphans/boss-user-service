/**
 * 
 */
package org.ssor.boss.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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
	public ResponseEntity<String> updateServiceException(UserDataAccessException udaEx) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(udaEx.getMessage() + "Internal server error, please contact the administrator.");
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> typeMismatchException() {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Malformed request syntax.");
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> messageNotReableException() {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payload unreadable.");
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<String> methodNotAllowedException() {
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Method not allowed.");
	}
}
