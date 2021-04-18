package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import model.Pair;
import model.PayVoucher;
import model.Tutor;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.FakeDatabase;
import tutorsdb.persist.IDatabase;


public class SearchControllerTest {
	private IDatabase db;
	private SearchController controller;
	
	@Before
    public void setUp() {
		
		// Set and get the tutordb instance and initialize AddTutorController
		DatabaseProvider.setInstance(new FakeDatabase());
		db = DatabaseProvider.getInstance();
		controller = new SearchController();
    }
	
	@Test
	public void testGetAllVouchers() {
		List<PayVoucher> vouchers = db.getPayVouchers();
		List<Pair<Tutor, PayVoucher>> test = controller.getAllVouchers();
		
		assertEquals(test.size(), vouchers.size());
		assertEquals(test.get(0).getRight(), vouchers.get(0));
		assertEquals(test.get(1).getRight(), vouchers.get(1));
		assertEquals(test.get(2).getRight(), vouchers.get(2));
		assertEquals(test.get(3).getRight(), vouchers.get(3));
		assertEquals(test.get(4).getRight(), vouchers.get(4));
		assertEquals(test.get(5).getRight(), vouchers.get(5));
		assertEquals(test.get(6).getRight(), vouchers.get(6));	
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
		controller.assignPayVoucherAll("April", "May");
		List<PayVoucher> vouchers = db.getPayVouchers();
		
		String startDate = vouchers.get(vouchers.size() - 1).getStartDate();
		String dueDate = vouchers.get(vouchers.size() - 1).getDueDate();
		
		assertTrue(startDate.equals("April"));
		assertTrue(dueDate.equals("May"));
		
		PayVoucher delete = vouchers.get(vouchers.size() - 1);
		db.deletePayVoucher(delete);
	}
}

