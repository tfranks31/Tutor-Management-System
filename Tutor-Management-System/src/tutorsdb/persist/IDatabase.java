package tutorsdb.persist;

import java.util.List;

import model.Entry;
import model.PayVoucher;
import model.UserAccount;

public interface IDatabase {
	public List<Entry> findEntryByVoucher(int voucherID);
	public UserAccount accountByLogin(String username, String password);
	public List<PayVoucher> findVoucherBySearch(String search);
	public void AddTutor (String firstname, String lastname, String username, String password,
							String email, int studentID, int accountNumber, String subject, double payRate);
	public List<PayVoucher> findAllPayVouchers();
	public PayVoucher submitPayVoucher(int voucherID);
}
