package model;
/**
 * The model for the tutor class of users.
 * Contains tutors name, email, subject, payrate and tutorID
 */

public class Tutor extends User {
    private String name;
    private String email;
    private String subject;
    private double payRate;
    private int tutorID;
    private PayVoucher payVoucher = new PayVoucher();

    //Constructors
    public Tutor(){
        name = "null";
        email = "null";
        subject = "null";
        payRate = 0.0;
        tutorID = 0;
    }

    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
  
    public void setEmail(String email){
        this.email = email;
    }
    
    public String getEmail(){
        return email;
    }

    public void setPayRate(double payRate){
        this.payRate = payRate;
    }
    
    public double getPayRate(){
        return payRate;
    }
    
    public void setSubject(String subject){
        this.subject = subject;
    }
    
    public String getSubject(){
        return subject;
    }
    
    public void submitPayVoucher(){
        payVoucher.setIsSubmitted(true);
    }
    
    public PayVoucher getPayVoucher(){
        return payVoucher;
    }

    //editPayVoucher()

}