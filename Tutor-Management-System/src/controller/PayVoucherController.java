package controller;

import java.util.ArrayList;
import java.util.List;

import model.Entry;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.FakeDatabase;
import tutorsdb.persist.IDatabase;

public class PayVoucherController {
	
	private IDatabase db = null;
	
	public PayVoucherController() {
		
		DatabaseProvider.setInstance(new FakeDatabase());
		db = DatabaseProvider.getInstance();
	}
	
	public ArrayList<Entry> getPayVoucherEntries(int voucherID) {
		
		List<Entry> entryList = db.findEntryByVoucher(voucherID);
		
		ArrayList<Entry> entries = new ArrayList<Entry>();
		for (Entry entry : entryList) {
			
			entries.add(entry);
		}
		
		return entries;
	}
}
