package controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

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
		
		assertEquals(newTutor, tuple.getLeft());
		assertEquals(newPayVoucher, tuple.getMiddle());
		assertEquals(newEntry, tuple.getRight());
		
		db.deleteTutor(newTutor);
		db.deletePayVoucher(newPayVoucher);
		db.deleteEntry(newEntry);
	}
}
