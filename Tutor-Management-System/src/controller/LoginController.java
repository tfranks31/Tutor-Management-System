package controller;

import model.UserAccount;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.FakeDatabase;
import tutorsdb.persist.IDatabase;

public class LoginController {
	private IDatabase db = null;
	
	public LoginController() {
		
		db = DatabaseProvider.getInstance();
	}
	
	public UserAccount getUserFromLogin(String username, String password) {
		
		UserAccount user = db.accountByLogin(username, password);
		
		return user;
	}
}
