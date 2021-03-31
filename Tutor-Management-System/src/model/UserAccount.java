package model;

/**
 * The UserAccount class represents an account that can interact with
 * our application. All account types will inherit from the UserAccount class.
 */
public class UserAccount {
	
	private String username;
	private String password;
	private int accountID;
	
	/**
	 * Empty UserAccount Constructor
	 */
	public UserAccount() {
		
		username = null;
		password = null;
		accountID = -1;
	}
	
	/**
	 * Initialize UserAccount Constructor
	 * @param username This UserAccount's username
	 * @param password This UserAccount's password
	 * @param accountId This UserAccount's account ID
	 */
	public UserAccount(String username, String password, int accountID) {
		
		this.username = username;
		this.password = password;
		this.accountID = accountID;
	}
	
	/**
	 * Get this UserAccount's username.
	 * @return This UserAccount's username.
	 */
	public String getUsername() {
		
		return username;
	}
	
	/**
	 * Set this UserAccount's username.
	 * @param username This UserAccount's new username.
	 */
	public void setUsername(String username) {
		
		this.username = username;
	}
	
	/**
	 * Get this UserAccount's password.
	 * @return This UserAccount's password.
	 */
	public String getPassword() {
		
		return password;
	}
	
	/**
	 * Set this UserAccount's password.
	 * @param password This UserAccount's new password.
	 */
	public void setPassword(String password) {
		
		this.password = password;
	}
	
	/**
	 * Get this UserAccount's account ID.
	 * @return This UserAccount's account ID.
	 */
	public int getAccountID() {
		
		return accountID;
	}
	
	/**
	 * Set this UserAccount's account ID.
	 * @param accountId This UserAccount's new account ID.
	 */
	public void setAccountID(int accountID) {
		
		this.accountID = accountID;
	}
}