package controller;

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
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.FakeDatabase;
import tutorsdb.persist.IDatabase;

public class PayVoucherControllerTest {
	
	private IDatabase db;
	private PayVoucherController controller;
	
	@Before
    public void setUp() {
		
		DatabaseProvider.setInstance(new FakeDatabase());
		db = DatabaseProvider.getInstance();
		controller = new PayVoucherController();
    }
	
	@Test
	public void testGetPayVoucherEntries() {
		
		Tutor newTutor = new Tutor();
		newTutor.setTutorID(-1);
		db.insertTutor(newTutor);
		
		PayVoucher newPayVoucher = new PayVoucher();
		newPayVoucher.setTutorID(-1);
		newPayVoucher.setPayVoucherID(-1);
		db.insertPayVoucher(newPayVoucher);
		
		Entry newEntry = new Entry();
		newEntry.setPayVoucherID(-1);
		newEntry.setEntryID(-1);
		db.insertEntry(newEntry);
		
		ArrayList<Tuple<Tutor, PayVoucher, Entry>> tupleList = controller.getPayVoucherEntries(-1);
		Tuple<Tutor, PayVoucher, Entry> tuple = tupleList.get(0);
		
		assertEquals(newTutor.getTutorID(), tuple.getLeft().getTutorID());
		assertEquals(newPayVoucher.getPayVoucherID(), tuple.getMiddle().getPayVoucherID());
		assertEquals(newEntry.getEntryID(), tuple.getRight().getEntryID());
		
		db.deleteTutor(newTutor);
		db.deletePayVoucher(newPayVoucher);
		db.deleteEntry(newEntry);
	}
	
	@Test
	public void testUpdateVoucherWithEntries() {
		
		PayVoucher payVoucher = new PayVoucher();
		payVoucher.setPayVoucherID(-1);
		db.insertPayVoucher(payVoucher);
		
		Entry entry = new Entry();
		entry.setDate("10/24/2021");
		entry.setHours(5);
		entry.setServicePerformed("Tutoring");
		entry.setWherePerformed("zoom");
		ArrayList<Entry> entries = new ArrayList<Entry>();
		entries.add(entry);
		
		controller.updateVoucherWithEntries(entries, payVoucher);
		
		List<Entry> dbEntries = db.getEntries();
		
		assertEquals(entry, dbEntries.get(dbEntries.size() - 1));
		
		db.deleteEntry(entry);
		db.deletePayVoucher(payVoucher);
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
		PayVoucher payVoucher = new PayVoucher();
		payVoucher.setPayVoucherID(-1);
		
		//vouchers is added to database before is Submit is updated
		db.insertPayVoucher(payVoucher);
		
		payVoucher.setIsSubmitted(true);
		
		controller.submitPayVoucher(payVoucher.getPayVoucherID());
		List<PayVoucher> dbPayVouchers = db.getPayVouchers();
		
		assertEquals(payVoucher, dbPayVouchers.get(dbPayVouchers.size() - 1));
		
		db.deletePayVoucher(payVoucher);
	}
	
	@Test
	public void testSignPayVoucher() {
		PayVoucher payVoucher = new PayVoucher();
		payVoucher.setPayVoucherID(-1);
		
		db.insertPayVoucher(payVoucher);
		
		payVoucher.setIsSigned(true);
		
		controller.signPayVoucher(payVoucher.getPayVoucherID());
		List<PayVoucher> dbPayVouchers = db.getPayVouchers();
		
		assertEquals(payVoucher, dbPayVouchers.get(dbPayVouchers.size() - 1));
		
		db.deletePayVoucher(payVoucher);
	}
}
