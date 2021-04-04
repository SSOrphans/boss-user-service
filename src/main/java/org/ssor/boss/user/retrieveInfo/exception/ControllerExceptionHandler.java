/**
 * 
 */
package org.ssor.boss.user.retrieveInfo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @author Christian Angeles
 *
 */
@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorMessage> typeMismatchException() {
		return ResponseEntity.badRequest().body(
				ErrorMessage.builder().status("400").error("Bad Request").message("Malformed request syntax").build());
	}
}
