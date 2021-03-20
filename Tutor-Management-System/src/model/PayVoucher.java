package model;

import java.util.ArrayList;

/**
 * PayVoucher class.
 * Contains dueDate, totalHours, totalPay.
 * Can tell if the voucher has been submitted, Signed, edited by an admin or
 * if the voucher is new.
 */

public class PayVoucher {
    
    private final int defaultVoucherSize = 10; //10 entries per blank voucher

    private String dueDate;
    private double totalHours;
    private double totalPay;
    private boolean isSubmitted;
    private boolean isSigned;
    private boolean isNew;
    private boolean isAdminEdited;
    private ArrayList<Entry> entries = new ArrayList<Entry>();

    public PayVoucher(){
        dueDate = "null";
        totalHours = 0.0;
        totalPay = 0.0;
        isSubmitted = false;
        isSigned = false;
        isNew = false;
        isAdminEdited = false;

        //initializes default number of entries per voucher, currently 10
        for (int i = 0; i < defaultVoucherSize; i++){
            addEntry();
        }
    }

    public void setIsSubmitted(boolean isSubmitted){
        this.isSubmitted = isSubmitted;
    }

    public boolean getIsSubmitted(){
        return isSubmitted;
    }

    public void setIsSigned(boolean isSigned){
        this.isSigned = isSigned;
    }

    public boolean getIsSigned(){
        return isSigned;
    }

    public void setIsNew(boolean isNew){
        this.isNew = isNew;
    }

    public boolean getIsNew(){
        return isNew;
    }

    public void setIsAdminEdited(boolean isAdminEdited){
        this.isAdminEdited = isAdminEdited;
    }

    public boolean getIsAdminEdited(){
        return isAdminEdited;
    }

    public void setDueDate(String dueDate){
        this.dueDate = dueDate;
    }

    public String getDueDate(){
        return dueDate;
    }

    public void calculatePay(double hours, double payRate){
        totalPay = hours * payRate;
    }

    public double getTotalPay(){
        return totalPay;
    }

    public void setTotalHours(double totalHours){
        this.totalHours = totalHours; 
    }

    public double getTotalHours(){
        return totalHours;
    }

    public void entryValues() {
    	for (Entry entry: entries) {
    		System.out.println("entry:" + entry.getHours());
    	}
    }
    
    //iterates over every entry in the voucher, sums the hours
    public void CalculateTotalHours(){
    	entryValues();
    	for (Entry entry: entries){
        	System.out.println(totalHours + "   " + entry.getHours());
            totalHours += entry.getHours();
        }
        System.out.println(totalHours);
    }

    // generates a blank entry object and adds it to the array list
    public void addEntry(){
        Entry entry = new Entry();
        entries.add(entry); 
    }

    public Entry getEntry(int entryNum){
        return entries.get(entryNum);
    } 
    
    public void setEntry(int entryNum, Entry entry){
       entries.set(entryNum, entry);
    }
}
