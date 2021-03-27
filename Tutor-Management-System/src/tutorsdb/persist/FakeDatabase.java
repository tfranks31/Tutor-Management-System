package tutorsdb.persist;

// Initially refactored from Library Example
public class FakeDatabase implements IDatabase {
	
	// Fake database constructor - initializes the DB
	// the DB only consists for a List of Tutors, Accounts(Users) and a List of PayVouchers
	public FakeDatabase() {
		
		// Add initial data
		readInitialData();
	}

	// loads the initial data retrieved from the CSV files into the DB
	public void readInitialData() {
		
	}
}
