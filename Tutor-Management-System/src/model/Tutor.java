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
	private String studentID;

	/**
	 *  Empty Tutor constructor
	 */
	public Tutor() {
		name = null;
		email = null;
		subject = null;
		accountNumber = null;
		payRate = 0.0;
		tutorID = 0;
		studentID = null;
	}
	
	/**
	 * Initialize Tutor constructor.
	 * @param name This Tutor's name.
	 * @param email This Tutor's email.
	 * @param subject This Tutor's subject.
	 * @param payRate This Tutor's pay rate.
	 * @param tutorID This Tutor's tutor ID.
	 * @param accountID This Tutor's account ID.
	 * @param accountNumber This Tutor's account number.
	 * @param studentID This Tutor's student ID.
	 */
	public Tutor(String name, String email, String subject, double payRate,
				 int tutorID, int accountID, String accountNumber, 
				 String studentID) {
		
		this.name = name;
		this.email = email;
		this.subject = subject;
		this.accountNumber = accountNumber;
		this.payRate = payRate;
		this.tutorID = tutorID;
		this.studentID = studentID;
	}

	/**
	 * Set this Tutor's name.
	 * @param name This Tutor's new name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get this Tutor's name.
	 * @return This Tutor's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set this Tutor's email.
	 * @param email This Tutor's new email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Get this Tutor's email.
	 * @return This Tutor's email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set this Tutor's tutor ID.
	 * @param tutorID This Tutor's new tutor ID.
	 */
	public void setTutorID(int tutorID) {
		this.tutorID = tutorID;
	}

	/**
	 * Get this Tutor's tutor ID.
	 * @return This Tutor's tutor ID.
	 */
	public int getTutorID() {
		return tutorID;
	}

	/**
	 * Set this Tutor's pay rate.
	 * @param payRate This Tutor's new pay rate.
	 */
	public void setPayRate(double payRate) {
		this.payRate = payRate;
	}

	/**
	 * Get this Tutor's pay rate.
	 * @return This Tutor's pay rate.
	 */
	public double getPayRate() {
		return payRate;
	}

	/**
	 * Set this Tutor's subject.
	 * @param subject This Tutor's new subject.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Get this Tutor's subject.
	 * @return This tutor's subject.
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Set this Tutor's account number.
	 * @param accountNumber This Tutor's new account number.
	 */
	public void setAccountNumber(String accountNumber){
		this.accountNumber = accountNumber;
	}

	/**
	 * Get this Tutor's account number.
	 * @return This Tutor's account number.
	 */
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