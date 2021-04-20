package controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Tutor;
import model.UserAccount;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.FakeDatabase;
import tutorsdb.persist.IDatabase;

public class AddTutorControllerTest {

	private IDatabase db;
	private AddTutorController controller;
	
	@Before
    public void setUp() {
		
		// Set and get the tutordb instance and initialize AddTutorController
		DatabaseProvider.setInstance(new FakeDatabase());
		controller = new AddTutorController();
		db = DatabaseProvider.getInstance();		
    }
	
	@Test
	public void testAddTutor() {
		
		// Create and add objects to tutorsdb
		UserAccount newAccount = new UserAccount("user", "pass", -1, false);
		Tutor newTutor = new Tutor("user pass", "user@user.use", "use", 1, -1, -1, "321", "123");
		controller.addTutor(newAccount, newTutor);
		
		List<UserAccount> accountList = db.getUserAccounts();
		List<Tutor> tutorList = db.getTutors();
		
		// Newly added objects should be the last object in the list
		UserAccount dbAccount = accountList.get(accountList.size() - 1);
		Tutor dbTutor = tutorList.get(tutorList.size() - 1);
		
		// Check that the objects were successfully entered
		assertEquals(dbAccount.getUsername(), newAccount.getUsername());
		assertEquals(dbTutor.getName(), newTutor.getName());
		
		// Remove test objects from tutorsdb		
		db.deleteTutor(dbTutor);
		db.deleteUserAccount(dbAccount);
	}
}
