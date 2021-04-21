package tutorsdb.persist;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
	public void testDeleteTutor() {
		UserAccount account = new UserAccount();
		Tutor tutor = new Tutor();
		int count = 0;
		
		account.setUsername("username");
		account.setPassword("password");
		tutor.setName("SuperRandomNameWeWillNeverUse");
		tutor.setTutorID(123456789);
		
		db.addTutor(account, tutor);
		
		Tutor testTutor = db.getTutors().get(db.getTutors().size() - 1);
		assertTrue(testTutor.getName().equals("SuperRandomNameWeWillNeverUse"));
		
		db.deleteTutor(testTutor);
		db.deleteUserAccount(account);
		
		for (int i = 0; i < db.getTutors().size(); i++) {
			if (db.getTutors().get(i).getName().equals("SuperRandomNameWeWillNeverUse")) {
				count++;
			}
		}
		
		assertEquals(count, 0);
	}
}
