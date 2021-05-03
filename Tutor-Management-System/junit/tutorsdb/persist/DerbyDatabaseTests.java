package tutorsdb.persist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import model.Entry;
import model.Pair;
import model.PayVoucher;
import model.Tuple;
import model.Tutor;
import model.UserAccount;

public class DerbyDatabaseTests {

	private IDatabase db = null;

	@Before
	public void setUp() throws Exception {
		
		// creating DB instance here
		//DatabaseProvider.setInstance(new FakeDatabase());
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
		assertEquals(dbAccount.getPassword(), newAccount.getPassword());
		assertEquals(dbAccount.getIsAdmin(), newAccount.getIsAdmin());
		assertEquals(dbTutor.getName(), newTutor.getName());
		assertEquals(dbTutor.getEmail(), newTutor.getEmail());
		assertEquals(dbTutor.getAccountNumber(), newTutor.getAccountNumber());
		assertTrue(dbTutor.getPayRate() == newTutor.getPayRate());
		assertEquals(dbTutor.getStudentID(), newTutor.getStudentID());
		assertEquals(dbTutor.getSubject(), newTutor.getSubject());
		
		// Remove test objects
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
		assertEquals(dbAccount.getPassword(), newAccount.getPassword());
		assertEquals(dbAccount.getIsAdmin(), newAccount.getIsAdmin());
	
		// Remove test objects
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
		assertEquals(dbTutor.getEmail(), newTutor.getEmail());
		assertEquals(dbTutor.getAccountNumber(), newTutor.getAccountNumber());
		assertTrue(dbTutor.getPayRate() == newTutor.getPayRate());
		assertEquals(dbTutor.getStudentID(), newTutor.getStudentID());
		assertEquals(dbTutor.getSubject(), newTutor.getSubject());
		
		// Remove test objects
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
		assertEquals(dbAccount.getPassword(), newAccount.getPassword());
		assertEquals(dbAccount.getIsAdmin(), newAccount.getIsAdmin());
		
		// Remove test objects
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
		assertEquals(dbTutor.getEmail(), newTutor.getEmail());
		assertEquals(dbTutor.getAccountNumber(), newTutor.getAccountNumber());
		assertTrue(dbTutor.getPayRate() == newTutor.getPayRate());
		assertEquals(dbTutor.getStudentID(), newTutor.getStudentID());
		assertEquals(dbTutor.getSubject(), newTutor.getSubject());

		// Remove test objects
		db.deleteTutor(dbTutor);
	}
	
	@Test
	public void testFindVoucherByEntry() {
		
		Tutor newTutor = new Tutor("user pass", "user@user.use", "use", 1.0, -1, 1, "321", "123");
		db.insertTutor(newTutor);
		Tutor dbTutor = db.getTutors().get(db.getTutors().size() - 1);
		
		PayVoucher newPayVoucher = new PayVoucher("03/04/0001", "03/02/0001", 0, 0, false, false, false, false, -1, dbTutor.getTutorID());
		db.insertPayVoucher(newPayVoucher);
		PayVoucher dbPayVoucher = db.getPayVouchers().get(db.getPayVouchers().size() - 1);
		
		Entry newEntry = new Entry("03/03/0001", "Tutoring", "zoom", 1, -1, dbPayVoucher.getPayVoucherID());
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
	
	@Test
	public void testDeleteTutor() {
		UserAccount account = new UserAccount();
		Tutor tutor = new Tutor();
		int count = 0;
		
		account.setUsername("delete");
		account.setPassword("password");
		tutor.setName("SuperRandomNameWeWillNeverUse");
		tutor.setTutorID(123456789);
		
		db.addTutor(account, tutor);
		
		Tutor testTutor = db.getTutors().get(db.getTutors().size() - 1);
		assertTrue(testTutor.getName().equals("SuperRandomNameWeWillNeverUse"));
		UserAccount deleteMe = db.getUserAccounts().get(db.getUserAccounts().size()-1);
		db.deleteTutor(testTutor);
		db.deleteUserAccount(deleteMe);
		
		for (int i = 0; i < db.getTutors().size(); i++) {
			if (db.getTutors().get(i).getName().equals("SuperRandomNameWeWillNeverUse")) {
				count++;
			}
		}
		
		assertEquals(count, 0);
	}
	
	@Test
	public void testGetTutorInfo() {
		UserAccount account = new UserAccount();
		Tutor tutor = new Tutor();
		
		account.setUsername("gettutor");
		account.setPassword("password");
		tutor.setName("Steven Seymour");

		
		db.addTutor(account, tutor);
		
		Pair<UserAccount, Tutor> testing = new Pair<UserAccount, Tutor>(account, tutor);
		
		assertEquals(db.getTutorInfo("Steven Seymour").getRight().getName(), testing.getRight().getName());
		
		UserAccount deleteMe = db.getUserAccounts().get(db.getUserAccounts().size()-1);
		Tutor deleteTutor = db.getTutors().get(db.getTutors().size()-1);
		
		db.deleteTutor(deleteTutor);
		db.deleteUserAccount(deleteMe);
	}
	
	public void testAccountByLogin() {
		
		String username = "test";
		String password = "test";
		UserAccount newAccount = new UserAccount();
		newAccount.setAccountID(-1);
		newAccount.setUsername(username);
		newAccount.setPassword(password);
		
		db.insertUserAccount(newAccount);
		
		assertEquals(newAccount.getUsername(), db.accountByLogin(username, password).getUsername());
		
		db.deleteUserAccount(db.accountByLogin(username, password));
	}
	
	@Test
	public void testUpdateVoucher(){
		PayVoucher payVoucher = new PayVoucher("03/04/0001", "03/02/0001", 0, 0, false, false, false, false, -1, 1);
		db.insertPayVoucher(payVoucher);
		PayVoucher dbPayVoucher = db.getPayVouchers().get(db.getPayVouchers().size() - 1);
		
		Entry entry = new Entry("03/03/2021", "Tutoring", "zoom", 1, -1, -1);
		ArrayList<Entry> entries = new ArrayList<Entry>();
		entries.add(entry);
		
		db.updateVoucher(entries, dbPayVoucher);
		
		List<Entry> dbEntries = db.getEntries();
		
		assertEquals(entry.getDate(), dbEntries.get(dbEntries.size() - 1).getDate());
		
		db.deleteEntry(dbEntries.get(dbEntries.size() - 1));
		db.deletePayVoucher(dbPayVoucher);
	}
	
	@Test
	public void testFindVoucherBySearch() {
		
		Tutor tutor = new Tutor();
		tutor.setName("Tyler Franks");
		
		List<Pair<Tutor, PayVoucher>> test = db.findVoucherBySearch("Tyler Franks");
		Tutor testTutor = test.get(0).getLeft();
	
		assertTrue(testTutor.getName().equals(tutor.getName()));
	}
	
	@Test
	public void testInsertPayVoucher() {
		
		PayVoucher newVoucher = new PayVoucher("04/27/0001", "04/20/0001", 0.0,
				  0.0, false, false, true, false, 1,1);
		
		db.insertPayVoucher(newVoucher);
		
		List<PayVoucher> PayVoucherList = db.getPayVouchers();
		
		// Newly added objects should be the last object in the list
		PayVoucher dbVoucher = PayVoucherList.get(PayVoucherList.size() - 1);
		
		// Check that the objects were successfully entered
		assertEquals(dbVoucher.getStartDate(), newVoucher.getStartDate());
		assertEquals(dbVoucher.getDueDate(), newVoucher.getDueDate());
		assertEquals(dbVoucher.getIsAdminEdited(), newVoucher.getIsAdminEdited());
		assertEquals(dbVoucher.getIsNew(), newVoucher.getIsNew());
		assertEquals(dbVoucher.getIsSigned(), newVoucher.getIsSigned());
		assertEquals(dbVoucher.getIsSubmitted(), newVoucher.getIsSubmitted());
		assertTrue(dbVoucher.getTotalHours() == newVoucher.getTotalHours());
		assertTrue(dbVoucher.getTotalPay() == newVoucher.getTotalPay());
		assertEquals(dbVoucher.getTutorID(), newVoucher.getTutorID());
		
		// Remove test objects
		db.deletePayVoucher(dbVoucher);
	}
	
	@Test
	public void testFindAllPayVouchers() {
		
		List<PayVoucher> vouchers = db.getPayVouchers();
		List<Pair<Tutor, PayVoucher>> test = db.findAllPayVouchers();
		
		assertEquals(test.size(), vouchers.size());
		assertEquals(test.get(0).getRight().getPayVoucherID(), vouchers.get(0).getPayVoucherID());
		assertEquals(test.get(1).getRight().getPayVoucherID(), vouchers.get(1).getPayVoucherID());
		assertEquals(test.get(2).getRight().getPayVoucherID(), vouchers.get(2).getPayVoucherID());
		assertEquals(test.get(3).getRight().getPayVoucherID(), vouchers.get(3).getPayVoucherID());
		assertEquals(test.get(4).getRight().getPayVoucherID(), vouchers.get(4).getPayVoucherID());
		assertEquals(test.get(5).getRight().getPayVoucherID(), vouchers.get(5).getPayVoucherID());
		assertEquals(test.get(6).getRight().getPayVoucherID(), vouchers.get(6).getPayVoucherID());
	}
	
	@Test
	public void testSubmitPayVouchers() {
		
		PayVoucher payVoucher = new PayVoucher("03/04/0001", "03/02/0001", 0, 0, false, false, false, false, -1, 1);
		db.insertPayVoucher(payVoucher);
		PayVoucher dbPayVoucher = db.getPayVouchers().get(db.getPayVouchers().size() - 1);
		
		db.submitPayVoucher(dbPayVoucher.getPayVoucherID());
		List<PayVoucher> dbPayVouchers = db.getPayVouchers();
		
		assertTrue(dbPayVouchers.get(dbPayVouchers.size() - 1).getIsSubmitted());
		
		db.deletePayVoucher(dbPayVoucher);
		
	}
	
	@Test
	public void testGetPayVoucher() {
		
		PayVoucher newVoucher = new PayVoucher("04/27/0001", "04/20/0001", 0.0,
				  0.0, false, false, true, false, 1,1);
		
		db.insertPayVoucher(newVoucher);
		
		List<PayVoucher> PayVoucherList = db.getPayVouchers();
		
		// Newly added objects should be the last object in the list
		PayVoucher dbVoucher = PayVoucherList.get(PayVoucherList.size() - 1);
		
		// Check that the objects were successfully entered
		assertEquals(dbVoucher.getStartDate(), newVoucher.getStartDate());
		assertEquals(dbVoucher.getDueDate(), newVoucher.getDueDate());
		assertEquals(dbVoucher.getIsAdminEdited(), newVoucher.getIsAdminEdited());
		assertEquals(dbVoucher.getIsNew(), newVoucher.getIsNew());
		assertEquals(dbVoucher.getIsSigned(), newVoucher.getIsSigned());
		assertEquals(dbVoucher.getIsSubmitted(), newVoucher.getIsSubmitted());
		assertTrue(dbVoucher.getTotalHours() == newVoucher.getTotalHours());
		assertTrue(dbVoucher.getTotalPay() == newVoucher.getTotalPay());
		assertEquals(dbVoucher.getTutorID(), newVoucher.getTutorID());
		
		// Remove test objects
		db.deletePayVoucher(dbVoucher);
	}
	
	@Test
	public void testInsertEntries() {
		
		Entry newEntry = new Entry("04/27/0001", "tutoring", "zoom",
				 0.0, 1, 1);
		
		db.insertEntry(newEntry);
		
		List<Entry> EntriesList = db.getEntries();
		
		// Newly added objects should be the last object in the list
		Entry dbEntry = EntriesList.get(EntriesList.size() - 1);
		
		// Check that the objects were successfully entered
		assertEquals(dbEntry.getDate(), newEntry.getDate());
		assertEquals(dbEntry.getServicePerformed(), newEntry.getServicePerformed());
		assertEquals(dbEntry.getWherePerformed(), newEntry.getWherePerformed());
		assertTrue(dbEntry.getHours() == newEntry.getHours());
		
		// Remove test objects
		db.deleteEntry(dbEntry);
	}
	
	@Test
	public void testGetEntries() {
		
		Entry newEntry = new Entry("04/27/0001", "tutoring", "zoom",
				 0.0, 1, 1);
		
		db.insertEntry(newEntry);
		
		List<Entry> EntriesList = db.getEntries();
		
		// Newly added objects should be the last object in the list
		Entry dbEntry = EntriesList.get(EntriesList.size() - 1);
		
		// Check that the objects were successfully entered
		assertEquals(dbEntry.getDate(), newEntry.getDate());
		assertEquals(dbEntry.getServicePerformed(), newEntry.getServicePerformed());
		assertEquals(dbEntry.getWherePerformed(), newEntry.getWherePerformed());
		assertTrue(dbEntry.getHours() == newEntry.getHours());
		
		// Remove test objects
		db.deleteEntry(dbEntry);
	}
	
	@Test
	public void testSignPayVoucher() {
		PayVoucher payVoucher = new PayVoucher("03/04/0001", "03/02/0001", 0, 0, false, false, false, false, -1, 1);
		db.insertPayVoucher(payVoucher);
		PayVoucher dbPayVoucher = db.getPayVouchers().get(db.getPayVouchers().size() - 1);
		
		db.signPayVoucher(dbPayVoucher.getPayVoucherID());
		List<PayVoucher> dbPayVouchers = db.getPayVouchers();
		
		assertTrue(dbPayVouchers.get(dbPayVouchers.size() - 1).getIsSigned());
		
		db.deletePayVoucher(dbPayVoucher);
	}
	
	@Test
	public void testAssignVoucherSpecific() {
		
		Tutor newTutor = new Tutor("user pass", "user@user.use", "use", 1.0, -1, 1, "321", "123");
		db.insertTutor(newTutor);
		Tutor dbTutor = db.getTutors().get(db.getTutors().size() - 1);
		
		db.assignVoucherSpecific("04/01/0001", "05/01/0001", dbTutor.getName());
		List<PayVoucher> vouchers = db.getPayVouchers();
		
		String startDate = vouchers.get(vouchers.size() - 1).getStartDate();
		String dueDate = vouchers.get(vouchers.size() - 1).getDueDate();
		
		assertTrue(startDate.equals("04/01/0001"));
		assertTrue(dueDate.equals("05/01/0001"));
			
		PayVoucher delete = vouchers.get(vouchers.size() - 1);
		db.deletePayVoucher(delete);
		db.deleteTutor(dbTutor);
	}
	
	@Test
	public void testDeleteUserAccount() {
		
		UserAccount newAccount = new UserAccount("user", "pass", -1, false);
		int count = 0;
		db.insertUserAccount(newAccount);
		
		List<UserAccount> accountList = db.getUserAccounts();
		
		// Newly added objects should be the last object in the list
		UserAccount dbAccount = accountList.get(accountList.size() - 1);
		
		// Check that the objects were successfully entered
		assertEquals(dbAccount.getUsername(), newAccount.getUsername());
		assertEquals(dbAccount.getPassword(), newAccount.getPassword());
		assertEquals(dbAccount.getIsAdmin(), newAccount.getIsAdmin());
	
		// Remove test objects
		db.deleteUserAccount(dbAccount);
		
		for (UserAccount Account : db.getUserAccounts()) {
			assertFalse(Account.getUsername().equals(newAccount.getUsername()));
			assertFalse(Account.getPassword().equals(newAccount.getPassword()));
			if (Account.getUsername().equals(newAccount.getUsername()) && Account.getPassword().equals(newAccount.getPassword())) {
				count++;
				
			}
		}
		assertEquals(0, count);
	}
	
	@Test
	public void testDeleteEntry() {
		
		Entry newEntry = new Entry("04/27/0001", "nothingWasDoneHereNoOneWorked", "notALocationThatWillEverBeUsed",
				 0.0, 1, 1);
		
		db.insertEntry(newEntry);
		
		List<Entry> EntriesList = db.getEntries();
		
		// Newly added objects should be the last object in the list
		Entry dbEntry = EntriesList.get(EntriesList.size() - 1);
		// Check that the objects were successfully entered
		assertEquals(dbEntry.getDate(), newEntry.getDate());
		assertEquals(dbEntry.getServicePerformed(), newEntry.getServicePerformed());
		assertEquals(dbEntry.getWherePerformed(), newEntry.getWherePerformed());
		assertTrue(dbEntry.getHours() == newEntry.getHours());
	
		// Remove test objects
		db.deleteEntry(dbEntry);
		
		for (Entry entry : db.getEntries()) {
			assertNotEquals(entry.getServicePerformed(), newEntry.getServicePerformed());
			assertNotEquals(entry.getWherePerformed(), newEntry.getWherePerformed());
		}
	}
	
	@Test
	public void testEditTutor() {
		
		UserAccount newAccount = new UserAccount("user", "pass", -1, false);
		Tutor newTutor = new Tutor("user pass", "user@user.use", "use", 1, -1, -1, "321", "123");
		db.addTutor(newAccount, newTutor);
		
		List<UserAccount> accountList = db.getUserAccounts();
		List<Tutor> tutorList = db.getTutors();
		
		// Newly added objects should be the last object in the list
		UserAccount dbAccount = accountList.get(accountList.size() - 1);
		Tutor dbTutor = tutorList.get(tutorList.size() - 1);
		
		dbAccount.setUsername("someUsername");
		dbTutor.setName("someName");
		
		db.editTutor(dbAccount, dbTutor);
		
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
}
