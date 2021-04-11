package org.ssor.boss.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ApiRequestException
{
  private LocalDateTime timestamp;
  private int status;
  private String error;
  private String message;
  private Map<String, String> validationErrors;
  private String path;
}
