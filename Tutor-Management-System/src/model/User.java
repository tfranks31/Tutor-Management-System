package model;

/**
 * The User abstract class represents an account that can interact with our
 * application. All account types will inherit from the User class.
 */
public abstract class User {
	
	private String username;
	private String password;
	
	/**
	 * Get this User's username.
	 * @return The User's username.
	 */
	public String getUserName() {
		
		return username;
	}
	
	/**
	 * Set this User's username.
	 * @param username The User's new username
	 */
	public void setUserName(String username) {
		
		this.username = username;
	}
	
	/**
	 * Get this User's Password.
	 * @return The User's Password.
	 */
	public String getPassword() {
		
		return password;
	}
	
	/**
	 * Set this User's password
	 * @param password The User's new password
	 */
	public void setPassword(String password) {
		
		this.password = password;
	}
}