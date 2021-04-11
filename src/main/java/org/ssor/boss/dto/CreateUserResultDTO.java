package org.ssor.boss.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResultDTO implements Serializable
{
  private Integer id;
  private Integer typeId;
  private Integer branchId;
  private String displayName;
  private String email;
  private LocalDateTime created;
}
