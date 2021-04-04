package tutorsdb.persist;

import java.util.List;

import model.Entry;
import model.PayVoucher;

public interface IDatabase {
	public List<Entry> findEntryByVoucher(int voucherID);
}
