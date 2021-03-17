package model;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class tutorModelTest{
    private tutorModel model;

    @Before
    public void setUp(){
        model = new tutorModel();

    }

    @Test
    public void testSetGetName(){
        model.setName("Ethan");
        assertTrue(model.getName() == "Ethan");
    }

    @Test
    public void testSetGetEmail(){
        model.setEmail("testemail@gmail.com");
        assertTrue(model.getEmail() == "testemail@gmail.com");
    }

    @Test
    public void testSetGetPayRate(){
        model.setPayRate(7.25);
        assertTrue(model.getPayRate() == 7.25);
    }

    @Test
    public void testSetSubject(){
        model.setSubject("Computer Science");
        assertTrue(model.getSubject() == "Computer Science");
    }
    //test submit payvoucher test
    //get payvoucher test
    //edit payvoucher test
}