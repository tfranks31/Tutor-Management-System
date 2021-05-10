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
	
	public boolean findStudentID(String studentID, Tutor selectedTutor, boolean isAddTutor) {
		
		List<Tutor> tutorList = db.getTutors();
		for (Tutor tutor : tutorList) {
			
			if (tutor.getStudentID().equals(studentID)) {
				
				if (isAddTutor) {
					
					return true;
				}				
				else if (selectedTutor != null && !isAddTutor && tutor.getTutorID() != selectedTutor.getTutorID()) {
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean findEmail(String email, Tutor selectedTutor, boolean isAddTutor) {
		
		List<Tutor> tutorList = db.getTutors();
		for (Tutor tutor : tutorList) {
			
			if (tutor.getEmail().equals(email)) {
				
				if (isAddTutor) {
					
					return true;
				}				
				else if (selectedTutor != null && !isAddTutor && tutor.getTutorID() != selectedTutor.getTutorID()) {
					
					return true;
				}		
			}
		}
		
		return false;
	}
	
	public boolean findUsername(String username, UserAccount selectedAccount, boolean isAddTutor) {
		
		List<UserAccount> userList = db.getUserAccounts();
		for (UserAccount user : userList) {
			
			if (user.getUsername().equals(username)) {
				
				if (isAddTutor) {
					
					return true;
				}
				else if (selectedAccount != null && !isAddTutor && user.getAccountID() != selectedAccount.getAccountID()) {
					
					return true;
				}				
			}
		}
		
		return false;
	}
}