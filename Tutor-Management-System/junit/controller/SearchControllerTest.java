package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Pair;
import model.PayVoucher;
import model.Tutor;
import model.UserAccount;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.DerbyDatabase;
import tutorsdb.persist.FakeDatabase;
import tutorsdb.persist.IDatabase;


public class SearchControllerTest {
	private IDatabase db;
	private SearchController controller;
	
	@Before
    public void setUp() {
		
		// Set and get the tutordb instance and initialize AddTutorController
		DatabaseProvider.setInstance(new FakeDatabase());
		controller = new SearchController();
		db = DatabaseProvider.getInstance();
    }
	
	@Test
	public void testGetAllVouchers() {
		List<PayVoucher> vouchers = db.getPayVouchers();
		List<Pair<Tutor, PayVoucher>> test = controller.getAllVouchers(null);
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
	public void testGetVoucherFromSearch() {
		Tutor tutor = new Tutor();
		tutor.setName("Tyler Franks");
		
		List<Pair<Tutor, PayVoucher>> test = controller.getVoucherFromSearch("Tyler Franks",null);
		Tutor testTutor = test.get(0).getLeft();
	
		assertTrue(testTutor.getName().equals(tutor.getName()));
	}
	
	@Test
	public void testAssignPayVoucher() {
		controller.assignPayVoucherAll("04/01/0001", "05/01/0001");
		List<PayVoucher> vouchers = db.getPayVouchers();
		
		String startDate = vouchers.get(vouchers.size() - 1).getStartDate();
		String dueDate = vouchers.get(vouchers.size() - 1).getDueDate();
		
		assertTrue(startDate.equals("04/01/0001"));
		assertTrue(dueDate.equals("05/01/0001"));
		
		List<Tutor> tutors = db.getTutors();
		
		for (int i = 1; i <= tutors.size(); i++) {
			
			PayVoucher delete = vouchers.get(vouchers.size() - i);
			db.deletePayVoucher(delete);
		}
	}
	
	@Test
	public void testGetTutorInfo() {
		UserAccount account = new UserAccount();
		Tutor tutor = new Tutor();
		
		account.setUsername("username");
		account.setPassword("password");
		tutor.setName("Steven Seymour");

		
		db.addTutor(account, tutor);
		
		Pair<UserAccount, Tutor> testing = new Pair<UserAccount, Tutor>(account, tutor);
		
		assertEquals(controller.getTutorInfo("Steven Seymour").getRight().getName(), testing.getRight().getName());
		
		UserAccount deleteMe = db.getUserAccounts().get(db.getUserAccounts().size()-1);
		Tutor deleteTutor = db.getTutors().get(db.getTutors().size()-1);
		
		db.deleteTutor(deleteTutor);
		db.deleteUserAccount(deleteMe);
	}
	
	
	@Test
	public void testAssignVoucherSpecific() {
		
		UserAccount newUser = new UserAccount("username", "password", 10000000, false);
		db.insertUserAccount(newUser);
		
		List<UserAccount> userAccountList = db.getUserAccounts();
		
		newUser = userAccountList.get(userAccountList.size() - 1);
		
		Tutor newTutor = new Tutor("user pass", "user@user.use", "use", 1.0, -1, newUser.getAccountID(), "321", "123");
		db.insertTutor(newTutor);
		
		
		Tutor dbTutor = db.getTutors().get(db.getTutors().size() - 1);
		UserAccount dbUser = db.getUserAccounts().get(db.getUserAccounts().size() -1);
		
		controller.assignPayVoucherSpecific("04/01/0001", "05/01/0001", dbUser.getUsername());
		List<PayVoucher> vouchers = db.getPayVouchers();
		
		String startDate = vouchers.get(vouchers.size() - 1).getStartDate();
		String dueDate = vouchers.get(vouchers.size() - 1).getDueDate();
		assertTrue(startDate.equals("04/01/0001"));
		assertTrue(dueDate.equals("05/01/0001"));
			
		PayVoucher delete = vouchers.get(vouchers.size() - 1);
		db.deletePayVoucher(delete);
		db.deleteTutor(dbTutor);
		db.deleteUserAccount(dbUser);
	}
	
	@Test
	public void testGetAllUserTutor() {
		
		List<Tutor> tutors = db.getTutors();
		List<UserAccount> userAccounts = db.getUserAccounts();
		List<Pair<UserAccount, Tutor>> userTutorList = new ArrayList<Pair<UserAccount, Tutor>>();
		
		//sorts the list by tutor name to match the sql query sorting by tutor name
		for (int i = 0; i < tutors.size(); i++) {
			for (int c = i + 1; c < tutors.size(); c++) {
				Tutor tempI = tutors.get(i);
				Tutor tempC = tutors.get(c);
				int compare = tempI.getName().compareTo(tempC.getName());
				if (compare > 0) {
					tutors.set(i,tempC);
					tutors.set(c,tempI);
				}
			}
		}
		
		for (Tutor tutor : tutors) {
			for (UserAccount user: userAccounts) {
				if (tutor.getAccountID() == user.getAccountID()) {
					userTutorList.add(new Pair<UserAccount, Tutor>(user, tutor));
				}
			}
		}
		
		List<Pair<UserAccount, Tutor>> test = controller.getAllUserTutors();
		
		assertEquals(test.size(), userTutorList.size());
		for (int i = 0; i < userTutorList.size(); i++) {
			assertEquals(test.get(i).getRight().getAccountID(), userTutorList.get(i).getRight().getAccountID());
			assertEquals(test.get(i).getLeft().getAccountID(), userTutorList.get(i).getLeft().getAccountID());
		}
	}
	
	@Test
	public void testGetUserTutorByAccountID() {
	
		UserAccount account = new UserAccount();
		Tutor tutor = new Tutor();
		
		account.setUsername("gettutor");
		account.setPassword("password");
		tutor.setName("Steven Seymour");

		db.addTutor(account, tutor);
		
		Pair<UserAccount, Tutor> manualTest = new Pair<UserAccount, Tutor>(account, tutor);
		Pair<UserAccount, Tutor> dbTest = controller.getUserTutorByAccountID(db.getTutorInfo("Steven Seymour").getLeft().getAccountID());
		assertEquals(manualTest.getLeft().getUsername(),dbTest.getLeft().getUsername());
		assertEquals(manualTest.getLeft().getPassword(),dbTest.getLeft().getPassword());
		assertEquals(manualTest.getRight().getName(),dbTest.getRight().getName());
		
		db.deleteTutor(dbTest.getRight());
		db.deleteUserAccount(dbTest.getLeft());
		
	}
	
	@Test
	public void testGetUserTutorsFromSearch() {
		
		Tutor tutor = new Tutor();
		tutor.setName("Barry B. Benson");
		UserAccount account = new UserAccount();
		account.setUsername("Barry");
		account.setPassword("Vanessa Bloome");
		db.addTutor(account, tutor);
		
		List<Pair<UserAccount, Tutor>> test = controller.getUserTutorsFromSearch("Barry B. Benson");
		Tutor testTutor = test.get(0).getRight();
	
		assertTrue(testTutor.getName().equals(tutor.getName()));
		
		db.deleteTutor(test.get(0).getRight());
		db.deleteUserAccount(test.get(0).getLeft());
	}
}