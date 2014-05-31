package com.example.carpool.authenticate;

/**
 * Contains the current session information.
 * @author ambika_b
 *
 */
public class SessionInformation {

	boolean status;
	
	Long userId;
	
	public SessionInformation() {
		status = false;
		userId = -1l;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void logOut() {
		status = false;
		userId = -1l;
	}
}
