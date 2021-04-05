package controller;

import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.FakeDatabase;
import tutorsdb.persist.IDatabase;

public class AddTutorController {
	private IDatabase db = null;
	
	public AddTutorController() {
		
		db = DatabaseProvider.getInstance();
	}
	
	public void addTutor(String firstname, String lastname, String username, String password,
			String email, String studentID, String accountNumber, String subject, double payRate){
		
		db.AddTutor(firstname, lastname, username, password, email, studentID, accountNumber, subject, payRate);
		
	}
}
