package tutorsdb.persist;

import java.util.List;

import model.Entry;
import model.PayVoucher;
import model.UserAccount;

public interface IDatabase {
	public List<Entry> findEntryByVoucher(int voucherID);
	public UserAccount accountByLogin(String username, String password);
	public List<PayVoucher> findVoucherBySearch(String search);
}
