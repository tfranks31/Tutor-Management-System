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
		
		// Get all entries , pay voucher, and tutor from the specified voucherID
		List<Tuple<Tutor, PayVoucher, Entry>> tutorVoucherEntriesList = db.findEntryByVoucher(voucherID);
		
		ArrayList<Tuple<Tutor, PayVoucher, Entry>> tutorVoucherEntries = new ArrayList<Tuple<Tutor, PayVoucher, Entry>>();
		for (Tuple<Tutor, PayVoucher, Entry> tutorVoucherEntry : tutorVoucherEntriesList) {
			
			tutorVoucherEntries.add(tutorVoucherEntry);
		}
		
		return tutorVoucherEntries;
	}
	
	public void updateVoucherWithEntries(List<Entry> entries, PayVoucher voucher) {
		
		// Update the database with the entries for the selected voucher
		db.updateVoucher(entries, voucher);
	}
	
	public double calculateTotalHours(List<Entry> entries) {
		
		// Calculate the total hours from all the entries
		double totalHours = 0.0;
		
		for (Entry entry: entries) {
			totalHours += entry.getHours();
		}
		
		return totalHours;
	}
	
	public double calculateTotalPay(Tutor tutor, PayVoucher voucher){
		
		// Calculate the total pay from the voucher and tutor
		return tutor.getPayRate() * voucher.getTotalHours();
	}
	
	public PayVoucher submitPayVoucher(int voucherID) {
		
		// Mark the pay voucher as submitted 
		return db.submitPayVoucher(voucherID);
	}
	
	public PayVoucher signPayVoucher(int voucherID) {
		
		// Mark the payVoucher as signed
		return db.signPayVoucher(voucherID);
	}
}
