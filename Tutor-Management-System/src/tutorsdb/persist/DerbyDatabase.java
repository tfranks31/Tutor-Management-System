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
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet1 = null;
				ResultSet resultSet2 = null;
				List<Tuple<Tutor, PayVoucher, Entry>> result = new ArrayList<Tuple<Tutor, PayVoucher, Entry>>();
				
				try {
					// Retrieve only the entries to see if any exist
					stmt1 = conn.prepareStatement(
						"select entries.*" + 
						"from tutors, pay_vouchers, entries " + 
						"where entries.pay_voucher_id = ? " +
						"AND pay_vouchers.pay_voucher_id = entries.pay_voucher_id " +
						"AND pay_vouchers.tutor_id = tutors.tutor_id "
					);
					
					stmt1.setLong(1, voucherID);
					
					resultSet1 = stmt1.executeQuery();
					
					if (!resultSet1.next()) {
						Entry Entry = new Entry();
						Entry = null;
						
						stmt2 = conn.prepareStatement(
							"select tutors.*, pay_vouchers.* " + 
							"from tutors, pay_vouchers " + 
							"where pay_vouchers.pay_voucher_id = ? " +
							"AND pay_vouchers.tutor_id = tutors.tutor_id "
						);
						
						stmt2.setLong(1, voucherID);
						
						resultSet2 = stmt2.executeQuery();
						
						while (resultSet2.next()) {
							Tutor Tutor = new Tutor();
							loadTutor(Tutor, resultSet2, 1);
							PayVoucher PayVoucher = new PayVoucher();
							loadPayVoucher(PayVoucher, resultSet2, 9);
							//loadEntry(Entry, resultSet2, 19);
							PayVoucher.setStartDate(PayVoucher.getStartDate().substring(5) + "/" + PayVoucher.getStartDate().substring(0, 4));
							PayVoucher.setDueDate(PayVoucher.getDueDate().substring(5) + "/" + PayVoucher.getDueDate().substring(0, 4));
							result.add(new Tuple<Tutor, PayVoucher, Entry>(Tutor, PayVoucher, Entry));
						}
					} else {
						stmt2 = conn.prepareStatement(
								"select tutors.*, pay_vouchers.*, entries.* " + 
								"from tutors, pay_vouchers, entries " + 
								"where entries.pay_voucher_id = ? " +
								"AND pay_vouchers.pay_voucher_id = entries.pay_voucher_id " +
								"AND pay_vouchers.tutor_id = tutors.tutor_id "
							);
							
							stmt2.setLong(1, voucherID);
							
							resultSet2 = stmt2.executeQuery();
							
							//if (!resultSet2.next()) {
							//	return null;
							//}
							
							while (resultSet2.next()) {
								Tutor Tutor = new Tutor();
								loadTutor(Tutor, resultSet2, 1);
								PayVoucher PayVoucher = new PayVoucher();
								loadPayVoucher(PayVoucher, resultSet2, 9);
								PayVoucher.setStartDate(PayVoucher.getStartDate().substring(5) + "/" + PayVoucher.getStartDate().substring(0, 4));
								PayVoucher.setDueDate(PayVoucher.getDueDate().substring(5) + "/" + PayVoucher.getDueDate().substring(0, 4));
								
								Entry Entry = new Entry();
								loadEntry(Entry, resultSet2, 19);
								Entry.setDate(Entry.getDate().substring(5) + "/" + Entry.getDate().substring(0, 4));
								
								result.add(new Tuple<Tutor, PayVoucher, Entry>(Tutor, PayVoucher, Entry));
							} 
					}
					
					return result;
					
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(resultSet2);
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
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
					} else {
						return null;
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
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;
				ResultSet resultSet = null;
				
				try {
					// show submitted vouchers
					if (search.toUpperCase().equals("SUBMITTED")) {
						stmt1 = conn.prepareStatement(
							"select tutors.*, pay_vouchers.* " + 
							"from tutors, pay_vouchers " + 
							"where pay_vouchers.is_submitted = true " +
							"AND pay_vouchers.tutor_id = tutors.tutor_id " + 
							"Order by pay_vouchers.due_date DESC"
						);
						resultSet = stmt1.executeQuery();
					// show signed vouchers
					} else if (search.toUpperCase().equals("SIGNED")) {
						stmt2 = conn.prepareStatement(
							"select tutors.*, pay_vouchers.* " + 
							"from tutors, pay_vouchers " + 
							"where pay_vouchers.is_signed = true " +
							"AND pay_vouchers.tutor_id = tutors.tutor_id " + 
							"Order by pay_vouchers.due_date DESC"
						);
						resultSet = stmt2.executeQuery();
					// show all vouchers
					} else if (search.equals("")) {
						stmt3 = conn.prepareStatement(
							"select tutors.*, pay_vouchers.* " + 
							"from tutors, pay_vouchers " + 
							"where pay_vouchers.tutor_id = tutors.tutor_id " + 
							"Order by pay_vouchers.due_date DESC"
						);
						resultSet = stmt3.executeQuery();
					} else {
						// searches by name or start date or due date or subject
						String fuzzy = "%" + search + "%";
						String date = null;
						
						if (search.length() == 10) {
							date = search.substring(6) + "/" + search.substring(0, 5);
						}
						
						stmt4 = conn.prepareStatement(
							"select tutors.*, pay_vouchers.* " + 
							"from tutors, pay_vouchers " + 
							"where (UPPER(tutors.name) LIKE UPPER(?) " + 
							"OR pay_vouchers.due_date = ? " +
							"OR pay_vouchers.start_date = ? " + 
							"OR UPPER(tutors.subject) LIKE UPPER(?)) " +
							"AND pay_vouchers.tutor_id = tutors.tutor_id " + 
							"Order by pay_vouchers.due_date DESC"
						);
						
						stmt4.setString(1, fuzzy);
						stmt4.setString(2, date);
						stmt4.setString(3, date);
						stmt4.setString(4, fuzzy);
						resultSet = stmt4.executeQuery();
					}
					
					List<Pair<Tutor, PayVoucher>> result = new ArrayList<Pair<Tutor, PayVoucher>>();

					while (resultSet.next()) {
						// Create and then load a new user Account
						Tutor Tutor = new Tutor();
						loadTutor(Tutor, resultSet, 1);
						PayVoucher PayVoucher = new PayVoucher();
						loadPayVoucher(PayVoucher, resultSet, 9);
						PayVoucher.setStartDate(PayVoucher.getStartDate().substring(5) + "/" + PayVoucher.getStartDate().substring(0, 4));
						PayVoucher.setDueDate(PayVoucher.getDueDate().substring(5) + "/" + PayVoucher.getDueDate().substring(0, 4));
						
						result.add(new Pair<Tutor, PayVoucher>(Tutor, PayVoucher));
					}

					return result;
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(stmt4);
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
						"where pay_vouchers.tutor_id = tutors.tutor_id " + 
						"Order by pay_vouchers.due_date DESC"
						
					);
					
					List<Pair<Tutor, PayVoucher>> result = new ArrayList<Pair<Tutor, PayVoucher>>();

					resultSet = stmt.executeQuery();

					while (resultSet.next()) {
						// create new payVoucher
						// retrieve attributes from resultSet starting with index 1
						PayVoucher PayVoucher = new PayVoucher();
						loadPayVoucher(PayVoucher, resultSet, 1);
						PayVoucher.setStartDate(PayVoucher.getStartDate().substring(5) + "/" + PayVoucher.getStartDate().substring(0, 4));
						PayVoucher.setDueDate(PayVoucher.getDueDate().substring(5) + "/" + PayVoucher.getDueDate().substring(0, 4));
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
				PreparedStatement stmt5 = null;
				PreparedStatement stmt6 = null;
				ResultSet resultSet1 = null; 
				ResultSet resultSet2 = null;
				
				try {
					for (Entry entry : entries) {
						String date = entry.getDate().substring(6) + "/" + entry.getDate().substring(0, 5);
						
						if (entry.getEntryID() == -1) {
							stmt5 = conn.prepareStatement(
								"INSERT into entries (pay_voucher_id, date, service_performed, where_performed, hours) " + 
								"VALUES (?, ?, ?, ?, ?)"
							);
							
							stmt5.setInt(1, voucher.getPayVoucherID());
							stmt5.setString(2, date);
							stmt5.setString(3, entry.getServicePerformed());
							stmt5.setString(4, entry.getWherePerformed());
							stmt5.setDouble(5, entry.getHours());
							
							stmt5.executeUpdate();
						} else if(entry.getDate().equals("") && entry.getHours() == 0.0 && 
								entry.getServicePerformed().equals("") && entry.getWherePerformed().equals("")) {
							stmt5 = conn.prepareStatement(
								"DELETE FROM entries " + 
								"WHERE entries.entry_id = ? "
							);
							stmt5.setInt(1, entry.getEntryID());
								
							stmt5.executeUpdate();
							
						} else {
							
							stmt1 = conn.prepareStatement(
								"UPDATE entries " + 
								"SET entries.date = ?, entries.service_performed = ?, entries.where_performed = ?, entries.hours = ? " +
								"WHERE entries.entry_id = ? " + 
								"AND entries.pay_voucher_id = ? "
							);
							
							stmt1.setString(1, date);
							stmt1.setString(2, entry.getServicePerformed());
							stmt1.setString(3, entry.getWherePerformed());
							stmt1.setDouble(4, entry.getHours());
							stmt1.setInt(5, entry.getEntryID());
							stmt1.setInt(6, voucher.getPayVoucherID());
							
							stmt1.executeUpdate();
						}
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
					
					//get the tutor attached to the pay voucher
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
					
					//update total hours and total pay
					stmt4 = conn.prepareStatement(
						"UPDATE pay_vouchers " + 
						"SET pay_vouchers.total_hours = ?, pay_vouchers.total_pay = ? " +
						"WHERE pay_vouchers.pay_voucher_id = ? "
					);
					
					stmt4.setDouble(1, totalHours);
					stmt4.setDouble(2, totalPay);
					stmt4.setInt(3, voucher.getPayVoucherID());
					
					stmt4.executeUpdate();
				
					return true;
					
				} finally {
					DBUtil.closeQuietly(resultSet1);
					DBUtil.closeQuietly(resultSet2);
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(stmt4);
					DBUtil.closeQuietly(stmt5);
					DBUtil.closeQuietly(stmt6);
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
				String start = null;
				String due = null;
				//if (startDate.length() == 10) {
					start = startDate.substring(startDate.length() - 4) + "/" + startDate.substring(0, startDate.length() - 5);
				//}
				//if (dueDate.length() == 10) {
					due = dueDate.substring(dueDate.length() - 4) + "/" + dueDate.substring(0, dueDate.length() - 5);
				//}
				try {
					//get a list of all the tutors
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
					//for all of the returned tutors add a new pay voucher
					for (Tutor tutor : result) {
						stmt2 = conn.prepareStatement(
							"INSERT INTO pay_vouchers (tutor_id, start_date, due_date, total_hours, total_pay, is_submitted, is_signed, is_new, is_admin_edited) " +
							"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
						);
						stmt2.setInt(1, tutor.getTutorID());
						stmt2.setString(2, start);
						stmt2.setString(3, due);
						stmt2.setDouble(4, 0);
						stmt2.setDouble(5, 0);
						stmt2.setBoolean(6, false);
						stmt2.setBoolean(7, false);
						stmt2.setBoolean(8, true);
						stmt2.setBoolean(9, false);
						stmt2.executeUpdate();
					}
					
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
						"from pay_vouchers " +
						"Order by pay_vouchers.due_date DESC"
					);
					
					List<PayVoucher> result = new ArrayList<PayVoucher>();

					resultSet = stmt.executeQuery();

					while (resultSet.next()) {
						PayVoucher PayVoucher = new PayVoucher();
						loadPayVoucher(PayVoucher, resultSet, 1);
						PayVoucher.setStartDate(PayVoucher.getStartDate().substring(5) + "/" + PayVoucher.getStartDate().substring(0, 4));
						PayVoucher.setDueDate(PayVoucher.getDueDate().substring(5) + "/" + PayVoucher.getDueDate().substring(0, 4));
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
						Entry.setDate(Entry.getDate().substring(5) + "/" + Entry.getDate().substring(0, 4));
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
		executeTransaction(new Transaction<Boolean>() {
			
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement insertUserAccount = null;
				
				try {
					insertUserAccount = conn.prepareStatement(
						"insert into user_accounts (username, password, is_admin) " +
						"values (?, ?, ?)"
					);
					
					insertUserAccount.setString(1, userAccount.getUsername());
					insertUserAccount.setString(2, userAccount.getPassword());
					insertUserAccount.setBoolean(3, userAccount.getIsAdmin());
					
					insertUserAccount.executeUpdate();
					
					return true;
				} finally {
					DBUtil.closeQuietly(insertUserAccount);
				}
			}
		});
	}

	@Override
	public void insertTutor(Tutor tutor) {
		executeTransaction(new Transaction<Boolean>() {
			
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement insertTutor = null;
				
				try {
					insertTutor = conn.prepareStatement(
						"insert into tutors (user_account_id, name, email, student_id, account_number, subject, pay_rate) " +
						"values (?, ?, ?, ?, ?, ?, ?)"
					);
		
					insertTutor.setInt(1, tutor.getAccountID());
					insertTutor.setString(2, tutor.getName());
					insertTutor.setString(3, tutor.getEmail());
					insertTutor.setString(4, tutor.getStudentID());
					insertTutor.setString(5, tutor.getAccountNumber());
					insertTutor.setString(6, tutor.getSubject());
					insertTutor.setDouble(7, tutor.getPayRate());
					
					insertTutor.executeUpdate();
					
					return true;
				} finally {
					DBUtil.closeQuietly(insertTutor);
				}
			}
		});
	}

	@Override
	public void insertPayVoucher(PayVoucher payVoucher) {
		executeTransaction(new Transaction<Boolean>() {
			
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement insertPayVoucher = null;
				String startDate = payVoucher.getStartDate().substring(6) + "/" + payVoucher.getStartDate().substring(0, 5);
				String dueDate = payVoucher.getDueDate().substring(6) + "/" + payVoucher.getDueDate().substring(0, 5);
				try {
					insertPayVoucher = conn.prepareStatement(
						"insert into pay_vouchers (tutor_id, start_date, due_date, total_hours, total_pay, is_submitted, is_signed, is_new, is_admin_edited) " +
						"values (?, ?, ?, ?, ?, ?, ?, ?, ?)"
					);
		
					insertPayVoucher.setInt(1, payVoucher.getTutorID());
					insertPayVoucher.setString(2, startDate);
					insertPayVoucher.setString(3, dueDate);
					insertPayVoucher.setDouble(4, payVoucher.getTotalHours());
					insertPayVoucher.setDouble(5, payVoucher.getTotalPay());
					insertPayVoucher.setBoolean(6, payVoucher.getIsSubmitted());
					insertPayVoucher.setBoolean(7, payVoucher.getIsSigned());
					insertPayVoucher.setBoolean(8, payVoucher.getIsNew());
					insertPayVoucher.setBoolean(9, payVoucher.getIsAdminEdited());
					
					insertPayVoucher.executeUpdate();
					
					return true;
				} finally {
					DBUtil.closeQuietly(insertPayVoucher);
				}
			}
		});
	}

	@Override
	public void insertEntry(Entry entry) {
		executeTransaction(new Transaction<Boolean>() {
			
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement insertEntry = null;
				String date = entry.getDate().substring(6) + "/" + entry.getDate().substring(0, 5);
		
				try {
					insertEntry = conn.prepareStatement(
						"insert into entries (pay_voucher_id, date, service_performed, where_performed, hours) " + 
						"values (?, ?, ?, ?, ?)"
					);
					
					insertEntry.setInt(1, entry.getPayVoucherID());
					insertEntry.setString(2, date);
					insertEntry.setString(3, entry.getServicePerformed());
					insertEntry.setString(4, entry.getWherePerformed());
					insertEntry.setDouble(5, entry.getHours());
					
					insertEntry.executeUpdate();
					
					return true;
				} finally {
					DBUtil.closeQuietly(insertEntry);
				}
			}
		});
	}

	@Override
	public PayVoucher signPayVoucher(int voucherID) {
		return executeTransaction(new Transaction<PayVoucher>() {
			@Override
			public PayVoucher execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;

				try {
					stmt1 = conn.prepareStatement(
						"UPDATE pay_vouchers " + 
						"SET pay_vouchers.is_signed = True " +
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
						PayVoucher.setStartDate(PayVoucher.getStartDate().substring(5) + "/" + PayVoucher.getStartDate().substring(0, 4));
						PayVoucher.setDueDate(PayVoucher.getDueDate().substring(5) + "/" + PayVoucher.getDueDate().substring(0, 4));
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
	public void editTutor(UserAccount account, Tutor tutor) {
		executeTransaction(new Transaction<Boolean>() {
			
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				PreparedStatement stmt3 = null;
				PreparedStatement stmt4 = null;
				ResultSet resultSet = null;
				ResultSet accountList = null;
				
				try {
					//get a list of all the tutors
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
					
					for (Tutor tutorList : result) {
						if (tutorList.getTutorID() == (tutor.getTutorID())) {
							
							stmt2 = conn.prepareStatement(
								"UPDATE tutors " + 
								"SET tutors.name = ?, tutors.email = ?, tutors.student_id = ?, " +
								"tutors.account_number = ?, tutors.subject = ?, tutors.pay_rate = ? " +
								"WHERE tutors.tutor_id = ? "
							);
							
							//for some reason the tutor that is passed in has an account id of 0
							//stmt2.setInt(1, tutor.getAccountID());
							
							stmt2.setString(1, tutor.getName());
							stmt2.setString(2, tutor.getEmail());
							stmt2.setString(3, tutor.getStudentID());
							stmt2.setString(4, tutor.getAccountNumber());
							stmt2.setString(5, tutor.getSubject());
							stmt2.setDouble(6, tutor.getPayRate());
							stmt2.setInt(7, tutor.getTutorID());
							stmt2.executeUpdate();
						}
					}
					
					stmt3 = conn.prepareStatement(
							"SELECT user_accounts.* " + 
							"FROM user_accounts "
						);
						
						accountList = stmt3.executeQuery();
						
						List<UserAccount> accounts = new ArrayList<UserAccount>();
						
						while (accountList.next()) {
							UserAccount user = new UserAccount();
							loadUserAccount(user, accountList, 1);

							accounts.add(user);
						}
						
						for (UserAccount user : accounts) {
							if (user.getAccountID() == (account.getAccountID())) {
								stmt4 = conn.prepareStatement(
									"UPDATE user_accounts " + 
									"SET user_accounts.username = ?, user_accounts.password = ? " +
									"WHERE user_accounts.user_account_id = ? "
								);
								
								stmt4.setString(1,account.getUsername());
								stmt4.setString(2, account.getPassword());
								stmt4.setInt(3, account.getAccountID());
								stmt4.executeUpdate();
							}
						}
						
					return true;
					
				} finally {
					DBUtil.closeQuietly(resultSet);
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
					DBUtil.closeQuietly(stmt3);
					DBUtil.closeQuietly(stmt4);
				}
			}
		});
	}

	@Override
	public void assignVoucherSpecific(String startDate, String dueDate, String name) {
		executeTransaction(new Transaction<Boolean>() {
			
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				ResultSet resultSet = null;
				String start = startDate.substring(startDate.length() - 4) + "/" + startDate.substring(0, startDate.length() - 5);
				String due = dueDate.substring(dueDate.length() - 4) + "/" + dueDate.substring(0, dueDate.length() - 5);
				try {
					//get a list of all the tutors
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
					
					//for all of the returned tutors add a new pay voucher
					for (Tutor tutor : result) {
						if (tutor.getName().equals(name)) {
							stmt2 = conn.prepareStatement(
								"INSERT INTO pay_vouchers (tutor_id, start_date, due_date, total_hours, total_pay, is_submitted, is_signed, is_new, is_admin_edited) " +
								"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
							);
							stmt2.setInt(1, tutor.getTutorID());
							stmt2.setString(2, start);
							stmt2.setString(3, due);
							stmt2.setDouble(4, 0);
							stmt2.setDouble(5, 0);
							stmt2.setBoolean(6, false);
							stmt2.setBoolean(7, false);
							stmt2.setBoolean(8, true);
							stmt2.setBoolean(9, false);
							
							stmt2.executeUpdate();
						}
					}
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
	public Pair<UserAccount, Tutor> getTutorInfo(String name) {
		return executeTransaction(new Transaction<Pair<UserAccount, Tutor>>() {
			@Override
			public Pair<UserAccount, Tutor> execute(Connection conn) throws SQLException {
				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;
				ResultSet tutorList = null;
				ResultSet accountList = null;

				try {
					stmt1 = conn.prepareStatement(
						"select tutors.* " + 
						"from tutors " + 
						"where tutors.name = ? "
					);
					
					stmt1.setString(1, name);
					tutorList = stmt1.executeQuery();
					
					Pair<UserAccount, Tutor> result = null;
					
					Tutor Tutor = new Tutor();
					
					if (tutorList.next()) {
						loadTutor(Tutor, tutorList, 1);
					
						stmt2 = conn.prepareStatement(
							"select user_accounts.* " + 
							"from user_accounts " + 
							"where user_accounts.user_account_id = ? "
						);
							
						stmt2.setInt(1, Tutor.getAccountID());
						accountList = stmt2.executeQuery();
						
						UserAccount UserAccount = new UserAccount();
						
						if (accountList.next()) {
							loadUserAccount(UserAccount, accountList, 1);
						}
						
						result = (new Pair<UserAccount, Tutor>(UserAccount, Tutor));
					}
					
					return result;
				
				} finally {
					DBUtil.closeQuietly(tutorList);
					DBUtil.closeQuietly(accountList);
					DBUtil.closeQuietly(stmt1);
					DBUtil.closeQuietly(stmt2);
				}
			}
		});
	}

	@Override
	public void updatePasswordWithUserID(UserAccount user, String password) {
		executeTransaction(new Transaction<Boolean>() {
			
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				try {
					stmt = conn.prepareStatement(
						"UPDATE user_accounts " + 
						"SET user_accounts.password = ? " +
						"WHERE user_accounts.user_account_id = ? "
					);
					
					stmt.setString(1, password);
					stmt.setInt(2, user.getAccountID());
					stmt.executeUpdate();
						
					return true;
					
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public void markPayVoucherNotNew(int voucherID) {
		executeTransaction(new Transaction<Boolean>() {
			
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				try {
					stmt = conn.prepareStatement(
						"UPDATE pay_vouchers " + 
						"SET pay_vouchers.is_new = False " +
						"WHERE pay_vouchers.pay_voucher_id = ? "
					);
					
					stmt.setInt(1, voucherID);
					stmt.executeUpdate();
						
					return true;
					
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}

	@Override
	public void markPayVoucherEditedByAdmin(int voucherID, boolean isEdited) {
		executeTransaction(new Transaction<Boolean>() {
			
			@Override
			public Boolean execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				try {
					stmt = conn.prepareStatement(
						"UPDATE pay_vouchers " + 
						"SET pay_vouchers.is_admin_edited = ? " +
						"WHERE pay_vouchers.pay_voucher_id = ? "
					);
					
					stmt.setBoolean(1, isEdited);
					stmt.setInt(2, voucherID);
					stmt.executeUpdate();
						
					return true;
					
				} finally {
					DBUtil.closeQuietly(stmt);
				}
			}
		});
	}
}
