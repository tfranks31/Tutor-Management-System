package controller;

import model.Tutor;
import model.UserAccount;

import java.util.List;

import model.Pair;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.DerbyDatabase;
import tutorsdb.persist.IDatabase;

/**
 * The AddTutorController class manages all user interactions with the AddTutor 
 * page.
 */
public class AddTutorController {
	
	private IDatabase db = null;
	
	/**
	 * Refresh the database instance when constructed.
	 */
	public AddTutorController() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		db = DatabaseProvider.getInstance();
	}
	
	/**
	 * Add a new Tutor and their UserAccount to the tutorsdb.
	 * @param account New Tutor's UserAccount.
	 * @param tutor	New Tutor.
	 */
	public void addTutor(UserAccount account, Tutor tutor){
		
		db.addTutor(account, tutor);
	}
	
	/**
	 * Returns an instance of a tutor and account with respective name 
	 * @param name name of tutor who info is requested
	 * @return A pair consiting of a tutor and it's respective UserAccount
	 */
	public Pair<UserAccount, Tutor> getTutorInfo(String name) {
		Pair<UserAccount, Tutor> userTutor;
		userTutor = db.getTutorInfo(name);
		return userTutor;
	}
	
	/**
	 * Updates the Tutor's account and user information in the database
	 * @param account Useraccount of the Tutor
	 * @param tutor Tutor object of the repective tutor
	 */
	public void editTutor(UserAccount account, Tutor tutor) {
		db.editTutor(account, tutor);
	}
	
	/**
	 * Utility method that updates the password of a tutor with request of the tutor
	 * @param User userAccount of session user
	 * @param Password string of updated password 
	 */
	public void updatePassword(UserAccount User, String Password) {
		db.updatePasswordWithUserID(User, Password);
	}
	
	/**
	 * Check to see if the database already has the inputed student id in use.
	 * @param studentID Student id to look for.
	 * @param selectedTutor The tutor currently being viewed.
	 * @param isAddTutor Whether or not a tutor is being added.
	 * @return True if the student ID exists and if owned by a different tutor, false if it does not.
	 */
	public boolean findStudentID(String studentID, Tutor selectedTutor, boolean isAddTutor) {
		
		List<Tutor> tutorList = db.getTutors();
		
		// Search through all tutors and check their student ids
		for (Tutor tutor : tutorList) {
			
			// Student id is found
			if (tutor.getStudentID().equals(studentID)) {
				
				// The user is adding a tutor, which means that the id found is by default in use by another tutor
				if (isAddTutor) {
					
					return true;
				}		
				
				// The user is editing a tutor, which means that the id found either belongs to the selected tutor
				// or is owned by another tutor
				else if (selectedTutor != null && !isAddTutor && tutor.getTutorID() != selectedTutor.getTutorID()) {
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Check to see if the database already has the inputed email in use.
	 * @param email	Email to look for.
	 * @param selectedTutor The tutor currently being viewed.
	 * @param isAddTutor Whether or not a tutor is being added.
	 * @return True if the email exists and is owned by a different tutor, false if it does not.
	 */
	public boolean findEmail(String email, Tutor selectedTutor, boolean isAddTutor) {
		
		List<Tutor> tutorList = db.getTutors();
		
		// Search through all tutors and check their emails
		for (Tutor tutor : tutorList) {
			
			// Email is found
			if (tutor.getEmail().equals(email)) {
				
				// The user is adding a tutor, which means that the email found is by default in use by another tutor
				if (isAddTutor) {
					
					return true;
				}			
				
				// The user is editing a tutor, which means that the email found either belongs to the selected tutor
				// or is owned by another tutor
				else if (selectedTutor != null && !isAddTutor && tutor.getTutorID() != selectedTutor.getTutorID()) {
					
					return true;
				}		
			}
		}
		
		return false;
	}
	
	/**
	 * Check to see if the database already has the generated username in use.
	 * @param username Username to look for.
	 * @param selectedAccount The account of the tutor currently being viewed.
	 * @param isAddTutor Whether or not a tutor is being added.
	 * @return True if the username exists and is owned by a different user, false if it does not.
	 */
	public boolean findUsername(String username, UserAccount selectedAccount, boolean isAddTutor) {
		
		List<UserAccount> userList = db.getUserAccounts();
		
		// Search through all user accounts and check their usernames
		for (UserAccount user : userList) {
			
			// Username is found
			if (user.getUsername().equals(username)) {
				
				// The user is adding a tutor, which means that the username found is by default in use by another account
				if (isAddTutor) {
					
					return true;
				}
				
				// The user is editing a tutor, which means that the username found either belongs to the selected account
				// or is owned by another account.
				else if (selectedAccount != null && !isAddTutor && user.getAccountID() != selectedAccount.getAccountID()) {
					
					return true;
				}				
			}
		}
		
		return false;
	}
}