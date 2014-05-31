package com.example.carpool.util;

/**
 * Contains the Result of ay operation and its associated message if there was an error / failure.
 * @author ambika_b
 *
 */
public class Result {

	String message; 
	
	boolean status;

	public Result(String message, boolean status) {
		super();
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}