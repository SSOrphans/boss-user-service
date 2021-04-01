/**
 * 
 */
package org.ssor.boss.user.retrieveInfo.dto;

import java.sql.Timestamp;

/**
 * @author Christian Angeles
 *
 */
public class UserInfoDto {

	private Integer userId;
	private String email;
	private String displayName;
	private Timestamp created;
	private Timestamp deleted;
	private Boolean confirmed;
	private Boolean locked;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
