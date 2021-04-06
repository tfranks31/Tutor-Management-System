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
	public void addTutor (String firstname, String lastname, String username, String password,
							String email, String studentID, String accountNumber, String subject, double payRate);
	public List<Pair<Tutor, PayVoucher>> findAllPayVouchers();
	public PayVoucher submitPayVoucher(int voucherID);
	public void updateVoucher(List<Entry> entries);
	public void assignVoucher(String startDate, String dueDate);
	public List<UserAccount> getUserAccounts();
	public List<Tutor> getTutors();
	public List<PayVoucher> getPayVouchers();
	public List<Entry> getEntries();
	public void deleteUserAccount(UserAccount userAccount);
	public void deleteTutor(Tutor tutor);
	public void deletePayVoucher(PayVoucher payVoucher);
	public void deleteEntry(Entry entry);
}
