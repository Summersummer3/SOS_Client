package com.example.layoutt;

public class Location {
	private double Latitude,Longitude;
	private String time;
	
	public Location(double Latitude,double Longitude,String time){
		this.Latitude = Latitude;
		this.Longitude = Longitude;
		this.time = time;
		}

	public double getLatitude() {
		return Latitude;
	}

	public double getLongitude() {
		return Longitude;
	}

	public String getTime() {
		return time;
	}
	
	
}
