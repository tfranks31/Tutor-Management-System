package tutorsdb.persist;

import java.util.List;

import model.Entry;
import model.PayVoucher;
import model.Tuple;
import model.Tutor;
import model.UserAccount;
import model.Pair;

public interface IDatabase {
	
	/**
	 * Find all Entries by a pay voucher ID, and get all of the Entries and
	 * associated Tutor and PayVoucher information.
	 * @param voucherID The pay voucher ID to look for entries with.
	 * @return A List of Tuples containing entries with their associated Tutor
	 * information and PayVoucher information.
	 */
	public List<Tuple<Tutor, PayVoucher, Entry>> findEntryByVoucher(int voucherID);
	
	/**
	 * Get the UserAccount by the inputed username and password.
	 * @param username The username of the account you want to get.
	 * @param password The password of the account you want to get.
	 * @return The UserAccount with the associated username and password, null
	 * if the account does not exist.
	 */
	public UserAccount accountByLogin(String username, String password);
	
	/**
	 * Find all PayVouchers by a search parameter, and get all the PayVouchers
	 * with their associated Tutors.
	 * @param search The parameter to search by.
	 * @return A List of Pairs containing PayVouchers with their associated
	 * Tutor information.
	 */
	public List<Pair<Tutor, PayVoucher>> findVoucherBySearch(String search);
	
	/**
	 * Insert a new Tutor and their UserAccount into this database.
	 * @param account New Tutor's UserAccount.
	 * @param tutor New Tutor.
	 */
	public void addTutor (UserAccount account, Tutor tutor);
	
	/**
	 * Get all PayVouchers in the database with their associated Tutors.
	 * @return A List of Pairs containing PayVouchers with their associated 
	 * Tutor information.
	 */
	public List<Pair<Tutor, PayVoucher>> findAllPayVouchers();
	
	/**
	 * Submit the PayVoucher with the inputed pay voucher ID.
	 * @param voucherID The ID of the PayVoucher to submit
	 * @return The newly submitted PayVoucher.
	 */
	public PayVoucher submitPayVoucher(int voucherID);
	
	/**
	 * Update the inputed PayVoucher with the corresponding list of Entries.
	 * @param entries A List of Entries to update the PayVoucher with.
	 * @param voucher The PayVoucher to update.
	 */
	public void updateVoucher(List<Entry> entries, PayVoucher voucher);
	
	/**
	 * Assign a new PayVoucher to all Tutors with the specified start date and
	 * end date.
	 * @param startDate The date the PayVoucher is open at.
	 * @param dueDate The date the PayVoucher is due at.
	 */
	public void assignVoucher(String startDate, String dueDate);
	
	/**
	 * Assign a new PayVoucher to a specific Tutor with the specified start date and
	 * end date.
	 * @param startDate The date the PayVoucher is open at.
	 * @param dueDate The date the PayVoucher is due at.
	 * @param name The name of the specific tutor to assign a voucher to.
	 */
	public void assignVoucherSpecific(String startDate, String dueDate, String name);
	
	
	/**
	 * A utility method to get all UserAccounts, meant to be used for testing.
	 * @return A List of all UserAccounts in the database.
	 */
	public List<UserAccount> getUserAccounts();
	
	/**
	 * A utility method to get all Tutors, meant to be used for testing.
	 * @return A List of all Tutors in the database.
	 */
	public List<Tutor> getTutors();
	
	/**
	 * A utility method to get all PayVouchers, meant to be used for testing.
	 * @return A List of all PayVouchers in the database.
	 */
	public List<PayVoucher> getPayVouchers();
	
	/**
	 * A utility method to get all Entries, meant to be used for testing.
	 * @return
	 */
	public List<Entry> getEntries();
	
	/**
	 * Sign the PayVoucher with the inputed pay voucher ID.
	 * @param voucherID The ID of the PayVoucher to Sign.
	 * @return The newly signed PayVoucher.
	 */
	public PayVoucher signPayVoucher(int voucherID);
	
	/**
	 * A utility method to delete a UserAccount from the database, meant to be
	 * used for testing.
	 * @param userAccount The UserAccount to delete.
	 */
	public void deleteUserAccount(UserAccount userAccount);
	
	/**
	 * A utility method to delete a Tutor from the database, meant to be used
	 * for testing.
	 * @param tutor The Tutor to delete.
	 */
	public void deleteTutor(Tutor tutor);
	
	/**
	 * A utility method to delete a PayVoucher from the database, meant to be
	 * used for testing.
	 * @param payVoucher The PayVoucher to delete.
	 */
	public void deletePayVoucher(PayVoucher payVoucher);
	
	/**
	 * A utility method to delete an Entry from the database, meant to be used
	 * for testing.
	 * @param entry The Entry to delete.
	 */
	public void deleteEntry(Entry entry);
	
	/**
	 * A utility method to insert a UserAccount into the database, meant to be
	 * used for testing.
	 * @param userAccount The UserAccount to insert.
	 */
	public void insertUserAccount(UserAccount userAccount);
	
	/**
	 * A utility method to insert a Tutor into the database, meant to be used
	 * for testing.
	 * @param tutor The Tutor to insert.
	 */
	public void insertTutor(Tutor tutor);
	
	/**
	 * A utility method to insert a PayVoucher into the database, meant to be
	 * used for testing.
	 * @param payVoucher The PayVoucher to insert.
	 */
	public void insertPayVoucher(PayVoucher payVoucher);
	
	/**
	 * A utility method to insert an Entry into the database, meant to be used
	 * for testing.
	 * @param entry The Entry to insert.
	 */
	public void insertEntry(Entry entry);
	
	/**
	 * A utility method to change a Tutor's account and User Information
	 * @param account updated Tutor's UserAccount.
	 * @param tutor updated Tutor.
	 */
	public void editTutor (UserAccount account, Tutor tutor);
	
	
	/**
	 * A utility method to get a Tutor's Account and User information
	 * @param name THe name of the tutor
	 * @return A pair consisting of the Tutor and it's respective account
	 */
	public Pair<UserAccount, Tutor>getTutorInfo(String name);
	
	/**
	 * Updates a users password 
	 * @param user The userAccount who password is being updated
	 * @param password	String of what the user wants the new password to be
	 */
	public void updatePasswordWithUserID(UserAccount user, String password);
	
	/**
	 * A utility method to remove the status of new voucher by setting it to false
	 * @param voucherID the ID number of the payVoucher getting updated
	 */
	public void markPayVoucherNotNew(int voucherID);
	
	/**
	 * A utility method to update the status of a payVoucher isAdminEdited flag
	 * @param voucherID the ID number of the payVoucher getting updated
	 * @param isEdited Boolean of the desired updated value
	 */
	public void markPayVoucherEditedByAdmin(int voucherID, boolean isEdited);
	
	/**
	 * A utility method to get every user and tutor in the databse
	 */
	public List<Pair<UserAccount, Tutor>> getAllUserTutor();
	
	public Pair<UserAccount, Tutor> getUserTutorByAccountID(int ID);
}