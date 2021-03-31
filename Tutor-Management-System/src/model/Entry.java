package model;

/**
 * Entry class. Contains Date, servicePerformed, wherePerformed, and hours
 * Stores the information for each individual entry of a pay voucher
 */

public class Entry {

	private String date, servicePerformed, wherePerformed;
	private double hours;
	private int entryID;
	private int payVoucherID;
	
	public Entry(){
		date = "null";
		servicePerformed = "null";
		wherePerformed = "null";
		hours = 0.0;
		entryID = -1;
		payVoucherID = -1;
	}
	
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
	
	/**
	 * Get this Entry's entry ID.
	 * @return This Entry's entry ID.
	 */
	public int getEntryID() {
		
		return entryID;
	}
	
	/**
	 * Set this Entry's entry ID.
	 * @param entryID This Entry's new entry ID.
	 */
	public void setEntryID(int entryID) {
		
		this.entryID = entryID;
	}
	
	/**
	 * Get this Entry's pay voucher ID.
	 * @return This Entry's pay voucher ID.
	 */
	public int getPayVoucherID() {
		
		return payVoucherID;
	}
	
	/**
	 * Set this Entry's pay voucher ID.
	 * @param payVoucherID This Entry's new pay voucher ID.
	 */
	public void setPayVoucherID(int payVoucherID) {
		
		this.payVoucherID = payVoucherID;
	}
}
