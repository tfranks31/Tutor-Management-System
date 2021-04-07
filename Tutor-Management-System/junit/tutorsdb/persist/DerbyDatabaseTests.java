package tutorsdb.persist;

import org.junit.Before;

public class DerbyDatabaseTests {

	private IDatabase db = null;

	@Before
	public void setUp() throws Exception {
		// creating DB instance here
		DatabaseProvider.setInstance(new DerbyDatabase());
		db = DatabaseProvider.getInstance();		
		
	}
}
