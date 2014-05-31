package com.example.carpool.model;

/**
 * 
 * @author ambika_b
 *
 */
public class City {

	private String cityName;
	
	private int cityId;
	
	public City(){}
	
	public City(int cityId, String cityName) {
		this.cityId = cityId;
		this.cityName = cityName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String toString() {
		return this.cityName;
	}
}