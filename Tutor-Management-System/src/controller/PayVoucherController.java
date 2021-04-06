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
		
		db = DatabaseProvider.getInstance();
	}
	
	public ArrayList<Tuple<Tutor, PayVoucher, Entry>> getPayVoucherEntries(int voucherID) {
		
		List<Tuple<Tutor, PayVoucher, Entry>> tutorVoucherEntriesList = db.findEntryByVoucher(voucherID);
		
		ArrayList<Tuple<Tutor, PayVoucher, Entry>> tutorVoucherEntries = new ArrayList<Tuple<Tutor, PayVoucher, Entry>>();
		for (Tuple<Tutor, PayVoucher, Entry> tutorVoucherEntry : tutorVoucherEntriesList) {
			
			tutorVoucherEntries.add(tutorVoucherEntry);
		}
		
		return tutorVoucherEntries;
	}
	
	public void updateVoucherWithEntries(List<Entry> entries, int voucherID) {
		db.updateVoucher(entries, voucherID);
	}
}
