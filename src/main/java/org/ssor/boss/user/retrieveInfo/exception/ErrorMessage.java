/**
 * 
 */
package org.ssor.boss.user.retrieveInfo.exception;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Christian Angeles
 *
 */
@Builder
@Getter
@Setter
public class ErrorMessage {
	private String status;
	private String error;
	private String message;
}
