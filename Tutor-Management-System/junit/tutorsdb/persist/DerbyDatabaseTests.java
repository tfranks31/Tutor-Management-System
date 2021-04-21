package tutorsdb.persist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Entry;
import model.PayVoucher;
import model.Tuple;
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
		
		db.insertUserAccount(newAccount);
		
		List<UserAccount> accountList = db.getUserAccounts();
		
		// Newly added objects should be the last object in the list
		UserAccount dbAccount = accountList.get(accountList.size() - 1);
		
		// Check that the objects were successfully entered
		assertEquals(dbAccount.getUsername(), newAccount.getUsername());
		
		// Remove test objects from tutorsdb
		db.deleteUserAccount(dbAccount);
	}
	
	@Test
	public void testGetTutor() {
		
		Tutor newTutor = new Tutor("user pass", "user@user.use", "use", 1, -1, 1, "321", "123");
		
		db.insertTutor(newTutor);
		
		List<Tutor> tutorList = db.getTutors();
		
		// Newly added objects should be the last object in the list
		Tutor dbTutor = tutorList.get(tutorList.size() - 1);
		
		// Check that the objects were successfully entered
		assertEquals(dbTutor.getName(), newTutor.getName());
		
		// Remove test objects from tutorsdb
		db.deleteTutor(dbTutor);
	}
	
	@Test
	public void testInsertUserAccount() {
		
		UserAccount newAccount = new UserAccount("user", "pass", -1, false);
		
		db.insertUserAccount(newAccount);
		
		List<UserAccount> accountList = db.getUserAccounts();
		
		// Newly added objects should be the last object in the list
		UserAccount dbAccount = accountList.get(accountList.size() - 1);
		
		// Check that the objects were successfully entered
		assertEquals(dbAccount.getUsername(), newAccount.getUsername());
		
		// Remove test objects from tutorsdb
		db.deleteUserAccount(dbAccount);
	}
	
	@Test
	public void testInsertTutor() {
		
		Tutor newTutor = new Tutor("user pass", "user@user.use", "use", 1, -1, 1, "321", "123");
		
		db.insertTutor(newTutor);
		
		List<Tutor> tutorList = db.getTutors();
		
		// Newly added objects should be the last object in the list
		Tutor dbTutor = tutorList.get(tutorList.size() - 1);
		
		// Check that the objects were successfully entered
		assertEquals(dbTutor.getName(), newTutor.getName());
		
		// Remove test objects from tutorsdb
		db.deleteTutor(dbTutor);
	}
	
	@Test
	public void testFindVoucherByEntry() {
		
		Tutor newTutor = new Tutor("user pass", "user@user.use", "use", 1.0, -1, 1, "321", "123");
		db.insertTutor(newTutor);
		Tutor dbTutor = db.getTutors().get(db.getTutors().size() - 1);
		
		PayVoucher newPayVoucher = new PayVoucher("03/04/2021", "03/02/2021", 0, 0, false, false, false, false, -1, dbTutor.getTutorID());
		db.insertPayVoucher(newPayVoucher);
		PayVoucher dbPayVoucher = db.getPayVouchers().get(db.getPayVouchers().size() - 1);
		
		Entry newEntry = new Entry("03/03/2021", "Tutoring", "zoom", 1, -1, dbPayVoucher.getPayVoucherID());
		db.insertEntry(newEntry);
		Entry dbEntry = db.getEntries().get(db.getEntries().size() - 1);
		
		List<Tuple<Tutor, PayVoucher, Entry>> tupleList = db.findEntryByVoucher((dbPayVoucher.getPayVoucherID()));
		Tuple<Tutor, PayVoucher, Entry> tuple = tupleList.get(tupleList.size() - 1);
		
		assertEquals(newTutor.getName(), tuple.getLeft().getName());
		assertEquals(newPayVoucher.getDueDate(), tuple.getMiddle().getDueDate());
		assertEquals(newEntry.getDate(), tuple.getRight().getDate());
		
		db.deleteEntry(dbEntry);
		db.deletePayVoucher(dbPayVoucher);
		db.deleteTutor(dbTutor);
	}
}
