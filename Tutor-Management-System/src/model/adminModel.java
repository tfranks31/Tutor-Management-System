package model;

/**
 * Admin class
 * 
 * The methods in this class will most likely
 * be put into a controller class but are being
 * left in the admin model class for now. 
 * 
 */

 tutorModel tutor = new tutorModel();

public class adminModel {

    // Constructor
    public adminModel() {
    }

    public void addTutor(String tutorName, String tutorEmail, double payRate
    String subject, int tutorID) {
        tutor.setName(tutorName);
        tutor.setEmail(tutorEmail);
        tutor.setPayRate(payRate);
        tutor.setSubject(subject);
        // tutor.setID(tutorID);
    } 

    public void removeTutor(int tutorID) {
  
    }

    public void editTutor() {

    }

    public void assignVoucher() {

    }

    public void editVoucher() {

    }
}