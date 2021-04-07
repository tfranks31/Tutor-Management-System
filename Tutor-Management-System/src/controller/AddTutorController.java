package controller;

import model.Tutor;
import model.UserAccount;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.IDatabase;

/**
 * The AddTutorController class manages all database interactions with the 
 * AddTutor page.
 */
public class AddTutorController {
	
	private IDatabase db = null;
	
	/**
	 * Refresh the database instance when constructed.
	 */
	public AddTutorController() {
		
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
}