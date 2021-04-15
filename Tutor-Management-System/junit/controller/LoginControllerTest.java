package controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

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
		UserAccount newAccount = new UserAccount();
		newAccount.setAccountID(-1);
		newAccount.setUsername(username);
		newAccount.setPassword(password);
		
		db.insertUserAccount(newAccount);
		
		assertEquals(newAccount, controller.getUserFromLogin(username, password));
		
		db.deleteUserAccount(newAccount);
	}
}
