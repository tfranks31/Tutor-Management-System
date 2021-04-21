package tutorsdb.persist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

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
		assertEquals(dbAccount.getPassword(), newAccount.getPassword());
		assertEquals(dbAccount.getIsAdmin(), newAccount.getIsAdmin());
	
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
		assertEquals(dbTutor.getEmail(), newTutor.getEmail());
		assertEquals(dbTutor.getAccountNumber(), newTutor.getAccountNumber());
		assertTrue(dbTutor.getPayRate() == newTutor.getPayRate());
		assertEquals(dbTutor.getStudentID(), newTutor.getStudentID());
		assertEquals(dbTutor.getSubject(), newTutor.getSubject());
		
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
		assertEquals(dbAccount.getPassword(), newAccount.getPassword());
		assertEquals(dbAccount.getIsAdmin(), newAccount.getIsAdmin());
		
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
		assertEquals(dbTutor.getEmail(), newTutor.getEmail());
		assertEquals(dbTutor.getAccountNumber(), newTutor.getAccountNumber());
		assertTrue(dbTutor.getPayRate() == newTutor.getPayRate());
		assertEquals(dbTutor.getStudentID(), newTutor.getStudentID());
		assertEquals(dbTutor.getSubject(), newTutor.getSubject());

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
	
	@Test
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
		PayVoucher payVoucher = new PayVoucher("03/04/2021", "03/02/2021", 0, 0, false, false, false, false, -1, 1);
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
		
		PayVoucher newVoucher = new PayVoucher("04/27/2021", "04/20/2021", 0.0,
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
		
		// Remove test objects from tutorsdb
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
		
		PayVoucher payVoucher = new PayVoucher("03/04/2021", "03/02/2021", 0, 0, false, false, false, false, -1, 1);
		db.insertPayVoucher(payVoucher);
		PayVoucher dbPayVoucher = db.getPayVouchers().get(db.getPayVouchers().size() - 1);
		
		db.submitPayVoucher(dbPayVoucher.getPayVoucherID());
		List<PayVoucher> dbPayVouchers = db.getPayVouchers();
		
		assertTrue(dbPayVouchers.get(dbPayVouchers.size() - 1).getIsSubmitted());
		
		db.deletePayVoucher(dbPayVoucher);
		
	}
	
	@Test
	public void testInsertPayVoucher1() {
		db.assignVoucher("April", "May");
		List<PayVoucher> vouchers = db.getPayVouchers();
		
		String startDate = vouchers.get(vouchers.size() - 1).getStartDate();
		String dueDate = vouchers.get(vouchers.size() - 1).getDueDate();
		
		assertTrue(startDate.equals("April"));
		assertTrue(dueDate.equals("May"));
		
		List<Tutor> tutors = db.getTutors();
		
		for (int i = 1; i <= tutors.size(); i++) {
			
			PayVoucher delete = vouchers.get(vouchers.size() - i);
			db.deletePayVoucher(delete);
		}
	}
	
	@Test
	public void testGetPayVoucher() {
		
		PayVoucher newVoucher = new PayVoucher("04/27/2021", "04/20/2021", 0.0,
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
		
		// Remove test objects from tutorsdb
		db.deletePayVoucher(dbVoucher);
	}
	
	@Test
	public void testInsertEntries() {
		
		Entry newEntry = new Entry("04/27/2021", "tutoring", "zoom",
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
		
		// Remove test objects from tutorsdb
		db.deleteEntry(dbEntry);
	}
	
	@Test
	public void testGetEntries() {
		
		Entry newEntry = new Entry("04/27/2021", "tutoring", "zoom",
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
		
		// Remove test objects from tutorsdb
		db.deleteEntry(dbEntry);
	}
}
