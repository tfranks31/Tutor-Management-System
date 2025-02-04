package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.DerbyDatabase;
import tutorsdb.persist.FakeDatabase;
import tutorsdb.persist.IDatabase;

public class PayVoucherControllerTest {
	
	private IDatabase db;
	private PayVoucherController controller;
	
	@Before
    public void setUp() {

		DatabaseProvider.setInstance(new FakeDatabase());
		controller = new PayVoucherController();
		db = DatabaseProvider.getInstance();
    }
	
	@Test
	public void testGetPayVoucherEntries() {

		Tutor newTutor = new Tutor("user pass", "user@user.use", "use", 1.0, -1, 1, "321", "123");
		db.insertTutor(newTutor);
		Tutor dbTutor = db.getTutors().get(db.getTutors().size() - 1);
		
		PayVoucher newPayVoucher = new PayVoucher("03/04/0001", "03/02/0001", 0, 0, false, false, false, false, -1, dbTutor.getTutorID());
		db.insertPayVoucher(newPayVoucher);
		PayVoucher dbPayVoucher = db.getPayVouchers().get(db.getPayVouchers().size() - 1);
		
		Entry newEntry = new Entry("03/03/0001", "Tutoring", "zoom", 1, -1, dbPayVoucher.getPayVoucherID());
		db.insertEntry(newEntry);
		Entry dbEntry = db.getEntries().get(db.getEntries().size() - 1);
		
		ArrayList<Tuple<Tutor, PayVoucher, Entry>> tupleList = controller.getPayVoucherEntries(dbPayVoucher.getPayVoucherID());
		Tuple<Tutor, PayVoucher, Entry> tuple = tupleList.get(tupleList.size() - 1);
		
		assertEquals(newTutor.getName(), tuple.getLeft().getName());
		assertEquals("03/04/0001", tuple.getMiddle().getDueDate());
		assertEquals("03/03/0001", tuple.getRight().getDate());
		
		db.deleteEntry(dbEntry);
		db.deletePayVoucher(dbPayVoucher);
		db.deleteTutor(dbTutor);
	}
	
	@Test
	public void testUpdateVoucherWithEntries() {
		
		PayVoucher payVoucher = new PayVoucher("03/04/0001", "03/02/0001", 0, 0, false, false, false, false, -1, 1);
		db.insertPayVoucher(payVoucher);
		PayVoucher dbPayVoucher = db.getPayVouchers().get(db.getPayVouchers().size() - 1);
		
		Entry entry = new Entry("03/03/0001", "Tutoring", "zoom", 1, -1, -1);
		ArrayList<Entry> entries = new ArrayList<Entry>();
		entries.add(entry);
		
		controller.updateVoucherWithEntries(entries, dbPayVoucher);
		
		List<Entry> dbEntries = db.getEntries();
		
		assertEquals("03/03/0001", dbEntries.get(dbEntries.size() - 1).getDate());
		
		db.deleteEntry(dbEntries.get(dbEntries.size() - 1));
		db.deletePayVoucher(dbPayVoucher);
	}
	
	@Test
	public void testCalculateTotalHours() {
		Entry entry1 = new Entry();
		Entry entry2 = new Entry();
		ArrayList<Entry> entries = new ArrayList<Entry>();
		
		entry1.setHours(5);
		entry2.setHours(2);
		
		entries.add(entry1);
		entries.add(entry2);
		
		double totalhours = controller.calculateTotalHours(entries);
		
		assertTrue(totalhours == 7.0);
		
	}
	
	@Test
	public void testCalculateTotalPay() {
		PayVoucher voucher = new PayVoucher();
		Tutor newTutor = new Tutor();
		
		newTutor.setPayRate(2.0);
		voucher.setTotalHours(5.0);
		
		double totalPay = controller.calculateTotalPay(newTutor, voucher);
		
		assertTrue(totalPay == 10.0);
	}
	
	@Test
	public void testSubmitPayVoucher() {
		
		PayVoucher payVoucher = new PayVoucher("03/04/0001", "03/02/0001", 0, 0, false, false, false, false, -1, 1);
		db.insertPayVoucher(payVoucher);
		PayVoucher dbPayVoucher = db.getPayVouchers().get(db.getPayVouchers().size() - 1);
		
		controller.submitPayVoucher(dbPayVoucher.getPayVoucherID());
		List<PayVoucher> dbPayVouchers = db.getPayVouchers();
		
		assertTrue(dbPayVouchers.get(dbPayVouchers.size() - 1).getIsSubmitted());
		
		db.deletePayVoucher(dbPayVoucher);
	}
	
	@Test
	public void testSignPayVoucher() {
		
		PayVoucher payVoucher = new PayVoucher("03/04/0001", "03/02/0001", 0, 0, false, false, false, false, -1, 1);
		db.insertPayVoucher(payVoucher);
		PayVoucher dbPayVoucher = db.getPayVouchers().get(db.getPayVouchers().size() - 1);
		
		controller.signPayVoucher(dbPayVoucher.getPayVoucherID());
		List<PayVoucher> dbPayVouchers = db.getPayVouchers();
		
		assertTrue(dbPayVouchers.get(dbPayVouchers.size() - 1).getIsSigned());
		
		db.deletePayVoucher(dbPayVoucher);
	}
	
	@Test
	public void testMarkPayVoucherNotNew() {
		
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
		
		controller.markPayVoucherNotNew(dbVoucher.getPayVoucherID());
		PayVoucherList = db.getPayVouchers();
		dbVoucher = PayVoucherList.get(PayVoucherList.size() - 1);
		
		assertFalse(dbVoucher.getIsNew());
		// Remove test objects
		db.deletePayVoucher(dbVoucher);
	}
	
	@Test
	public void testMarkPayVoucherEditedByAdmin() {
		
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
		
		assertFalse(dbVoucher.getIsAdminEdited());
		
		controller.markPayVoucherEditedByAdmin(dbVoucher.getPayVoucherID(), true);
		PayVoucherList = db.getPayVouchers();
		dbVoucher = PayVoucherList.get(PayVoucherList.size() - 1);
		
		assertTrue(dbVoucher.getIsAdminEdited());
		// Remove test objects
		db.deletePayVoucher(dbVoucher);
	}
}
