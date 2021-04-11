package org.ssor.boss.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class UserEntity implements Serializable
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "type_id")
  private Integer typeId;
  @Column(name = "branch_id")
  private Integer branchId;
  @Column(name = "display_name")
  private String displayName;
  private String email;
  private String password;
  private LocalDateTime created;
  private LocalDateTime deleted = null;
  private Boolean confirmed;

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserEntity that = (UserEntity) o;
    return Objects.equals(id, that.id) && displayName.equals(that.displayName) && email.equals(that.email);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(id, displayName, email);
  }
}
