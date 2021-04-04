package controller;

import java.util.ArrayList;
import java.util.List;

import model.Entry;
import model.PayVoucher;
import model.Tuple;
import model.Tutor;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.FakeDatabase;
import tutorsdb.persist.IDatabase;

public class PayVoucherController {
	
	private IDatabase db = null;
	
	public PayVoucherController() {
		
		DatabaseProvider.setInstance(new FakeDatabase());
		db = DatabaseProvider.getInstance();
	}
	
	public List<Tuple<Tutor, PayVoucher, Entry>> getPayVoucherEntries(int voucherID) {
		
		List<Tuple<Tutor, PayVoucher, Entry>> tutorVoucherList = db.findEntryByVoucher(voucherID);
		
		if (tutorVoucherList.isEmpty()) {
			return null;
		} else {
			return tutorVoucherList;
		}
		
		
	}
}
