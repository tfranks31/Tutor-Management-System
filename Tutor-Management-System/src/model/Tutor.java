package model;

/**
 * The model for the tutor class of users. Contains tutors name, email, subject,
 * payrate and tutorID
 */

public class Tutor{
	private String name;
	private String email;
	private String subject;
	private double payRate;
	private int tutorID;
	private int accountID;
	private String accountNumber;
//	private PayVoucher payVoucher = new PayVoucher();
	private String studentID;

	// Constructors
	public Tutor() {
		name = null;
		email = null;
		subject = null;
		accountNumber = null;
		payRate = 0.0;
		tutorID = 0;
		studentID = null;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setTutorID(int tutorID) {
		this.tutorID = tutorID;
	}

	public int getTutorID() {
		return tutorID;
	}

	public void setPayRate(double payRate) {
		this.payRate = payRate;
	}

	public double getPayRate() {
		return payRate;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}

//	public void submitPayVoucher() {
//		payVoucher.setIsSubmitted(true);
//	}
//
//	public PayVoucher getPayVoucher() {
//		return payVoucher;
//	}

	public void setAccountNumber(String accountNumber){
		this.accountNumber = accountNumber;
	}

	public String getAccountNumber(){
		return accountNumber;
	}
	
	/**
	 * Get this Tutor's Student ID
	 * @return This Tutor's Student ID
	 */
	public String getStudentID() {
		
		return studentID;
	}
	
	/**
	 * Set this Tutor's Student ID.
	 * @param studentID This Tutor's new Student ID.
	 */
	public void setStudentID(String studentID) {
		
		this.studentID = studentID;
	}
	
	/**
	 * Get this Tutor's Account ID.
	 * @return This Tutor's Account ID.
	 */
	public int getAccountID() {
		
		return accountID;
	}
	
	/**
	 * Set this Tutor's Account ID.
	 * @param accountID This Tutor's new Account ID.
	 */
	public void setAccountID(int accountID) {
		
		this.accountID = accountID;
	}
}