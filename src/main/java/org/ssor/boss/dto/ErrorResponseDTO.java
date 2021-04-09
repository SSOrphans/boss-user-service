package org.ssor.boss.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO
{
  private LocalDateTime timestamp;
  private int status;
  private String error;
  private String message;
  private String path;
}
