package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
		//DatabaseProvider.setInstance(new FakeDatabase());
		DatabaseProvider.setInstance(new DerbyDatabase());
		controller = new SearchController();
		db = DatabaseProvider.getInstance();
    }
	
	@Test
	public void testGetAllVouchers() {
		List<PayVoucher> vouchers = db.getPayVouchers();
		List<Pair<Tutor, PayVoucher>> test = controller.getAllVouchers();
		
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
		
		List<Pair<Tutor, PayVoucher>> test = controller.getVoucherFromSearch("Tyler Franks");
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
		
		Tutor newTutor = new Tutor("user pass", "user@user.use", "use", 1.0, -1, 1, "321", "123");
		db.insertTutor(newTutor);
		Tutor dbTutor = db.getTutors().get(db.getTutors().size() - 1);
		
		controller.assignPayVoucherSpecific("04/01/0001", "05/01/0001", dbTutor.getName());
		List<PayVoucher> vouchers = db.getPayVouchers();
		
		String startDate = vouchers.get(vouchers.size() - 1).getStartDate();
		String dueDate = vouchers.get(vouchers.size() - 1).getDueDate();
		assertTrue(startDate.equals("04/01/0001"));
		assertTrue(dueDate.equals("05/01/0001"));
			
		PayVoucher delete = vouchers.get(vouchers.size() - 1);
		db.deletePayVoucher(delete);
		db.deleteTutor(dbTutor);
	}
}