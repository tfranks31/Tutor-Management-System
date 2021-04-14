package model;

/**
 * PayVoucher class.
 * Contains dueDate, totalHours, totalPay.
 * Can tell if the voucher has been submitted, Signed, edited by an admin or
 * if the voucher is new.
 */
public class PayVoucher {

    private String dueDate;
    private String startDate;
    private double totalHours;
    private double totalPay;
    private boolean isSubmitted;
    private boolean isSigned;
    private boolean isNew;
    private boolean isAdminEdited;
    private int payVoucherID;
    private int tutorID;

    /**
     * Empty PayVoucher constructor.
     */
    public PayVoucher(){
        dueDate = null;
        startDate = null;
        totalHours = 0.0;
        totalPay = 0.0;
        isSubmitted = false;
        isSigned = false;
        isNew = false;
        isAdminEdited = false;
        payVoucherID = -1;
        tutorID = -1;
    }
    
    /**
     * Initialize PayVoucher constructor.
     * @param dueDate This PayVoucher's due date.
     * @param startDate This PayVoucher's start date.
     * @param totalHours This PayVoucher's total hours.
     * @param totalPay This PayVoucher's total pay.
     * @param isSubmitted Is this PayVoucher submitted?
     * @param isSigned Is this PayVoucher signed?
     * @param isNew Is this PayVoucher new?
     * @param isAdminEdited Is this PayVoucher admin edited.
     * @param payVoucherID This PayVoucher's pay voucher ID.
     * @param tutorID This PayVoucher's tutor ID.
     */
    public PayVoucher(String dueDate, String startDate, double totalHours,
    				  double totalPay, boolean isSubmitted, boolean isSigned,
    				  boolean isNew, boolean isAdminEdited, int payVoucherID,
    				  int tutorID){
    	
        this.dueDate = dueDate;
        this.startDate = startDate;
        this.totalHours = totalHours;
        this.totalPay = totalPay;
        this.isSubmitted = isSubmitted;
        this.isSigned = isSigned;
        this.isNew = isNew;
        this.isAdminEdited = isAdminEdited;
        this.payVoucherID = payVoucherID;
        this.tutorID = tutorID;
    }

    /**
     * Set if this PayVoucher is submitted.
     * @param isSubmitted Whether or not this PayVoucher is now submitted.
     */
    public void setIsSubmitted(boolean isSubmitted){
        this.isSubmitted = isSubmitted;
    }

    /**
     * Get if this PayVoucher is submitted.
     * @return Whether or not this PayVoucher is submitted.
     */
    public boolean getIsSubmitted(){
        return isSubmitted;
    }

    /**
     * Set if this PayVoucher is signed.
     * @param isSigned Whether or not this PayVoucher is now signed.
     */
    public void setIsSigned(boolean isSigned){
        this.isSigned = isSigned;
    }

    /**
     * Get if this PayVoucher is signed.
     * @return Whether or not this PayVoucher is signed.
     */
    public boolean getIsSigned(){
        return isSigned;
    }

    /**
     * Set if this PayVoucher is new.
     * @param isNew Whether or not this PayVoucher is now new.
     */
    public void setIsNew(boolean isNew){
        this.isNew = isNew;
    }

    /**
     * Get if this PayVoucher is new.
     * @return Whether or not this PayVoucher is new.
     */
    public boolean getIsNew(){
        return isNew;
    }

    /**
     * Set if this PayVoucher is admin edited.
     * @param isAdminEdited Whether or not this PayVoucher is now admin edited.
     */
    public void setIsAdminEdited(boolean isAdminEdited){
        this.isAdminEdited = isAdminEdited;
    }

    /**
     * Get if this PayVoucher is admin edited.
     * @return Whether or not this PayVoucher is admin edited.
     */
    public boolean getIsAdminEdited(){
        return isAdminEdited;
    }

    /**
     * Set this PayVoucher's due date.
     * @param dueDate This PayVoucher's new due date.
     */
    public void setDueDate(String dueDate){
        this.dueDate = dueDate;
    }

    /**
     * Get this PayVoucher's due date.
     * @return This PayVoucher's due date.
     */
    public String getDueDate(){
        return dueDate;
    }

    /**
     * Get this PayVoucher's total pay.
     * @return This PayVoucher's total pay.
     */
    public double getTotalPay(){
        return totalPay;
    }
    
    /**
     * Get this PayVoucher's total pay.
     * @param totalPay This PayVoucher's new total pay.
     */
    public void setTotalPay(double totalPay) {
    	this.totalPay = totalPay;
    }

    /**
     * Set this PayVoucher's total hours.
     * @param totalHours This PayVoucher's new total hours.
     */
    public void setTotalHours(double totalHours){
        this.totalHours = totalHours; 
    }

    /**
     * Get this PayVoucher's total hours.
     * @return This PayVoucher's total hours.
     */
    public double getTotalHours(){
        return totalHours;
    }
    
    /**
     * Get this PayVoucher's starting date.
     * @return This PayVoucher's starting date.
     */
    public String getStartDate() {
    	
    	return startDate;
    }
    
    /**
     * Set this PayVoucher's starting date.
     * @param startDate This PayVoucher's starting date.
     */
    public void setStartDate(String startDate) {
    	
    	this.startDate = startDate;
    }
    
    /**
     * Get this PayVoucher's pay voucher ID.
     * @return This PayVoucher's pay voucher ID.
     */
    public int getPayVoucherID() {
    	
    	return payVoucherID;
    }
    
    /**
     * Set this PayVoucher's pay voucher ID.
     * @param payVoucherID This PayVoucher's new pay voucher ID.
     */
    public void setPayVoucherID(int payVoucherID) {
    	
    	this.payVoucherID = payVoucherID;
    }
    
    /**
     * Get this PayVoucher's tutor ID.
     * @return This Payvoucher's tutor ID.
     */
    public int getTutorID() {
    	
    	return tutorID;
    }
    
    /**
     * Set this PayVoucher's tutor ID.
     * @param accountID This PayVoucher's new tutor ID.
     */
    public void setTutorID(int tutorID) {
    	
    	this.tutorID = tutorID;
    }
}