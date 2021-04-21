package tutorsdb.persist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Tutor;
import model.UserAccount;

public class DerbyDatabaseTests {

	private IDatabase db = null;

	@Before
	public void setUp() throws Exception {
		
		// creating DB instance here
		DatabaseProvider.setInstance(new DerbyDatabase());
		db = DatabaseProvider.getInstance();				
	}
	
	@Test
	public void testAddTutor() {
		
		UserAccount newAccount = new UserAccount("user", "pass", -1, false);
		Tutor newTutor = new Tutor("user pass", "user@user.use", "use", 1, -1, -1, "321", "123");
		
		db.addTutor(newAccount, newTutor);
		
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
	
	@Test
	public void testGetUserAccounts() {
		
		UserAccount newAccount = new UserAccount("user", "pass", -1, false);
		Tutor newTutor = new Tutor("user pass", "user@user.use", "use", 1, -1, -1, "321", "123");
		
		db.addTutor(newAccount, newTutor);
		
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
	
	@Test
	public void testGetTutor() {
		
		UserAccount newAccount = new UserAccount("user", "pass", -1, false);
		Tutor newTutor = new Tutor("user pass", "user@user.use", "use", 1, -1, -1, "321", "123");
		
		db.addTutor(newAccount, newTutor);
		
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
