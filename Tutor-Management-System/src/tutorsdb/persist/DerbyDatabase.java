package tutorsdb.persist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Entry;
import model.PayVoucher;
import model.Tuple;
import model.Tutor;
import model.UserAccount;
import model.Pair;

// Initially refactored from Library Example
public class DerbyDatabase implements IDatabase {
	static {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception e) {
			throw new IllegalStateException("Could not load Derby driver");
		}
	}

	private interface Transaction<ResultType> {
		public ResultType execute(Connection conn) throws SQLException;
	}

	private static final int MAX_ATTEMPTS = 10;

	// wrapper SQL transaction function that calls actual transaction function
	// (which has retries)
	public <ResultType> ResultType executeTransaction(Transaction<ResultType> txn) {
		try {
			return doExecuteTransaction(txn);
		} catch (SQLException e) {
			throw new PersistenceException("Transaction failed", e);
		}
	}

	// SQL transaction function which retries the transaction MAX_ATTEMPTS times
	// before failing
	public <ResultType> ResultType doExecuteTransaction(Transaction<ResultType> txn) throws SQLException {
		Connection conn = connect();

		try {
			int numAttempts = 0;
			boolean success = false;
			ResultType result = null;

			while (!success && numAttempts < MAX_ATTEMPTS) {
				try {
					result = txn.execute(conn);
					conn.commit();
					success = true;
				} catch (SQLException e) {
					if (e.getSQLState() != null && e.getSQLState().equals("41000")) {
						// Deadlock: retry (unless max retry count has been reached)
						numAttempts++;
					} else {
						// Some other kind of SQLException
						throw e;
					}
				}
			}

			if (!success) {
				throw new SQLException("Transaction failed (too many retries)");
			}

			// Success!
			return result;
		} finally {
			DBUtil.closeQuietly(conn);
		}
	}

	// Here is where you name and specify the location of your Derby SQL database
	// Change it here and in SQLDemo.java under Tutor-Management-System->sqldemo
	// DO NOT PUT THE DB IN THE SAME FOLDER AS YOUR PROJECT - that will cause
	// conflicts later w/Git
	private Connection connect() throws SQLException {
		Connection conn = DriverManager
				.getConnection("jdbc:derby:C:/CS320-2021-TutorManagementSystem-DB/library.db;create=true");

		// Set autocommit() to false to allow the execution of
		// multiple queries/statements as part of the same transaction.
		conn.setAutoCommit(false);

		return conn;
	}

	private void loadUserAccount(UserAccount account, ResultSet resultSet, int index) throws SQLException {

		account.setAccountID(resultSet.getInt(index++));
		account.setUsername(resultSet.getString(index++));
		account.setPassword(resultSet.getString(index++));
		account.setIsAdmin(resultSet.getBoolean(index++));
	}

	private void loadTutor(Tutor tutor, ResultSet resultSet, int index) throws SQLException {

		tutor.setTutorID(resultSet.getInt(index++));
		tutor.setAccountID(resultSet.getInt(index++));
		tutor.setName(resultSet.getString(index++));
		tutor.setEmail(resultSet.getString(index++));
		tutor.setStudentID(resultSet.getString(index++));
		tutor.setAccountNumber(resultSet.getString(index++));
		tutor.setSubject(resultSet.getString(index++));
		tutor.setPayRate(resultSet.getDouble(index++));
	}

	private void loadPayVoucher(PayVoucher payVoucher, ResultSet resultSet, int index) throws SQLException {

		payVoucher.setPayVoucherID(resultSet.getInt(index++));
		payVoucher.setTutorID(resultSet.getInt(index++));
		payVoucher.setStartDate(resultSet.getString(index++));
		payVoucher.setDueDate(resultSet.getString(index++));
		payVoucher.setTotalHours(resultSet.getInt(index++));
		payVoucher.setTotalPay(resultSet.getDouble(index++));
		payVoucher.setIsSubmitted(resultSet.getBoolean(index++));
		payVoucher.setIsSigned(resultSet.getBoolean(index++));
		payVoucher.setIsNew(resultSet.getBoolean(index++));
		payVoucher.setIsAdminEdited(resultSet.getBoolean(index++));
	}

	private void loadEntry(Entry entry, ResultSet resultSet, int index) throws SQLException {

		entry.setEntryID(resultSet.getInt(index++));
		entry.setPayVoucherID(resultSet.getInt(index++));
		entry.setDate(resultSet.getString(index++));
		entry.setServicePerformed(resultSet.getString(index++));
		entry.setWherePerformed(resultSet.getString(index++));
		entry.setHours(resultSet.getDouble(index++));
	}

	// creates the Tutors, Accounts and PayVouchers tables
	public void createTables() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {

				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;

				try {
					stmt1 = conn.prepareStatement(
						"create table user_accounts (" + 
						"	user_account_id integer primary key " +
						"		generated always as identity (start with 1, increment by 1), " +
						"	username varchar(40)," + 
						"	password varchar(40)," + 
						"	is_admin boolean" + 
						")"
					);
					stmt1.executeUpdate();

					stmt2 = conn.prepareStatement(
						"create table tutors (" + 
						"	tutor_id integer primary key " + 
						"		generated always as identity (start with 1, increment by 1), " + 
						"	user_account_id integer constraint user_account_id references user_accounts, " + 
						"	name varchar(80)," + 
						"	email varchar(80)," + 
						"   student_id varchar(16), " +
						"   account_number varchar(16), " + 
						"   subject varchar(40), " + 
						"   pay_rate double" + 
						")"
					);
					stmt2.executeUpdate();

					stmt3 = conn.prepareStatement(
						"create table pay_vouchers (" + 
						"	pay_voucher_id integer primary key " + 
						"		generated always as identity (start with 1, increment by 1), " + 
						"	tutor_id integer constraint tutor_id references tutors, " + 
						"	start_date varchar(12)," + 
						"	due_date varchar(12)," + 
						"   total_hours double," + 
						"   total_pay double," + 
						"   is_submitted boolean," + 
						"   is_signed boolean," + 
						"   is_new boolean," + 
						"   is_admin_edited boolean" + 
						")"
					);
					stmt3.executeUpdate();

					stmt4 = conn.prepareStatement(
						"create table entries (" + 
						"	entry_id integer primary key " + 
						"		generated always as identity (start with 1, increment by 1), " + 
						"	pay_voucher_id integer constraint pay_voucher_id references pay_vouchers, " + 
						"	date varchar(12)," + 
						"	service_performed varchar(250)," + 
						"   where_performed varchar(120), " + 
						"   hours double" + 
						")"
					);
					stmt4.executeUpdate();

					return true;
				} finally {
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(stmt4);
				}
			}
		});
	}

	// loads data retrieved from CSV files into DB tables in batch mode
	public void loadInitialData() {
		executeTransaction(new Transaction<Boolean>() {
			@Override
			public Boolean execute(Connection conn) throws SQLException {

				List<UserAccount> accountList;
				List<Tutor> tutorList;
				List<PayVoucher> payVoucherList;
				List<Entry> entryList;

				try {
					accountList = InitialData.getUserAccounts();
					tutorList = InitialData.getTutors();
					payVoucherList = InitialData.getPayVouchers();
					entryList = InitialData.getEntries();
				} catch (IOException e) {
					throw new SQLException("Couldn't read initial data", e);
				}

				PreparedStatement insertUserAccount = null;
				PreparedStatement insertTutor = null;
				PreparedStatement insertPayVoucher = null;
				PreparedStatement insertEntry = null;

				try {
					// populate user_accounts table (do user_accounts first, since account_id is
					// foreign key in tutors and pay_voucher tables)
					insertUserAccount = conn.prepareStatement(
							"insert into user_accounts (username, password, is_admin) values (?, ?, ?)");
					for (UserAccount account : accountList) {
						insertUserAccount.setString(1, account.getUsername());
						insertUserAccount.setString(2, account.getPassword());
						insertUserAccount.setBoolean(3, account.getIsAdmin());
						insertUserAccount.addBatch();
					}
					insertUserAccount.executeBatch();

					// populate tutors table
					insertTutor = conn.prepareStatement(
							"insert into tutors (user_account_id, name, email, student_id, account_number, subject, pay_rate)"
									+ "values (?, ?, ?, ?, ?, ?, ?)");
					for (Tutor tutor : tutorList) {
						insertTutor.setInt(1, tutor.getAccountID());
						insertTutor.setString(2, tutor.getName());
						insertTutor.setString(3, tutor.getEmail());
						insertTutor.setString(4, tutor.getStudentID());
						insertTutor.setString(5, tutor.getAccountNumber());
						insertTutor.setString(6, tutor.getSubject());
						insertTutor.setDouble(7, tutor.getPayRate());
						insertTutor.addBatch();
					}
					insertTutor.executeBatch();

					// populate pay_vouchers table
					insertPayVoucher = conn.prepareStatement(
							"insert into pay_vouchers (tutor_id, start_date, due_date, total_hours, total_pay, is_submitted, is_signed, is_new, is_admin_edited)"
									+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
					for (PayVoucher payVoucher : payVoucherList) {
						insertPayVoucher.setInt(1, payVoucher.getTutorID());
						insertPayVoucher.setString(2, payVoucher.getStartDate());
						insertPayVoucher.setString(3, payVoucher.getDueDate());
						insertPayVoucher.setDouble(4, payVoucher.getTotalHours());
						insertPayVoucher.setDouble(5, payVoucher.getTotalPay());
						insertPayVoucher.setBoolean(6, payVoucher.getIsSubmitted());
						insertPayVoucher.setBoolean(7, payVoucher.getIsSigned());
						insertPayVoucher.setBoolean(8, payVoucher.getIsNew());
						insertPayVoucher.setBoolean(9, payVoucher.getIsAdminEdited());
						insertPayVoucher.addBatch();
					}
					insertPayVoucher.executeBatch();

					// populate entries table
					insertEntry = conn.prepareStatement(
							"insert into entries (pay_voucher_id, date, service_performed, where_performed, hours)"
									+ "values (?, ?, ?, ?, ?)");
					for (Entry entry : entryList) {
						insertEntry.setInt(1, entry.getPayVoucherID());
						insertEntry.setString(2, entry.getDate());
						insertEntry.setString(3, entry.getServicePerformed());
						insertEntry.setString(4, entry.getWherePerformed());
						insertEntry.setDouble(5, entry.getHours());
						insertEntry.addBatch();
					}
					insertEntry.executeBatch();

					return true;
				} finally {
					DBUtil.closeQuietly(insertUserAccount);
					DBUtil.closeQuietly(insertTutor);
					DBUtil.closeQuietly(insertPayVoucher);
					DBUtil.closeQuietly(insertEntry);
				}
			}
		});
	}

	// The main method creates the database tables and loads the initial data.
	public static void main(String[] args) throws IOException {
		System.out.println("Creating tables...");
		DerbyDatabase db = new DerbyDatabase();
		db.createTables();

		System.out.println("Loading initial data...");
		db.loadInitialData();

		System.out.println("Tutor DB successfully initialized!");
	}

	@Override
	public List<Tuple<Tutor, PayVoucher, Entry>> findEntryByVoucher(int voucherID) throws UnsupportedOperationException{
		return executeTransaction(new Transaction<List<Tuple<Tutor, PayVoucher, Entry>>>() {
			@Override
			public List<Tuple<Tutor, PayVoucher, Entry>> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					// Retrieve the entries with the given voucher id
					stmt = conn.prepareStatement(
						"select tutors.*, pay_vouchers.*, entries.*" + 
						"from tutors, pay_vouchers, entries " + 
						"where entries.pay_voucher_id = ? " +
						"AND pay_vouchers.pay_voucher_id = entries.pay_voucher_id " +
						"AND pay_vouchers.tutor_id = tutors.tutor_id "
					);
					
					stmt.setLong(1, voucherID);
					
					List<Tuple<Tutor, PayVoucher, Entry>> result = new ArrayList<Tuple<Tutor, PayVoucher, Entry>>();

					resultSet = stmt.executeQuery();

					while (resultSet.next()) {
						// Create and then load a new entry object
						Tutor Tutor = new Tutor();
						loadTutor(Tutor, resultSet, 1);
						PayVoucher PayVoucher = new PayVoucher();
						loadPayVoucher(PayVoucher, resultSet, 9);
						Entry Entry = new Entry();
						loadEntry(Entry, resultSet, 19);
						result.add(new Tuple<Tutor, PayVoucher, Entry>(Tutor, PayVoucher, Entry));
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});


	}

	@Override
	public UserAccount accountByLogin(String username, String password) throws UnsupportedOperationException {
		return executeTransaction(new Transaction<UserAccount>() {
			@Override
			public UserAccount execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					// Retrieve the users account
					stmt = conn.prepareStatement(
						"select user_accounts.*" + 
						"from user_accounts " + 
						"where user_accounts.username = ? " + 
						"AND user_accounts.password = ? "
					);
					stmt.setString(1, username);
					stmt.setString(2, password);

					UserAccount result = new UserAccount();

					resultSet = stmt.executeQuery();

					if (resultSet.next()) {
						// Create and then load a new user Account
						UserAccount UserAccount = new UserAccount();
						loadUserAccount(UserAccount, resultSet, 1);

						result = UserAccount;
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public List<Pair<Tutor, PayVoucher>> findVoucherBySearch(String search) throws UnsupportedOperationException {
		return executeTransaction(new Transaction<List<Pair<Tutor, PayVoucher>>>() {
			@Override
			public List<Pair<Tutor, PayVoucher>> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				
				try {
					// show submitted vouchers
					if (search.equals("Submitted") || search.equals("submitted")) {
						stmt = conn.prepareStatement(
							"select tutors.*, pay_vouchers.* " + 
							"from tutors, pay_vouchers " + 
							"where pay_vouchers.is_submitted = true " 
						);
					// show signed vouchers
					} else if (search.equals("Signed") || search.equals("signed")) {
						stmt = conn.prepareStatement(
							"select tutors.*, pay_vouchers.* " + 
							"from tutors, pay_vouchers " + 
							"where pay_vouchers.is_signed = true " 
						);
					// show all vouchers
					} else if (search.equals("")) {
						stmt = conn.prepareStatement(
							"select tutors.*, pay_vouchers.* " + 
							"from tutors, pay_vouchers "
						);
					} else {
						// searches by name or start date or due date or subject
						stmt = conn.prepareStatement(
							"select tutors.*, pay_vouchers.* " + 
							"from tutors, pay_vouchers " + 
							"where tutors.name = ? " + 
							"OR pay_vouchers.due_date = ? " +
							"OR pay_vouchers.start_date = ? " + 
							"OR tutors.subject = ? "
						);
						stmt.setString(1, search);
						stmt.setString(2, search);
						stmt.setString(3, search);
						stmt.setString(4, search);
					}

					resultSet = stmt.executeQuery();
					
					List<Pair<Tutor, PayVoucher>> result = new ArrayList<Pair<Tutor, PayVoucher>>();

					while (resultSet.next()) {
						// Create and then load a new user Account
						Tutor Tutor = new Tutor();
						loadTutor(Tutor, resultSet, 1);
						PayVoucher PayVoucher = new PayVoucher();
						loadPayVoucher(PayVoucher, resultSet, 9);
						result.add(new Pair<Tutor, PayVoucher>(Tutor, PayVoucher));
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public void addTutor(UserAccount account, Tutor tutor) { 
		executeTransaction(new Transaction<Boolean>() {
		
		@Override
		public Boolean execute(Connection conn) throws SQLException {
			PreparedStatement stmt1 = null;
			PreparedStatement stmt2 = null;
			PreparedStatement stmt3 = null;
			ResultSet resultSet = null;
			boolean is_admin = false;
			try {
				stmt1 = conn.prepareStatement(
					"INSERT INTO user_accounts (username, password, is_admin) " + 
					"VALUES (?, ?, ?) "
				);
				stmt1.setString(1, account.getUsername());
				stmt1.setString(2, account.getPassword());
				stmt1.setBoolean(3, is_admin);
				
				stmt1.executeUpdate();
		
				stmt2 = conn.prepareStatement(
					"SELECT user_accounts.* " + 
					"FROM user_accounts " + 
					"WHERE user_accounts.username = ? " + 
					"AND user_accounts.password = ? "
				);
				
				stmt2.setString(1, account.getUsername());
				stmt2.setString(2, account.getPassword());
				
				resultSet = stmt2.executeQuery();
				
				UserAccount result = null;
				
				while (resultSet.next()) {
					// create new payVoucher
					// retrieve attributes from resultSet starting with index 1
					UserAccount UserAccount = new UserAccount();
					loadUserAccount(UserAccount, resultSet, 1);
					result = UserAccount;
				}
				
				int accountID = result.getAccountID();
				
				stmt3 = conn.prepareStatement(
						"INSERT into tutors (user_account_id, name, email, student_id, account_number, subject, pay_rate) " + 
						"VALUES (?, ?, ?, ?, ?, ?, ?)"
				);
				
				stmt3.setInt(1, accountID);
				stmt3.setString(2, tutor.getName());
				stmt3.setString(3, tutor.getEmail());
				stmt3.setString(4, tutor.getStudentID());
				stmt3.setString(5, tutor.getAccountNumber());
				stmt3.setString(6, tutor.getSubject());
				stmt3.setDouble(7, tutor.getPayRate());
				
				stmt3.executeUpdate();
				
				return true;
				
			} finally {
				DBUtil.closeQuietly(resultSet);
				DBUtil.closeQuietly(stmt1);
				DBUtil.closeQuietly(stmt2);
				DBUtil.closeQuietly(stmt3);
			}
		}
	});
}

	@Override
	public List<Pair<Tutor, PayVoucher>> findAllPayVouchers() throws UnsupportedOperationException {
		return executeTransaction(new Transaction<List<Pair<Tutor, PayVoucher>>>() {
			@Override
			public List<Pair<Tutor, PayVoucher>> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					// Retrieve all attributes from pay_voucher
					stmt = conn.prepareStatement(
						"select pay_vouchers.*, tutors.* " + 
						"from pay_vouchers, tutors " + 
						"where pay_vouchers.tutor_id = tutors.tutor_id "
					);
					
					List<Pair<Tutor, PayVoucher>> result = new ArrayList<Pair<Tutor, PayVoucher>>();

					resultSet = stmt.executeQuery();

					while (resultSet.next()) {
						// create new payVoucher
						// retrieve attributes from resultSet starting with index 1
						PayVoucher PayVoucher = new PayVoucher();
						loadPayVoucher(PayVoucher, resultSet, 1);
						Tutor Tutor = new Tutor();
						loadTutor(Tutor, resultSet, 11);

						result.add(new Pair<Tutor, PayVoucher>(Tutor, PayVoucher));
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public PayVoucher submitPayVoucher(int voucherID) throws UnsupportedOperationException {
		return executeTransaction(new Transaction<PayVoucher>() {
			@Override
			public PayVoucher execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;

				try {
					// Retrieve all attributes from pay_voucher
					stmt1 = conn.prepareStatement(
						"UPDATE pay_vouchers " + 
						"SET pay_vouchers.is_submitted = True " +
						"WHERE pay_vouchers.pay_voucher_id = ? "
					);
					
					stmt1.setLong(1, voucherID);
					stmt1.executeUpdate();
					
					stmt2 = conn.prepareStatement(
						"SELECT pay_vouchers.* " + 
						"FROM pay_vouchers " +
						"WHERE pay_vouchers.pay_voucher_id = ? "
					);
					
					stmt2.setLong(1, voucherID);
					
					resultSet = stmt2.executeQuery();
					
					PayVoucher result = new PayVoucher();
					
					if (resultSet.next()) {
						PayVoucher PayVoucher = new PayVoucher();
						loadPayVoucher(PayVoucher, resultSet, 1);

						result = PayVoucher;
					}
					
					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}

	@Override
	public void updateVoucher(List<Entry> entries, PayVoucher voucher) throws UnsupportedOperationException {
		executeTransaction(new Transaction<Boolean>() {

			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;
				ResultSet resultSet1 = null;
				ResultSet resultSet2 = null;
				
				try {
					for (Entry entry : entries) {
						stmt1 = conn.prepareStatement(
							"UPDATE entries " + 
							"SET entries.date = ?, entries.service_performed = ?, entries.where_performed = ?, entries.hours = ? " +
							"WHERE entries.entry_id = ? " + 
							"AND entries.pay_voucher_id = ? "
						);
						
						stmt1.setString(1, entry.getDate());
						stmt1.setString(2, entry.getServicePerformed());
						stmt1.setString(3, entry.getWherePerformed());
						stmt1.setDouble(4, entry.getHours());
						stmt1.setInt(5, entry.getEntryID());
						stmt1.setInt(6, voucher.getPayVoucherID());
						
						stmt1.executeUpdate();
					}
					
					stmt2 = conn.prepareStatement(
						"SELECT entries.* " + 
						"FROM entries " +
						"WHERE entries.pay_voucher_id = ? " 
					);
					
					stmt2.setInt(1, voucher.getPayVoucherID());
					
					resultSet1 = stmt2.executeQuery();
					
					List<Entry> result = new ArrayList<Entry>();
					
					while (resultSet1.next()) {
						Entry Entry = new Entry();
						loadEntry(Entry, resultSet1, 1);

						result.add(Entry);
					}
					
					double totalHours = 0;
					
					for (Entry Entry: result) {
						totalHours += Entry.getHours();
					}
					
					stmt3 = conn.prepareStatement(
						"SELECT tutors.* " + 
						"FROM tutors " +
						"WHERE tutors.tutor_id = ? " 
					);
						
					stmt3.setInt(1, voucher.getTutorID());
					
					resultSet2 = stmt3.executeQuery();
					
					Tutor resultTutor = null;
					
					if (resultSet2.next()) {
						Tutor Tutor = new Tutor();
						loadTutor(Tutor, resultSet2, 1);
						
						resultTutor = Tutor;
					}
					
					double totalPay = totalHours * resultTutor.getPayRate();
					
					stmt4 = conn.prepareStatement(
						"UPDATE pay_vouchers " + 
						"SET pay_vouchers.total_hours = ?, pay_vouchers.total_pay = ? "
					);
					
					stmt4.setDouble(1, totalHours);
					stmt4.setDouble(2, totalPay);
					
					stmt4.executeUpdate();
	
					return true;
					
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(resultSet2);
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(stmt4);
				}
			}
		});
		
	}

	@Override
	public void assignVoucher(String startDate, String dueDate) {
		executeTransaction(new Transaction<Boolean>() {
			
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				
				try {
					
					stmt1 = conn.prepareStatement(
						"SELECT tutors.* " + 
						"FROM tutors " 
					);
					
					resultSet = stmt1.executeQuery();
					
					List<Tutor> result = new ArrayList<Tutor>();
					
					while (resultSet.next()) {
						Tutor Tutor = new Tutor();
						loadTutor(Tutor, resultSet, 1);

						result.add(Tutor);
					}
				
					for (Tutor tutor : result) {
						stmt2 = conn.prepareStatement(
							"INSERT INTO pay_vouchers (tutor_id, start_date, due_date, total_hours, total_pay, is_submitted, is_signed, is_new, is_admin_edited) " +
							"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
						);
						stmt2.setInt(1, tutor.getTutorID());
						stmt2.setString(2, startDate);
						stmt2.setString(3, dueDate);
						stmt2.setDouble(4, 0);
						stmt2.setDouble(5, 0);
						stmt2.setBoolean(6, false);
						stmt2.setBoolean(7, false);
						stmt2.setBoolean(8, true);
						stmt2.setBoolean(9, false);	
					}
					
					stmt2.executeUpdate();
					
					return true;
					
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}

	@Override
	public List<UserAccount> getUserAccounts() {
		return executeTransaction(new Transaction<List<UserAccount>>() {
			@Override
			public List<UserAccount> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement(
						"select user_accounts.* " + 
						"from user_accounts " 
					);
					
					List<UserAccount> result = new ArrayList<UserAccount>();

					resultSet = stmt.executeQuery();

					while (resultSet.next()) {
						UserAccount UserAccount = new UserAccount();
						loadUserAccount(UserAccount, resultSet, 1);

						result.add(UserAccount);
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public List<Tutor> getTutors() {
		return executeTransaction(new Transaction<List<Tutor>>() {
			@Override
			public List<Tutor> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement(
						"select tutors.* " + 
						"from tutors " 
					);
					
					List<Tutor> result = new ArrayList<Tutor>();

					resultSet = stmt.executeQuery();

					while (resultSet.next()) {
						Tutor Tutor = new Tutor();
						loadTutor(Tutor, resultSet, 1);

						result.add(Tutor);
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public List<PayVoucher> getPayVouchers() {
		return executeTransaction(new Transaction<List<PayVoucher>>() {
			@Override
			public List<PayVoucher> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement(
						"select pay_vouchers.* " + 
						"from pay_vouchers " 
					);
					
					List<PayVoucher> result = new ArrayList<PayVoucher>();

					resultSet = stmt.executeQuery();

					while (resultSet.next()) {
						PayVoucher PayVoucher = new PayVoucher();
						loadPayVoucher(PayVoucher, resultSet, 1);

						result.add(PayVoucher);
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public List<Entry> getEntries() {
		return executeTransaction(new Transaction<List<Entry>>() {
			@Override
			public List<Entry> execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet resultSet = null;

				try {
					stmt = conn.prepareStatement(
						"select entries.* " + 
						"from entries " 
					);
					
					List<Entry> result = new ArrayList<Entry>();

					resultSet = stmt.executeQuery();

					while (resultSet.next()) {
						Entry Entry = new Entry();
						loadEntry(Entry, resultSet, 1);

						result.add(Entry);
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public void deleteUserAccount(UserAccount userAccounts) {
		executeTransaction(new Transaction<Boolean>() {
			
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement(
						"DELETE FROM user_accounts " + 
						"WHERE user_accounts.user_account_id = ? "
					);
					stmt.setInt(1, userAccounts.getAccountID());
					
					stmt.executeUpdate();
			
					return true;
					
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public void deleteTutor(Tutor tutors) {
		executeTransaction(new Transaction<Boolean>() {
			
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement(
						"DELETE FROM tutors " + 
						"WHERE tutors.tutor_id = ? "
					);
					stmt.setInt(1, tutors.getTutorID());
					
					stmt.executeUpdate();
			
					return true;
					
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public void deletePayVoucher(PayVoucher payVouchers) {
		executeTransaction(new Transaction<Boolean>() {
			
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement(
						"DELETE FROM pay_vouchers " + 
						"WHERE pay_vouchers.pay_voucher_id = ? "
					);
					stmt.setInt(1, payVouchers.getPayVoucherID());
					
					stmt.executeUpdate();
			
					return true;
					
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public void deleteEntry(Entry entries) {
		executeTransaction(new Transaction<Boolean>() {
			
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				
				try {
					stmt = conn.prepareStatement(
						"DELETE FROM entries " + 
						"WHERE entries.entry_id = ? "
					);
					stmt.setInt(1, entries.getEntryID());
					
					stmt.executeUpdate();
			
					return true;
					
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public void insertUserAccount(UserAccount userAccount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertTutor(Tutor tutor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertPayVoucher(PayVoucher payVoucher) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertEntry(Entry entry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PayVoucher signPayVoucher(int voucherID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void editTutor(UserAccount account, Tutor tutor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void assignVoucherSpecific(String startDate, String dueDate, String name) {
		// TODO Auto-generated method stub
		
	}

}
