package org.ssor.boss.core.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents the response of an API request, good or bad.
 * <p>
 *   This single utility allows us to report all the information we need for an API request. We are able to respond with
 *   both success and failure messages and provide as much information as possible on validation errors when failures
 *   occur.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequestResponse
{
  private LocalDateTime timestamp;
  private int status;
  private String reason;
  private String message;
  private String path;
  private Map<String, String> validationErrors;
}
