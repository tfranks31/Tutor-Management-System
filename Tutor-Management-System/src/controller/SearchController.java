package controller;

import java.util.ArrayList;
import java.util.List;

import model.PayVoucher;
import model.Tutor;
import model.Pair;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.DerbyDatabase;
import tutorsdb.persist.IDatabase;

/**
 * SearchController class manages all user interactions with the Search page.
 */
public class SearchController {
	private IDatabase db = null;
	
	/**
	 * Refresh database instance when constructed.
	 */
	public SearchController() {
		DatabaseProvider.setInstance(new DerbyDatabase());
		db = DatabaseProvider.getInstance();
	}
	
	/**
	 * Get all PayVouchers from the database with their tutor information.
	 * @return ArrayList containing pairs of Tutors and their PayVouchers.
	 */
	public ArrayList<Pair<Tutor, PayVoucher>> getAllVouchers() {
		
		// Get all vouchers from the database
		List<Pair<Tutor, PayVoucher>> allVouchersList = db.findAllPayVouchers();
		
		ArrayList<Pair<Tutor, PayVoucher>> allVouchers = new ArrayList<Pair<Tutor, PayVoucher>>();
		for (Pair<Tutor, PayVoucher> voucherPair : allVouchersList) {
			
			allVouchers.add(voucherPair);
		}
		
		return allVouchers;
	}
	
	/**
	 * Get all PayVouchers from a specified search parameter.
	 * @param search Parameter to search by.
	 * @return ArrayList containing pairs of Tutors and their PayVouchers.
	 */
	public ArrayList<Pair<Tutor, PayVoucher>> getVoucherFromSearch(String search){
		
		// Get all vouchers with the search parameter
		List<Pair<Tutor, PayVoucher>> resultList = db.findVoucherBySearch(search);
		
		ArrayList<Pair<Tutor, PayVoucher>> result = new ArrayList<Pair<Tutor, PayVoucher>>();
		for (Pair<Tutor, PayVoucher> voucherPair : resultList) {
			
			result.add(voucherPair);
		}
		
		return result;
	}
	
	/**
	 * Assign a PayVoucher with the specified start date and dueDate.
	 * @param startDate Date the PayVoucher will start on.
	 * @param dueDate Date the PayVoucher will be due on.
	 */
	public void assignPayVoucherAll(String startDate, String dueDate) {
		
		// Assign the pay voucher in the database
		db.assignVoucher(startDate, dueDate);
	}
	
	
	/**
	 * Assign a PayVoucher with the specified start date and dueDate.
	 * @param startDate Date the PayVoucher will start on.
	 * @param dueDate Date the PayVoucher will be due on.
	 * @param name Name of the tutor pay voucher is assigned added to.
	 */
	public void assignPayVoucherSpecific(String startDate, String dueDate, String name) {
		db.assignVoucherSpecific(startDate, dueDate, name);
	}
}
