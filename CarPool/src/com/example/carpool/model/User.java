package com.example.carpool.model;

import java.io.Serializable;

/**
 * User information is maintained here.
 * @author ambika_b
 *
 */
public class User implements Serializable{

	private static final long serialVersionUID = 1L;

	long userId;
	
	String emailId;
	
	String mobileNo;
	
	String password;
	
	String userName;

	public User() {
		
	}
	
	public User(long userId, String emailId, String mobileNo, String password,
			String userName) {
		super();
		this.userId = userId;
		this.emailId = emailId;
		this.mobileNo = mobileNo;
		this.password = password;
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}