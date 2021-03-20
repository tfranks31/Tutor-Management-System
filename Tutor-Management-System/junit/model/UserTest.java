package model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

	private Tutor tutor;
	
	@Before
    public void setUp() {
		
        tutor = new Tutor();
    }
	
	@Test
	public void testGetSetUsername() {
		
		tutor.setUsername("123abc");
		assertEquals("123abc", tutor.getUsername());
	}
	
	@Test
	public void testGetSetPassword() {
		
		tutor.setPassword("123abc");
		assertEquals("123abc", tutor.getPassword());
	}
}