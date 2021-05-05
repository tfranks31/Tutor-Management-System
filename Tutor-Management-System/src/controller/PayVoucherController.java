package controller;

import java.util.ArrayList;
import java.util.List;

import model.Entry;
import model.PayVoucher;
import model.Tuple;
import model.Tutor;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.DerbyDatabase;
import tutorsdb.persist.IDatabase;

/**
 * The PayVoucherController class manages all user interactions with the 
 * PayVoucher page.
 */
public class PayVoucherController {
	
	private IDatabase db = null;
	
	/**
	 * Refresh the database instance when constructed.
	 */
	public PayVoucherController() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		db = DatabaseProvider.getInstance();
	}
	
	/**
	 * Get a tuple of the PayVoucher, Tutor, and all Entries based on a
	 * pay voucher ID.
	 * @param voucherID The PayVoucherID for the pay voucher to get entries and
	 * tutor from.
	 * @return An ArrayList of Tuples that contain the Tutor, PayVoucher, and
	 * Entries for the specified PayVoucher ID.
	 */
	public ArrayList<Tuple<Tutor, PayVoucher, Entry>> getPayVoucherEntries(int voucherID) {
		
		// Get all entries , pay voucher, and tutor from the specified voucherID
		List<Tuple<Tutor, PayVoucher, Entry>> tutorVoucherEntriesList = db.findEntryByVoucher(voucherID);
		
		ArrayList<Tuple<Tutor, PayVoucher, Entry>> tutorVoucherEntries = new ArrayList<Tuple<Tutor, PayVoucher, Entry>>();
		for (Tuple<Tutor, PayVoucher, Entry> tutorVoucherEntry : tutorVoucherEntriesList) {
			
			tutorVoucherEntries.add(tutorVoucherEntry);
		}
		
		return tutorVoucherEntries;
	}
	
	/**
	 * Update the selected PayVoucher with the list of entries and PayVoucher
	 * pay and hour information.
	 * @param entries Entries to update PayVoucher with.
	 * @param voucher PayVoucher to update.
	 */
	public void updateVoucherWithEntries(List<Entry> entries, PayVoucher voucher) {
		
		// Update the database with the entries for the selected voucher
		db.updateVoucher(entries, voucher);
	}
	
	/**
	 * Calculate and get the total hours from a list of entries.
	 * @param entries Entries to calculate total hours from.
	 * @return Total hours from the list of entries.
	 */
	public double calculateTotalHours(List<Entry> entries) {
		
		// Calculate the total hours from all the entries
		double totalHours = 0.0;
		
		for (Entry entry: entries) {
			totalHours += entry.getHours();
		}
		
		return totalHours;
	}
	
	/**
	 * Calculate and get the total pay based on the hours attatched to the 
	 * selected PayVoucher and the pay rate from the selected Tutor.
	 * @param tutor Tutor to get pay rate from.
	 * @param voucher PayVoucher to get total hours from.
	 * @return The total pay based on the pay rate and total hours.
	 */
	public double calculateTotalPay(Tutor tutor, PayVoucher voucher){
		
		// Calculate the total pay from the voucher and tutor
		return tutor.getPayRate() * voucher.getTotalHours();
	}
	
	/**
	 * Mark the PayVoucher with the selected PayVoucherID as submitted.
	 * @param voucherID PayVoucherID from PayVoucher to mark as submitted.
	 * @return The newly submitted PayVoucher.
	 */
	public PayVoucher submitPayVoucher(int voucherID) {
		
		// Mark the pay voucher as submitted 
		return db.submitPayVoucher(voucherID);
	}
	
	/**
	 * Mark the PayVoucher with the selected PayVoucherID as signed off.
	 * @param voucherID PayVoucherID from PayVoucher to mark as signed off.
	 * @return The newly signed off PayVoucher.
	 */
	public PayVoucher signPayVoucher(int voucherID) {
		
		// Mark the payVoucher as signed
		return db.signPayVoucher(voucherID);
	}
	
	public void markPayVoucherNotNew(int voucherID) {
		
		db.markPayVoucherNotNew(voucherID);
	}
	
	public void markPayVoucherEditedByAdmin(int voucherID, boolean isEdited) {
		
		db.markPayVoucherEditedByAdmin(voucherID, isEdited);
	}
}
