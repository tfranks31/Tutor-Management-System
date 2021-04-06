package tutorsdb.persist;

import java.util.List;

import model.Entry;
import model.PayVoucher;
import model.Tuple;
import model.Tutor;
import model.UserAccount;
import model.Pair;

public interface IDatabase {
	public List<Tuple<Tutor, PayVoucher, Entry>> findEntryByVoucher(int voucherID);
	public UserAccount accountByLogin(String username, String password);
	public List<Pair<Tutor, PayVoucher>> findVoucherBySearch(String search);
	
	/**
	 * Insert a new Tutor and their UserAccount into this database.
	 * @param account New Tutor's UserAccount.
	 * @param tutor New Tutor.
	 */
	public void addTutor (UserAccount account, Tutor tutor);
	
	public List<Pair<Tutor, PayVoucher>> findAllPayVouchers();
	public PayVoucher submitPayVoucher(int voucherID);
	public void updateVoucher(List<Entry> entries, int voucherID);
	public void assignVoucher(String startDate, String dueDate);
	public List<UserAccount> getUserAccounts();
	public List<Tutor> getTutors();
	public List<PayVoucher> getPayVouchers();
	public List<Entry> getEntries();
	public void deleteUserAccount(UserAccount userAccount);
	public void deleteTutor(Tutor tutor);
	public void deletePayVoucher(PayVoucher payVoucher);
	public void deleteEntry(Entry entry);
	public void insertUserAccount(UserAccount userAccount);
	public void insertTutor(Tutor tutor);
	public void insertPayVoucher(PayVoucher payVoucher);
	public void insertEntry(Entry entry);
}
