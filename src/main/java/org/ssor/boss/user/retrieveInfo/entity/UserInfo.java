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

/**
 * @author Christian Angeles
 *
 */
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
	

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getDeleted() {
		return deleted;
	}

	public void setDeleted(Timestamp deleted) {
		this.deleted = deleted;
	}

	public Integer getGrants() {
		return grants;
	}

	public void setGrants(Integer grants) {
		this.grants = grants;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}
	
}
