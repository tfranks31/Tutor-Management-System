package controller;

import java.util.ArrayList;
import java.util.List;

import model.PayVoucher;
import model.Tutor;
import model.UserAccount;
import model.Pair;
import tutorsdb.persist.DatabaseProvider;
import tutorsdb.persist.DerbyDatabase;
import tutorsdb.persist.FakeDatabase;
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
		//DatabaseProvider.setInstance(new FakeDatabase());
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
	
	/**
	 * Returns an instance of a tutor and account with respective name 
	 * @param name name of tutor who info is requested
	 * @return A pair consiting of a tutor and it's respective UserAccount
	 */
	public Pair<UserAccount, Tutor> getTutorInfo(String name) {
		Pair<UserAccount, Tutor> userTutor;
		userTutor = db.getTutorInfo(name);
		return userTutor;
	}
	
	
	/**
	 * Returns an instance of respective tutor to the entered user ID
	 * @param user userAccount of session user
	 * @return Tutor object of respective user account
	 */
	public Tutor getTutorByUserID(UserAccount user){
		ArrayList<Tutor> tutorList = (ArrayList<Tutor>) db.getTutors();
		
		for (Tutor tutor : tutorList) {
			if (tutor.getAccountID() == user.getAccountID()) {
				return tutor;
			}
		}
		
		return null;
	}
}
