package controller;

import model.UserAccount;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.DerbyDatabase;
import tutorsdb.persist.IDatabase;

/**
 * The LoginController class manages all user interactions with the Login page.
 */
public class LoginController {
	private IDatabase db = null;
	
	/**
	 * Refresh the database instance when constructed.
	 */
	public LoginController() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		db = DatabaseProvider.getInstance();
	}
	
	/**
	 * Get the UserAccount with the username and password.
	 * @param username UserAccount's username.
	 * @param password UserAccount's password.
	 * @return The UserAccount with the specified username and password.
	 */
	public UserAccount getUserFromLogin(String username, String password) {
		
		// Get the UserAccount with the login credentials
		UserAccount user = db.accountByLogin(username, password);
		
		return user;
	}
}