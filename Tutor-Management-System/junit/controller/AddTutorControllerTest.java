package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Tutor;
import model.UserAccount;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.DerbyDatabase;
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
	
	@Test
	public void testEditTutor() {
		
		UserAccount newAccount = new UserAccount("user", "pass", -1, false);
		Tutor newTutor = new Tutor("user pass", "user@user.use", "use", 1, -1, -1, "321", "123");
		controller.addTutor(newAccount, newTutor);
		
		List<UserAccount> accountList = db.getUserAccounts();
		List<Tutor> tutorList = db.getTutors();
		
		// Newly added objects should be the last object in the list
		UserAccount dbAccount = accountList.get(accountList.size() - 1);
		Tutor dbTutor = tutorList.get(tutorList.size() - 1);
		
		dbAccount.setUsername("someUsername");
		dbTutor.setName("someName");
		
		controller.editTutor(dbAccount, dbTutor);
		
		accountList = db.getUserAccounts();
		tutorList = db.getTutors();
		
		// Newly added objects should be the last object in the list
		dbAccount = accountList.get(accountList.size() - 1);
		dbTutor = tutorList.get(tutorList.size() - 1);
		
		assertEquals("someUsername", dbAccount.getUsername());
		assertEquals("someName", dbTutor.getName());
		
		// Remove test objects from tutorsdb		
		db.deleteTutor(dbTutor);
		db.deleteUserAccount(dbAccount);
	}
	
	@Test
	public void testUpdatePasswordWithUserID() {
		
		UserAccount newAccount = new UserAccount("user", "pass", -1, false);
		db.insertUserAccount(newAccount);
		
		List<UserAccount> accountList = db.getUserAccounts();
		
		// Newly added objects should be the last object in the list
		UserAccount dbAccount = accountList.get(accountList.size() - 1);
		
		// Check that the objects were successfully entered
		assertEquals(dbAccount.getUsername(), newAccount.getUsername());
		assertEquals(dbAccount.getPassword(), newAccount.getPassword());
		assertEquals(dbAccount.getIsAdmin(), newAccount.getIsAdmin());
		//update the password
		controller.updatePassword(dbAccount, "newpassword");
		//get the account again
		accountList = db.getUserAccounts();
		dbAccount = accountList.get(accountList.size() - 1);
		
		assertEquals(dbAccount.getUsername(), newAccount.getUsername());
		assertEquals(dbAccount.getPassword(), "newpassword");
		assertEquals(dbAccount.getIsAdmin(), newAccount.getIsAdmin());
		
		// Remove test objects
		db.deleteUserAccount(dbAccount);
	}
	
	@Test
	public void testFindStudentID() {
		
		Tutor newTutor = new Tutor("user pass", "user@user.use", "use", 1, -1, 1, "321", "123");
		db.insertTutor(newTutor);
		
		List<Tutor> tutorList = db.getTutors();
		
		// Newly added objects should be the last object in the list
		Tutor dbTutor = tutorList.get(tutorList.size() - 1);
		
		assertTrue(controller.findStudentID(dbTutor.getStudentID(), dbTutor, true));
		assertFalse(controller.findStudentID(dbTutor.getStudentID(), dbTutor, false));
		
		// Remove test objects
		db.deleteTutor(dbTutor);
	}
	
	@Test
	public void testFindEmail() {
		
		Tutor newTutor = new Tutor("user pass", "user@user.use", "use", 1, -1, 1, "321", "123");
		db.insertTutor(newTutor);
		
		List<Tutor> tutorList = db.getTutors();
		
		// Newly added objects should be the last object in the list
		Tutor dbTutor = tutorList.get(tutorList.size() - 1);
		
		assertTrue(controller.findEmail(dbTutor.getEmail(), dbTutor, true));
		assertFalse(controller.findEmail(dbTutor.getEmail(), dbTutor, false));
		
		// Remove test objects
		db.deleteTutor(dbTutor);
	}
	
	@Test
	public void testFindUsername() {
		
		UserAccount newAccount = new UserAccount("user", "pass", -1, false);
		db.insertUserAccount(newAccount);
		
		List<UserAccount> accountList = db.getUserAccounts();
		
		// Newly added objects should be the last object in the list
		UserAccount dbAccount = accountList.get(accountList.size() - 1);
		
		assertTrue(controller.findUsername(dbAccount.getUsername(), dbAccount, true));
		assertFalse(controller.findUsername(dbAccount.getUsername(), dbAccount, false));
		
		// Remove test objects
		db.deleteUserAccount(dbAccount);
	}
}
