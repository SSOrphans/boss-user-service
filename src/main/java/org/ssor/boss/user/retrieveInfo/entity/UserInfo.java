/**
 * 
 */
package org.ssor.boss.user.retrieveInfo.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Christian Angeles
 *
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserInfo {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer userId;
	
	@Column(name = "display_name")
	private String displayName;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "created")
	private Timestamp created;
	
	@Column(name = "deleted")
	private Timestamp deleted;
	
	@Column(name = "grants")
	private Integer grants;
	
	@Column(name = "confirmed")
	private Boolean confirmed;
	
	@Column(name = "locked")
	private Boolean locked;
}
