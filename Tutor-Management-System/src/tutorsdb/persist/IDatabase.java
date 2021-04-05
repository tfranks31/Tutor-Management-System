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
	public void AddTutor (String firstname, String lastname, String username, String password,
							String email, int studentID, int accountNumber, String subject, double payRate);
	public List<Pair<Tutor, PayVoucher>> findAllPayVouchers();
	public PayVoucher submitPayVoucher(int voucherID);
	public void updateVoucher(List<Entry> entries);
	public void assignVoucher(String startDate, String dueDate);
}
