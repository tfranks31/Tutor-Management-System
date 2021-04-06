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
		// TODO Auto-generated method stub
		return null;
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
							"  from user_accounts " + 
							" where user_accounts.username = ? " + 
							"AND user_accounts.password = ? "
					);
					stmt.setString(1, username);
					stmt.setString(2, password);

					UserAccount result = new UserAccount();

					resultSet = stmt.executeQuery();

					// for testing that a result was returned
					Boolean found = false;

					if (resultSet.next()) {
						found = true;
						// Create and then load a new user Account
						UserAccount UserAccount = new UserAccount();
						loadUserAccount(UserAccount, resultSet, 1);

						result = UserAccount;
					}

					// check if any users were found
					if (!found) {
						System.out.println("No user was found for the given username and password.");
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addTutor(String firstname, String lastname, String username, String password, String email,
			String studentID, String accountNumber, String subject, double payRate) throws UnsupportedOperationException {
		// TODO Auto-generated method stub

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
							"  from pay_vouchers, tutors " + 
							" where pay_vouchers.tutor_id = tutors.tutor_id "
					);

					List<Pair<Tutor, PayVoucher>> result = new ArrayList<Pair<Tutor, PayVoucher>>();

					resultSet = stmt.executeQuery();

					// for testing that a result was returned
					Boolean found = false;

					while (resultSet.next()) {
						found = true;
						// create new payVoucher
						// retrieve attributes from resultSet starting with index 1
						PayVoucher PayVoucher = new PayVoucher();
						loadPayVoucher(PayVoucher, resultSet, 1);
						Tutor Tutor = new Tutor();
						loadTutor(Tutor, resultSet, 11);

						result.add(new Pair<Tutor, PayVoucher>(Tutor, PayVoucher));
					}

					// check if any pay vouchers were found
					if (!found) {
						System.out.println("No pay vouchers were found");
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateVoucher(List<Entry> entries) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void assignVoucher(String startDate, String dueDate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<UserAccount> getUserAccounts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tutor> getTutors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PayVoucher> getPayVouchers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Entry> getEntries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUserAccount(UserAccount userAccounts) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteTutor(Tutor tutors) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePayVoucher(PayVoucher payVouchers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteEntry(Entry entries) {
		// TODO Auto-generated method stub
		
	}

}
