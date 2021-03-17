package model;

/**
 * PayVoucher class.
 * Contains dueDate, totalHours, totalPay.
 * Can tell if the voucher has been submitted, Signed, edited by an admin or
 * if the voucher is new.
 */
public class PayVoucher {
    private String dueDate;
    private double totalHours;
    private double totalPay;
    private boolean isSubmitted;
    private boolean isSigned;
    private boolean isNew;
    private boolean isAdminEdited;

    public PayVoucher(){
        dueDate = "null";
        totalHours = 0.0;
        totalPay = 0.0;
        isSubmitted = false;
        isSigned = false;
        isNew = false;
        isAdminEdited = false;
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

    public void calculatePay(double hours, double payrate){
        totalPay = hours * payrate;
    }

    public double getTotalPay(){
        return totalPay;
    }

    public double getTotalHours(){
        return totalHours;
    }

    // Will need to be changed once entry class is created
    public void CalculateTotalHours(double hours){
        totalHours = hours;
    }

    // CalculateTotalHours
    // addEntry

}