package controller;

import model.Pair;
import model.Tutor;
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
	
	/**
	 * Get the UserAccount and Tutor with the username and studentID.
	 * @param username The UserAccount's username.
	 * @param studentID The Tutor's studentID.
	 * @return Both the UserAccount and Tutor with the respecting username and studentID.
	 */
	public Pair<UserAccount, Tutor> getUserTutorFromForgotPassword(String username, String studentID) {
		
		// Search through tutors and users checking if they are found
		for (Pair<UserAccount, Tutor> userTutor : db.getAllUserTutor()) {
			
			if (userTutor.getLeft().getUsername().equals(username) && userTutor.getRight().getStudentID().equals(studentID)) {
				
				return userTutor;
			}
		}
		
		return null;
	}
	
	/**
	 * Update the specified UserAccount with the specified password.
	 * @param account The UserAccount to update.
	 * @param password The UserAccount's new password.
	 */
	public void updatePassword(UserAccount account, String password) {
		
		db.updatePasswordWithUserID(account, password);
	}
}