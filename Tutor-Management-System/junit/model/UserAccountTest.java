package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UserAccountTest {

	private UserAccount emptyAccount, initialAccount;
	
	@Before
    public void setUp() {
		
        emptyAccount = new UserAccount();
        initialAccount = new UserAccount("123abc", "123abc", 1, false);
    }
	
	@Test
	public void testGetSetUsername() {
		
		emptyAccount.setUsername("123abc");
		assertEquals("123abc", emptyAccount.getUsername());
		assertEquals("123abc", initialAccount.getUsername());
	}
	
	@Test
	public void testGetSetPassword() {
		
		emptyAccount.setPassword("123abc");
		assertEquals("123abc", emptyAccount.getPassword());
		assertEquals("123abc", initialAccount.getPassword());
	}
	
	@Test
	public void testGetSetAccountId() {
		
		emptyAccount.setAccountID(1);
		assertEquals(1, emptyAccount.getAccountID());
		assertEquals(1, initialAccount.getAccountID());
	}
	
	@Test
	public void testGetSetIsAdmin() {
		
		emptyAccount.setIsAdmin(true);
		assertTrue(emptyAccount.getIsAdmin());
		assertFalse(initialAccount.getIsAdmin());
	}
}