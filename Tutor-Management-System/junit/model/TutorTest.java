package model;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class TutorTest {
	private Tutor model;

	@Before
	public void setUp() {
		model = new Tutor();

	}

	@Test
	public void testSetGetName() {
		model.setName("Ethan");
		assertTrue(model.getName() == "Ethan");
	}

	@Test
	public void testSetGetEmail() {
		model.setEmail("testemail@gmail.com");
		assertTrue(model.getEmail() == "testemail@gmail.com");
	}

	@Test
	public void testSetGetPayRate() {
		model.setPayRate(7.25);
		assertTrue(model.getPayRate() == 7.25);
	}

	@Test
	public void testSetGetSubject() {
		model.setSubject("Computer Science");
		assertTrue(model.getSubject() == "Computer Science");
	}

	@Test
	public void testSetGetTutorID() {
		model.setTutorID(12344321);
		assertTrue(model.getTutorID() == 12344321);
	}

	@Test
	public void testSetAccountNumber(){
		int accountNumber = 55;
		model.setAccountNumber(accountNumber);
		assertTrue(model.getAccountNumber() == accountNumber)
	}
	// test submit payvoucher test
	// get payvoucher test
	// edit payvoucher test
}