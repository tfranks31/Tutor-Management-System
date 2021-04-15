package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;


public class PayVoucherTest{
    private PayVoucher model;

    @Before
    public void setUp(){
        model = new PayVoucher();
    }

    @Test
    public void testSetGetIsSubmitted(){
        model.setIsSubmitted(true);
        assertTrue(model.getIsSubmitted());
    }

    @Test
    public void testSetGetIsSigned(){
        model.setIsSigned(true);
        assertTrue(model.getIsSigned());
    }

    @Test
    public void testSetGetIsNew(){
        model.setIsNew(true);
        assertTrue(model.getIsNew());
    }

    @Test
    public void testSetGetIsAdminEdited(){
        model.setIsAdminEdited(true);
        assertTrue(model.getIsAdminEdited());
    }

    @Test
    public void testSetGetDueDate(){
        model.setDueDate("03/20/21");
        assertTrue(model.getDueDate() == "03/20/21");
    }

    @Test 
    public void testGetSetTotalPay() {
    	
    	model.setTotalPay(15.5);
    	assertTrue(model.getTotalPay() == 15.5);
    }
    
    @Test
    public void testGetSetTotalHours(){
        model.setTotalHours(13);
        assertTrue(model.getTotalHours() == 13);
    }
    
    @Test
    public void testGetSetStartDate() {
    	
    	model.setStartDate("3/14/2021");
    	assertEquals("3/14/2021", model.getStartDate());
    }
    
    @Test
    public void testGetSetPayVoucherID() {
    	
    	model.setPayVoucherID(1);
    	assertEquals(1, model.getPayVoucherID());
    }
    
    @Test
    public void testGetSetTutorID() {
    	
    	model.setTutorID(1);
    	assertEquals(1, model.getTutorID());
    }
}
