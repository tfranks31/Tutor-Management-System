package tutorsdb.persist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Entry;
import model.PayVoucher;
import model.Tutor;
import model.UserAccount;

public class InitialData {

	// reads initial user_account data from CSV file and returns a List of UserAccounts
	public static List<UserAccount> getUserAccounts() throws IOException {
		List<UserAccount> accountList = new ArrayList<UserAccount>();
		ReadCSV readAccounts = new ReadCSV("user_accounts.csv");
		try {
			// auto-generated primary key for user_accounts table
			Integer accountID = 1;
			while (true) {
				List<String> tuple = readAccounts.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				UserAccount account = new UserAccount();

				// auto-generate account Id, instead
				account.setAccountID(accountID++);				
				account.setUsername(i.next());
				account.setPassword(i.next());
				account.setIsAdmin(Boolean.parseBoolean(i.next()));
				accountList.add(account);
			}
		//	System.out.println("accountList loaded from CSV file");
			return accountList;
		} finally {
			readAccounts.close();
		}
	}
	
	// reads initial tutor data from CSV file and returns a List of Tutors
	public static List<Tutor> getTutors() throws IOException {
		List<Tutor> tutorList = new ArrayList<Tutor>();
		ReadCSV readTutors = new ReadCSV("tutors.csv");
		try {
			// auto-generated primary key for tutors table
			Integer tutorID = 1;
			while (true) {
				List<String> tuple = readTutors.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				Tutor tutor = new Tutor();
				
				// auto-generate tutor ID, instead
				tutor.setTutorID(tutorID++);
				tutor.setAccountID(Integer.parseInt(i.next()));
				tutor.setName(i.next());
				tutor.setEmail(i.next());
				tutor.setStudentID(i.next());
				tutor.setAccountNumber(i.next());
				tutor.setSubject(i.next());
				tutor.setPayRate(Double.parseDouble(i.next()));
				
				tutorList.add(tutor);
			}
		//	System.out.println("tutorList loaded from CSV file");
			return tutorList;
		} finally {
			readTutors.close();
		}
	}
	
	// reads initial pay voucher data from CSV file and returns a List of PayVouchers
	public static List<PayVoucher> getPayVouchers() throws IOException {
		List<PayVoucher> payVoucherList = new ArrayList<PayVoucher>();
		ReadCSV readPayVouchers = new ReadCSV("pay_vouchers.csv");
		try {
			// auto-generated primary key for pay voucher table
			Integer payVoucherID = 1;
			while (true) {
				List<String> tuple = readPayVouchers.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				PayVoucher payVoucher = new PayVoucher();
				
				// auto-generate pay voucher ID
				payVoucher.setPayVoucherID(payVoucherID++);
				payVoucher.setTutorID(Integer.parseInt(i.next()));
				payVoucher.setStartDate(i.next());
				payVoucher.setDueDate(i.next());
				payVoucher.setTotalHours(Integer.parseInt(i.next()));
				payVoucher.setTotalPay(Double.parseDouble(i.next()));
				payVoucher.setIsSubmitted(Boolean.parseBoolean(i.next()));
				payVoucher.setIsSigned(Boolean.parseBoolean(i.next()));
				payVoucher.setIsNew(Boolean.parseBoolean(i.next()));
				payVoucher.setIsAdminEdited(Boolean.parseBoolean(i.next()));
				
				payVoucherList.add(payVoucher);
			}
		//	System.out.println("payVoucherList loaded from CSV file");
			return payVoucherList;
		} finally {
			readPayVouchers.close();
		}
	}
	
	// reads initial entry data from CSV file and returns a List of Entries
	public static List<Entry> getEntries() throws IOException {
		List<Entry> entryList = new ArrayList<Entry>();
		ReadCSV readEntries = new ReadCSV("entries.csv");
		try {
			// auto-generated primary key for entries table
			Integer entryID = 1;
			while (true) {
				List<String> tuple = readEntries.next();
				if (tuple == null) {
					break;
				}
				Iterator<String> i = tuple.iterator();
				Entry entry = new Entry();
				
				// auto-generate entry ID
				entry.setEntryID(entryID++);
				entry.setPayVoucherID(Integer.parseInt(i.next()));
				entry.setDate(i.next());
				entry.setServicePerformed(i.next());
				entry.setWherePerformed(i.next());
				entry.setHours(Double.parseDouble(i.next()));
				
				entryList.add(entry);
			}
		//	System.out.println("entryList loaded from CSV file");
			return entryList;
		} finally {
			readEntries.close();
		}
	}
}