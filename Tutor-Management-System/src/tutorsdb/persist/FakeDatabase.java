package tutorsdb.persist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;
import model.Entry;
import model.PayVoucher;
import model.Tutor;
import model.UserAccount;

// Initially refactored from Library Example
public class FakeDatabase implements IDatabase {
	
	private List<UserAccount> accountList;
	private List<Tutor> tutorList;
	private List<PayVoucher> payVoucherList;
	private List<Entry> entryList;
	
	// Fake database constructor - initializes the DB
	// the DB only consists for a List of Tutors, Accounts(Users) and a List of PayVouchers
	public FakeDatabase() {
		
		accountList = new ArrayList<UserAccount>();
		tutorList = new ArrayList<Tutor>();
		payVoucherList = new ArrayList<PayVoucher>();
		entryList = new ArrayList<Entry>();
		
		// Add initial data
		readInitialData();
	}

	// loads the initial data retrieved from the CSV files into the DB
	public void readInitialData() {
		
		try {
			accountList.addAll(InitialData.getUserAccounts());
			tutorList.addAll(InitialData.getTutors());
			payVoucherList.addAll(InitialData.getPayVouchers());
			entryList.addAll(InitialData.getEntries());
		} catch (IOException e) {
			throw new IllegalStateException("Couldn't read initial data", e);
		}
	}

	@Override
	public List<Entry> findEntryByVoucher(int voucherID) {
		List<Entry> result = new ArrayList<Entry>();
		for (Entry entry: entryList) {
			if (entry.getPayVoucherID() == voucherID) {
				result.add(entry);
			}
		}
		return result;
	}

	@Override
	public UserAccount accountByLogin(String username, String password) {
		UserAccount result = new UserAccount();
		for (UserAccount account: accountList) {
			if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
				result = account;
			}
		}
		return result;
	}


}
