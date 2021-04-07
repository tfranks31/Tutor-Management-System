package tutorsdb.persist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Pair;
import model.Entry;
import model.PayVoucher;
import model.Tuple;
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
	public List<UserAccount> getUserAccounts() {
		
		return accountList;
	}
	
	@Override
	public List<Tutor> getTutors() {
		
		return tutorList;
	}
	
	@Override
	public List<PayVoucher> getPayVouchers() {
		
		return payVoucherList;
	}
	
	@Override
	public List<Entry> getEntries() {
		
		return entryList;
	}
	
	@Override
	public void deleteUserAccount(UserAccount userAccount) {

		accountList.remove(userAccount);
	}

	@Override
	public void deleteTutor(Tutor tutor) {

		tutorList.remove(tutor);
	}

	@Override
	public void deletePayVoucher(PayVoucher payVoucher) {
			
		payVoucherList.remove(payVoucher);
	}

	@Override
	public void deleteEntry(Entry entry) {
		
		entryList.remove(entry);
	}

	@Override
	public void insertUserAccount(UserAccount userAccount) {
		
		accountList.add(userAccount);
	}

	@Override
	public void insertTutor(Tutor tutor) {

		tutorList.add(tutor);
	}

	@Override
	public void insertPayVoucher(PayVoucher payVoucher) {
		
		payVoucherList.add(payVoucher);
	}

	@Override
	public void insertEntry(Entry entry) {
		
		entryList.add(entry);
	}
	
	@Override
	public List<Tuple<Tutor, PayVoucher, Entry>> findEntryByVoucher(int voucherID) {
		List<Tuple<Tutor, PayVoucher, Entry>> result = new ArrayList<Tuple<Tutor, PayVoucher, Entry>>();
		
		//iterates over every pay voucher and tutor, check if they have the same tutor ID
		for (PayVoucher voucher: payVoucherList) {
			for (Tutor tutor : tutorList) {
				if (voucher.getTutorID() == tutor.getTutorID()) {
					if (voucher.getPayVoucherID() == voucherID) {
		
						//iterates over entry list and checks to see if the entry corresponds to the voucher
						//if so it add the entry to the result
						for (Entry entry: entryList) {
							if (entry.getPayVoucherID() == voucher.getPayVoucherID()) {
								result.add(new Tuple<Tutor, PayVoucher, Entry>(tutor, voucher, entry));
							}
						}
					}
				}
			}
		}
		
		//if voucher is empty returns tutor and voucher info with blank entry
		if (result.isEmpty()) {
			for (PayVoucher voucher: payVoucherList) {
				for (Tutor tutor : tutorList) {
					if (voucher.getTutorID() == tutor.getTutorID()) {
						if (voucher.getPayVoucherID() == voucherID) {
							Entry entry = null;
							result.add(new Tuple<Tutor, PayVoucher, Entry>(tutor, voucher, entry));
						}
					}
				}
			}
		}
		
		return result;
	}

	@Override
	public UserAccount accountByLogin(String username, String password) {
		//finds corresponding account from log in credentials
		for (UserAccount account: accountList) {
			if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
				UserAccount result = account;
				return result;
			}
		}
		return null;
	}

	@Override
	public List<Pair<Tutor, PayVoucher>> findVoucherBySearch(String search) {
		List<Pair<Tutor, PayVoucher>> result = new ArrayList<Pair<Tutor, PayVoucher>>();
		
		//checks for vouchers by username
		for (Tutor tutor : tutorList) {
			if (tutor.getName().equals(search)) {
				for (PayVoucher voucher : payVoucherList) {
					if (tutor.getTutorID() == voucher.getTutorID()) {
						result.add(new Pair<Tutor, PayVoucher>(tutor, voucher));
					}
				}
			}
		}
		
		//checks for vouchers by start or due date
		for (PayVoucher voucher: payVoucherList) {
			if (voucher.getDueDate().equals(search) || voucher.getStartDate().equals(search)) {
				for (Tutor tutor : tutorList) {
					if (voucher.getTutorID() == tutor.getTutorID()) {
						result.add(new Pair<Tutor, PayVoucher>(tutor, voucher));
					}
				}
			}
		}
		
		//checks for vouchers by submitted
		if (search.equals("submitted") || search.equals("Submitted")) {
			for (PayVoucher voucher : payVoucherList) {
				if(voucher.getIsSubmitted()) {
					for (Tutor tutor : tutorList) {
						if (tutor.getTutorID() == voucher.getTutorID()) {
							result.add(new Pair<Tutor, PayVoucher>(tutor, voucher));
						}
					}
				}
			}
		}
		
		//checks for vouchers by signed
		if (search.equals("signed") || search.equals("Signed")) {
			for (PayVoucher voucher : payVoucherList) {
				for (Tutor tutor : tutorList) {
					if (voucher.getTutorID() == tutor.getTutorID() && voucher.getIsSigned() == true) {
						result.add(new Pair<Tutor, PayVoucher>(tutor, voucher));
					}
				}
			}
		}
			
		//returns all vouchers of the search parameter is blank
		if (search.equals("")) {
			for (PayVoucher voucher : payVoucherList) {
				for (Tutor tutor : tutorList) {
					if (voucher.getTutorID() == tutor.getTutorID()) {
						result.add(new Pair<Tutor, PayVoucher>(tutor, voucher));
					}
				}
			}
		}
		return result;
	}

	@Override
	public void addTutor(UserAccount account, Tutor tutor) {
		
		// Set IDs
		account.setAccountID(accountList.size() + 1);
		tutor.setAccountID(account.getAccountID());
		tutor.setTutorID(tutorList.size() + 1);

		// Adds Tutor and User to respective lists
		accountList.add(account);
		tutorList.add(tutor);
	}

	@Override
	public List<Pair<Tutor, PayVoucher>> findAllPayVouchers() {
		//returns every voucher with correspond tutor
		List<Pair<Tutor, PayVoucher>>  result = new ArrayList<Pair<Tutor, PayVoucher>>();
		for (Tutor tutor : tutorList) {
			for (PayVoucher voucher: payVoucherList) {
				if (tutor.getTutorID() == voucher.getTutorID()) {
					result.add(new Pair<Tutor, PayVoucher>(tutor, voucher));
				}
			}
		}
		return result;
	}

	@Override
	public PayVoucher submitPayVoucher(int voucherID) {
		//checks to see if a voucher in the list has a corresponding id
		//updates the isSibmitted parameter to true if so
		for (PayVoucher voucher: payVoucherList) {
			if (voucher.getPayVoucherID() == voucherID) {
				voucher.setIsSubmitted(true);
				return voucher;
			}
		}
		return null;
	}

	@Override
	public void updateVoucher(List<Entry> entries, PayVoucher voucher) {
		for (Entry entry : entries) {
			//removes blank entries from a submitted voucher
			if (entry.getDate().equals("") && entry.getHours() == 0.0 && 
				entry.getServicePerformed().equals("") && entry.getWherePerformed().equals("")) {
				
				entryList.remove(entry);
				continue;
			}
			//checks if the entry is new, if new, it is assigned a corresponding ID
			//and gets added to entry list
			if (entry.getEntryID() == -1) {
				
				entry.setEntryID(entryList.size() + 1);
				entry.setPayVoucherID(voucher.getPayVoucherID());
				entryList.add(entry);
			}
			else {
				//if entry already exists, it updates the already existing entry in the database
				for (Entry dbEntry : entryList) {
					
					if (dbEntry.getEntryID() == entry.getEntryID()) {
						
						dbEntry.setDate(entry.getDate());
						dbEntry.setHours(entry.getHours());
						dbEntry.setServicePerformed(entry.getServicePerformed());
						dbEntry.setWherePerformed(entry.getWherePerformed());
					}
				}
			}
		}
		
		//updates voucher totalHour and totalPay
		for (PayVoucher dbVoucher : payVoucherList) {
			if (dbVoucher.getPayVoucherID() == voucher.getPayVoucherID()) {
				dbVoucher.setTotalPay(voucher.getTotalPay());
				dbVoucher.setTotalHours(voucher.getTotalHours());
			}
		}
		
	}

	@Override
	public void assignVoucher(String startDate, String dueDate) {
		//creates and assigns a pay voucher to every tutor with a set
		//start and end date
		for (Tutor tutor : tutorList) {
			PayVoucher voucher = new PayVoucher();
			voucher.setStartDate(startDate);
			voucher.setDueDate(dueDate);
			voucher.setTutorID(tutor.getTutorID());
			voucher.setIsAdminEdited(false);
			voucher.setIsNew(true);
			voucher.setPayVoucherID(payVoucherList.size() + 1);
			voucher.setIsSigned(false);
			voucher.setIsSubmitted(false);
			voucher.setTotalHours(0);
			voucher.setTotalPay(0);
			payVoucherList.add(voucher);
		}
	}

	@Override
	public PayVoucher signPayVoucher(int voucherID) {
		//checks to see if a voucher in the list has a corresponding id
		//updates the isSibmitted parameter to true if so
		for (PayVoucher voucher: payVoucherList) {
			if (voucher.getPayVoucherID() == voucherID) {
				voucher.setIsSigned(true);
				return voucher;
			}
		}
		return null;
	}

}
