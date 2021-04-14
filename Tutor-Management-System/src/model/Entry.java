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
	
	/**
	 * Empty entry constructor.
	 */
	public Entry(){
		date = null;
		servicePerformed = null;
		wherePerformed = null;
		hours = 0.0;
		entryID = -1;
		payVoucherID = -1;
	}
	
	/**
	 * Intialize Entry Constructor.
	 * @param date This Entry's date.
	 * @param servicePerformed This Entry's service performed.
	 * @param wherePerformed This Entry's where performed.
	 * @param hours This Entry's hours.
	 * @param entryID This Entry's entry ID.
	 * @param payVoucherID This Entry's pay voucher ID.
	 */
	public Entry(String date, String servicePerformed, String wherePerformed,
				 double hours, int entryID, int payVoucherID) {
		
		this.date = date;
		this.servicePerformed = null;
		this.wherePerformed = wherePerformed;
		this.hours = hours;
		this.entryID = entryID;
		this.payVoucherID = payVoucherID;
	}
	
	/**
	 * Get this Entry's date.
	 * @return This Entry's date.
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * Set this Entry's date
	 * @param date This Entry's new date
	 */
	public void setDate(String date) {
		this.date = date;
	}
	
	/**
	 * Get this Entry's service performed.
	 * @return This Entry's service performed
	 */
	public String getServicePerformed() {
		return servicePerformed;
	}
	
	/**
	 * Set this Entry's service performed.
	 * @param servicePerformed This Entry's new serviced performed.
	 */
	public void setServicePerformed(String servicePerformed) {
		this.servicePerformed = servicePerformed;
	}
	
	/**
	 * Get this Entry's where performed.
	 * @return This Entry's where performed.
	 */
	public String getWherePerformed() {
		return wherePerformed;
	}
	
	/**
	 * Set this Entry's where performed.
	 * @param wherePerfromed This Entry's new where performed
	 */
	public void setWherePerformed(String wherePerfromed) {
		this.wherePerformed = wherePerfromed;
	}
	
	/**
	 * Get this Entry's hours.
	 * @return This Entry's hours
	 */
	public double getHours() {
		return hours;
	}
	
	/**
	 * Set this Entry's hours.
	 * @param hours This Entry's new hours.
	 */
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