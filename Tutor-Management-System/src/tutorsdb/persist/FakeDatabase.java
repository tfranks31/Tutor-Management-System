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

	@Override
	public List<PayVoucher> findVoucherBySearch(String search) {
		List<PayVoucher> result = new ArrayList<PayVoucher>();
		
		//checks for vouchers by username
		for (Tutor tutor : tutorList) {
			if (tutor.getName().equals(search)) {
				for (PayVoucher voucher : payVoucherList) {
					if (tutor.getTutorID() == voucher.getTutorID()) {
						result.add(voucher);
					}
				}
			}
		}
		
		//checks for vouchers by start or due date
		for (PayVoucher voucher: payVoucherList) {
			if (voucher.getDueDate().equals(search) || voucher.getStartDate().equals(search)) {
				result.add(voucher);
			}
		}
		
		//checks for vouchers by submitted
		if (search.equals("submitted") || search.equals("Submitted")) {
			for (PayVoucher voucher : payVoucherList) {
				if(voucher.getIsSubmitted()) {
					result.add(voucher);
				}
			}
		}
		
		//checks for vouchers by signed
		if (search.equals("signed") || search.equals("Signed")) {
			for (PayVoucher voucher : payVoucherList) {
				if (voucher.getIsSigned()) {
					result.add(voucher);
				}
			}
		}

		return result;
	}

	@Override
	public void AddTutor(String firstname, String lastname, String username, String password,
			String email, int studentID, int accountNumber, String subject, double payRate) {
		//generates new user
		UserAccount user = new UserAccount();
		user.setUsername(username);
		user.setPassword(password);
		user.setIsAdmin(false);
		user.setAccountID(accountList.size());
		
		//generates new tutor
		Tutor tutor = new Tutor();
		tutor.setAccountID(user.getAccountID());
		tutor.setTutorID(tutorList.size());
		String name = firstname + " " + lastname;
		tutor.setName(name);
		tutor.setEmail(email);
		tutor.setStudentID(String.valueOf(studentID));
		tutor.setAccountNumber(String.valueOf(accountNumber));
		tutor.setSubject(subject);
		tutor.setPayRate(payRate);
		
		//adds tutor and user to respective lists
		accountList.add(user);
		tutorList.add(tutor);
	}

	@Override
	public List<PayVoucher> findAllPayVouchers() {
		List<PayVoucher>  result = new ArrayList<PayVoucher>();
		result = payVoucherList;
		return result;
	}


}
