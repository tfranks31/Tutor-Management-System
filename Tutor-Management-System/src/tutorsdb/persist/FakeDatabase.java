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
		
		List<PayVoucher> modifiedPayVoucherList = new ArrayList<PayVoucher>();
		
		for (PayVoucher dbPayVoucher : payVoucherList) {
			
			PayVoucher modifiedPayVoucher = new PayVoucher(dbPayVoucher.getDueDate().substring(5) + "/" + dbPayVoucher.getDueDate().substring(0, 4),
					dbPayVoucher.getStartDate().substring(5) + "/" + dbPayVoucher.getStartDate().substring(0, 4), dbPayVoucher.getTotalHours(),
					dbPayVoucher.getTotalPay(), dbPayVoucher.getIsSubmitted(), dbPayVoucher.getIsSigned(), dbPayVoucher.getIsNew(),
					dbPayVoucher.getIsAdminEdited(), dbPayVoucher.getPayVoucherID(), dbPayVoucher.getTutorID());
			
			modifiedPayVoucherList.add(modifiedPayVoucher);
		}
		
		return modifiedPayVoucherList;
	}
	
	@Override
	public List<Entry> getEntries() {
		
		List<Entry> modifiedEntryList = new ArrayList<Entry>();
		for (Entry dbEntry : entryList) {
			
			Entry modifiedEntry = new Entry(dbEntry.getDate().substring(5) + "/" + dbEntry.getDate().substring(0, 4), dbEntry.getServicePerformed(),
					dbEntry.getWherePerformed(), dbEntry.getHours(), dbEntry.getEntryID(), dbEntry.getPayVoucherID());
			
			modifiedEntryList.add(modifiedEntry);
		}
		return modifiedEntryList;
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
		
		payVoucher.setDueDate(payVoucher.getDueDate().substring(payVoucher.getDueDate().length() - 4) + "/" + payVoucher.getDueDate().substring(0, payVoucher.getDueDate().length() - 5));
		payVoucher.setStartDate(payVoucher.getStartDate().substring(payVoucher.getStartDate().length() - 4) + "/" + payVoucher.getStartDate().substring(0, payVoucher.getStartDate().length() - 5));
		payVoucherList.add(payVoucher);
	}

	@Override
	public void insertEntry(Entry entry) {
		
		entry.setDate(entry.getDate().substring(6) + "/" + entry.getDate().substring(0, 5));
		entryList.add(entry);
	}
	
	@Override
	public List<Tuple<Tutor, PayVoucher, Entry>> findEntryByVoucher(int voucherID) {
		List<Tuple<Tutor, PayVoucher, Entry>> result = new ArrayList<Tuple<Tutor, PayVoucher, Entry>>();
		
		//iterates over every pay voucher and tutor, check if they have the same tutor ID
		for (PayVoucher dbVoucher: payVoucherList) {
			for (Tutor dbTutor : tutorList) {
				if (dbVoucher.getTutorID() == dbTutor.getTutorID()) {
					if (dbVoucher.getPayVoucherID() == voucherID) {
		
						//iterates over entry list and checks to see if the entry corresponds to the voucher
						//if so it add the entry to the result
						for (Entry dbEntry: entryList) {
							if (dbEntry.getPayVoucherID() == dbVoucher.getPayVoucherID()) {
								
								Tutor tutor = new Tutor(dbTutor.getName(), dbTutor.getEmail(), dbTutor.getSubject(), dbTutor.getPayRate(),
										dbTutor.getTutorID(), dbTutor.getAccountID(), dbTutor.getAccountNumber(), dbTutor.getStudentID());
								
								PayVoucher voucher = new PayVoucher(dbVoucher.getDueDate().substring(5) + "/" + dbVoucher.getDueDate().substring(0, 4), 
										dbVoucher.getStartDate().substring(5) + "/" + dbVoucher.getStartDate().substring(0, 4), dbVoucher.getTotalHours(), 
										dbVoucher.getTotalPay(), dbVoucher.getIsSubmitted(), dbVoucher.getIsSigned(), dbVoucher.getIsNew(),
										dbVoucher.getIsAdminEdited(), dbVoucher.getPayVoucherID(), dbVoucher.getTutorID());
								
								Entry entry = new Entry(dbEntry.getDate().substring(5) + "/" + dbEntry.getDate().substring(0, 4), dbEntry.getServicePerformed(), 
										dbEntry.getWherePerformed(), dbEntry.getHours(), dbEntry.getEntryID(), dbEntry.getPayVoucherID());
								
								result.add(new Tuple<Tutor, PayVoucher, Entry>(tutor, voucher, entry));
							}
						}
					}
				}
			}
		}
		
		//if voucher is empty returns tutor and voucher info with blank entry
		if (result.isEmpty()) {
			for (PayVoucher dbVoucher: payVoucherList) {
				for (Tutor dbtutor : tutorList) {
					if (dbVoucher.getTutorID() == dbtutor.getTutorID()) {
						if (dbVoucher.getPayVoucherID() == voucherID) {
							
							Tutor tutor = new Tutor(dbtutor.getName(), dbtutor.getEmail(), dbtutor.getSubject(), dbtutor.getPayRate(),
									dbtutor.getTutorID(), dbtutor.getAccountID(), dbtutor.getAccountNumber(), dbtutor.getStudentID());
							
							PayVoucher voucher = new PayVoucher(dbVoucher.getDueDate().substring(5) + "/" + dbVoucher.getDueDate().substring(0, 4), 
									dbVoucher.getStartDate().substring(5) + "/" + dbVoucher.getStartDate().substring(0, 4), dbVoucher.getTotalHours(), 
									dbVoucher.getTotalPay(), dbVoucher.getIsSubmitted(), dbVoucher.getIsSigned(), dbVoucher.getIsNew(),
									dbVoucher.getIsAdminEdited(), dbVoucher.getPayVoucherID(), dbVoucher.getTutorID());
							
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
		
		String upperSearch = search.toUpperCase();
		
		for (Tutor tutor : tutorList) {
			
			for (PayVoucher dbVoucher : payVoucherList) {
				
				if (tutor.getTutorID() == dbVoucher.getTutorID()) {
					
					if (tutor.getName().toUpperCase().contains(upperSearch) ||
						tutor.getSubject().toUpperCase().contains(upperSearch) ||
						(dbVoucher.getDueDate().substring(5) + "/" + dbVoucher.getDueDate().substring(0, 4)).equals(search) || 
						(dbVoucher.getStartDate().substring(5) + "/" + dbVoucher.getStartDate().substring(0, 4)).equals(search) ||
						(upperSearch.equals("SUBMITTED") && dbVoucher.getIsSubmitted()) 
						|| (upperSearch.equals("SIGNED") && dbVoucher.getIsSigned())) {
						
						PayVoucher voucher = new PayVoucher(dbVoucher.getDueDate().substring(5) + "/" + dbVoucher.getDueDate().substring(0, 4), 
								dbVoucher.getStartDate().substring(5) + "/" + dbVoucher.getStartDate().substring(0, 4), dbVoucher.getTotalHours(), 
								dbVoucher.getTotalPay(), dbVoucher.getIsSubmitted(), dbVoucher.getIsSigned(), dbVoucher.getIsNew(),
								dbVoucher.getIsAdminEdited(), dbVoucher.getPayVoucherID(), dbVoucher.getTutorID());
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
			for (PayVoucher dbVoucher: payVoucherList) {
				if (tutor.getTutorID() == dbVoucher.getTutorID()) {
					
					PayVoucher voucher = new PayVoucher(dbVoucher.getDueDate().substring(5) + "/" + dbVoucher.getDueDate().substring(0, 4), 
							dbVoucher.getStartDate().substring(5) + "/" + dbVoucher.getStartDate().substring(0, 4), dbVoucher.getTotalHours(), 
							dbVoucher.getTotalPay(), dbVoucher.getIsSubmitted(), dbVoucher.getIsSigned(), dbVoucher.getIsNew(),
							dbVoucher.getIsAdminEdited(), dbVoucher.getPayVoucherID(), dbVoucher.getTutorID());
					
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
		for (PayVoucher dbVoucher: payVoucherList) {
			if (dbVoucher.getPayVoucherID() == voucherID) {
				dbVoucher.setIsSubmitted(true);
				
				PayVoucher voucher = new PayVoucher(dbVoucher.getDueDate().substring(5) + "/" + dbVoucher.getDueDate().substring(0, 4), 
						dbVoucher.getStartDate().substring(5) + "/" + dbVoucher.getStartDate().substring(0, 4), dbVoucher.getTotalHours(), 
						dbVoucher.getTotalPay(), dbVoucher.getIsSubmitted(), dbVoucher.getIsSigned(), dbVoucher.getIsNew(),
						dbVoucher.getIsAdminEdited(), dbVoucher.getPayVoucherID(), dbVoucher.getTutorID());
				
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
				
				for (int i = 0; i < entryList.size(); i++) {
					
					if (entryList.get(i).getEntryID() == entry.getEntryID()) {
						
						entryList.remove(entryList.get(i));
						i--;
					}
				}
			}
			//checks if the entry is new, if new, it is assigned a corresponding ID
			//and gets added to entry list
			else if (entry.getEntryID() == -1) {
				
				String date = entry.getDate().substring(6) + "/" + entry.getDate().substring(0, 5);
				entry.setEntryID(entryList.size() + 1);
				entry.setPayVoucherID(voucher.getPayVoucherID());
				entry.setDate(date);
				entryList.add(entry);
			}
			else {
				
				String date = entry.getDate().substring(6) + "/" + entry.getDate().substring(0, 5);
				//if entry already exists, it updates the already existing entry in the database
				for (Entry dbEntry : entryList) {
					
					if (dbEntry.getEntryID() == entry.getEntryID()) {
						
						dbEntry.setDate(date);
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
		String start = startDate.substring(startDate.length() - 4) + "/" + startDate.substring(0, startDate.length() - 5);
		String due = dueDate.substring(dueDate.length() - 4) + "/" + dueDate.substring(0, dueDate.length() - 5);
		for (Tutor tutor : tutorList) {
			PayVoucher voucher = new PayVoucher();
			voucher.setStartDate(start);
			voucher.setDueDate(due);
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
		
		for (PayVoucher dbVoucher: payVoucherList) {
			if (dbVoucher.getPayVoucherID() == voucherID) {
				dbVoucher.setIsSigned(true);
				
				PayVoucher voucher = new PayVoucher(dbVoucher.getDueDate().substring(5) + "/" + dbVoucher.getDueDate().substring(0, 4), 
						dbVoucher.getStartDate().substring(5) + "/" + dbVoucher.getStartDate().substring(0, 4), dbVoucher.getTotalHours(), 
						dbVoucher.getTotalPay(), dbVoucher.getIsSubmitted(), dbVoucher.getIsSigned(), dbVoucher.getIsNew(),
						dbVoucher.getIsAdminEdited(), dbVoucher.getPayVoucherID(), dbVoucher.getTutorID());
				
				return voucher;
			}
		}
		return null;
	}

	@Override
	public void editTutor(UserAccount account, Tutor tutor) {
		// Set IDs
		for (Tutor dbTutor: tutorList) {
			if (dbTutor.getTutorID() == tutor.getTutorID()) {
				dbTutor.setAccountNumber(tutor.getAccountNumber());
				dbTutor.setEmail(tutor.getEmail());
				dbTutor.setName(tutor.getName());
				dbTutor.setPayRate(tutor.getPayRate());
				dbTutor.setStudentID(tutor.getStudentID());
				dbTutor.setSubject(tutor.getSubject());
			}
		}

		for (UserAccount user : accountList) {
			if (user.getAccountID() == account.getAccountID()) {
				user.setPassword(account.getPassword());
				user.setUsername(account.getUsername());
			}
		}
	}

	@Override
	public void assignVoucherSpecific(String startDate, String dueDate, String name) {
		
		String start = startDate.substring(startDate.length() - 4) + "/" + startDate.substring(0, startDate.length() - 5);
		String due = dueDate.substring(dueDate.length() - 4) + "/" + dueDate.substring(0, dueDate.length() - 5);
		
		for (Tutor tutor : tutorList) {
			if (tutor.getName().equals(name)) {
				PayVoucher voucher = new PayVoucher();
				voucher.setStartDate(start);
				voucher.setDueDate(due);
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
		
	}

	@Override
	public Pair<UserAccount, Tutor> getTutorInfo(String name) {
		Pair<UserAccount, Tutor> userTutorPair = null;
		for (Tutor tutor : tutorList) {
			if (tutor.getName().equals(name)) {
				for (UserAccount account : accountList) {
					if (account.getAccountID() == tutor.getAccountID()) {
						userTutorPair = new Pair<UserAccount, Tutor>(account, tutor);
					}
				}
			}
		}
		
		return userTutorPair;
	}

	@Override
	public void updatePasswordWithUserID(UserAccount user, String password) {
		for (UserAccount account : accountList) {
			if (account.getAccountID() == user.getAccountID()) {
				account.setPassword(password);
			}
		}
	}

	@Override
	public void markPayVoucherNotNew(int voucherID) {
		
		for (PayVoucher voucher : payVoucherList) {
			
			if (voucher.getPayVoucherID() == voucherID) {
				
				voucher.setIsNew(false);
				break;
			}
		}
	}

	@Override
	public void markPayVoucherEditedByAdmin(int voucherID, boolean isEdited) {
		
		for (PayVoucher voucher : payVoucherList) {
			
			if (voucher.getPayVoucherID() == voucherID) {
				
				voucher.setIsAdminEdited(isEdited);
				break;
			}
		}
	}
}
