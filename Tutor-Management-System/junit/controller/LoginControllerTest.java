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

public class LoginControllerTest {
	
	private IDatabase db;
	private LoginController controller;
	
	@Before
    public void setUp() {
		
		DatabaseProvider.setInstance(new FakeDatabase());
		db = DatabaseProvider.getInstance();
		controller = new LoginController();
    }
	
	@Test
	public void testGetUserFromLogin() {
		
		String username = "test";
		String password = "test";
		db.addTutor("test", "test", username, password, "test", "test", "test", "test", 1);
		db.getUserAccounts();
		
		List<UserAccount> accountList = db.getUserAccounts();
		List<Tutor> tutorList = db.getTutors();
		
		UserAccount newAccount = accountList.get(accountList.size() - 1);
		Tutor newTutor = tutorList.get(tutorList.size() - 1);
		
		assertEquals(newAccount, controller.getUserFromLogin(username, password));
		
		db.deleteUserAccount(newAccount);
		db.deleteTutor(newTutor);
	}
}
