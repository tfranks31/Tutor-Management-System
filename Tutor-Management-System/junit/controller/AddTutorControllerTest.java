package controller;

import static org.junit.Assert.assertTrue;

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
		
		DatabaseProvider.setInstance(new FakeDatabase());
		db = DatabaseProvider.getInstance();
		controller = new AddTutorController();
    }
	
	@Test
	public void testAddTutor() {
		
		String firstname = "Add";
		String lastname = "Tutor";
		String username = "addTutor";
		String password = "addTutor";
		String email = "addTutor";
		String studentID = "1234";
		String accountNumber = "123";
		String subject = "tutor";
		double payRate = 7.50;
		
		controller.addTutor(firstname, lastname, username, password, email, studentID, accountNumber, subject, payRate);
		
		List<UserAccount> accountList = db.getUserAccounts();
		List<Tutor> tutorList = db.getTutors();
		
		UserAccount newAccount = accountList.get(accountList.size() - 1);
		Tutor newTutor = tutorList.get(tutorList.size() - 1);
		
		assertTrue(newAccount.getUsername().equals(username));
		assertTrue(newAccount.getPassword().equals(password));
		assertTrue(newTutor.getName().equals(firstname + " " + lastname));
		assertTrue(newTutor.getEmail().equals(email));
		assertTrue(newTutor.getStudentID().equals(studentID));
		assertTrue(newTutor.getAccountNumber().equals(accountNumber));
		assertTrue(newTutor.getSubject().equals(subject));
		assertTrue(newTutor.getPayRate() == payRate);
		
		db.deleteUserAccount(newAccount);
		db.deleteTutor(newTutor);
	}
}
