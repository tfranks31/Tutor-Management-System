package model;

public class Entry {

	private String date, servicePerformed, wherePerformed;
	private double hours;
	
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getServicePerformed() {
		return servicePerformed;
	}
	
	public void setServicePerformed(String servicePerformed) {
		this.servicePerformed = servicePerformed;
	}
	
	public String getWherePerformed() {
		return wherePerformed;
	}
	
	public void setWherePerformed(String wherePerfromed) {
		this.wherePerformed = wherePerfromed;
	}
	
	public double getHours() {
		return hours;
	}
	
	public void setHours(double hours) {
		this.hours = hours;
	}
	
}
