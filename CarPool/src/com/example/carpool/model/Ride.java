package com.example.carpool.model;

/**
 * Ride details
 * @author ambika_b
 *
 */
public class Ride {

	int rideId;

	String emailId;

	String phoneNumber;

	String name;

	String from;

	String to;

	String fromTime;

	String comments;
	
	String fromDate; //TODO: change to Date ??

	/*	public Ride() {
		super();
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.name = name;
		this.comments = comments;
		this.rideId = rideId;
		this.from = "middletown";
		this.to="Palo Alto";
		this.emailId = "aub282@psu.edu";
		this.phoneNumber = "123-456-7890";
		this.name = "Niv Babuji";
		this.fromTime = "1:30 PM";
		this.comments = "Free ride open all time :D :D";
	}*/

	public Ride() {
		
	}
	
	public Ride(int rideId, String emailId, String phoneNumber, String name,
			String from, String to, String fromTime, String comments) {
		super();
		this.rideId = rideId;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.name = name;
		this.from = from;
		this.to = to;
		this.fromTime = fromTime;
		this.comments = comments;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}


	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getRideId() {
		return rideId;
	}

	public void setRideId(int rideId) {
		this.rideId = rideId;
	}

}