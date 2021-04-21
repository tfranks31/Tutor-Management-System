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
    public void testSetIsSubmitted(){
        model.setIsSubmitted(true);
        assertTrue(model.getIsSubmitted());
    }

    @Test
    public void testSetIsSigned(){
        model.setIsSigned(true);
        assertTrue(model.getIsSigned());
    }

    @Test
    public void testSetIsNew(){
        model.setIsNew(true);
        assertTrue(model.getIsNew());
    }

    @Test
    public void testSetIsAdminEdited(){
        model.setIsAdminEdited(true);
        assertTrue(model.getIsAdminEdited());
    }

    @Test
    public void testSetDueDate(){
        model.setDueDate("03/20/21");
        assertTrue(model.getDueDate() == "03/20/21");
    }

    @Test 
    public void testSetTotalPay() {
    	
    	model.setTotalPay(15.5);
    	assertTrue(model.getTotalPay() == 15.5);
    }
    
    @Test
    public void testSetTotalHours(){
        model.setTotalHours(13);
        assertTrue(model.getTotalHours() == 13);
    }
    
    @Test
    public void testSetStartDate() {
    	
    	model.setStartDate("3/14/2021");
    	assertEquals("3/14/2021", model.getStartDate());
    }
    
    @Test
    public void testSetPayVoucherID() {
    	
    	model.setPayVoucherID(1);
    	assertEquals(1, model.getPayVoucherID());
    }
    
    @Test
    public void testSetTutorID() {
    	
    	model.setTutorID(1);
    	assertEquals(1, model.getTutorID());
    }
    
    @Test
    public void testGetIsSubmitted(){
        model.setIsSubmitted(true);
        assertTrue(model.getIsSubmitted());
    }

    @Test
    public void testGetIsSigned(){
        model.setIsSigned(true);
        assertTrue(model.getIsSigned());
    }

    @Test
    public void testGetIsNew(){
        model.setIsNew(true);
        assertTrue(model.getIsNew());
    }

    @Test
    public void testGetIsAdminEdited(){
        model.setIsAdminEdited(true);
        assertTrue(model.getIsAdminEdited());
    }

    @Test
    public void testGetDueDate(){
        model.setDueDate("03/20/21");
        assertTrue(model.getDueDate() == "03/20/21");
    }

    @Test 
    public void testGetTotalPay() {
    	
    	model.setTotalPay(15.5);
    	assertTrue(model.getTotalPay() == 15.5);
    }
    
    @Test
    public void testGetTotalHours(){
        model.setTotalHours(13);
        assertTrue(model.getTotalHours() == 13);
    }
    
    @Test
    public void testGetStartDate() {
    	
    	model.setStartDate("3/14/2021");
    	assertEquals("3/14/2021", model.getStartDate());
    }
    
    @Test
    public void testGetPayVoucherID() {
    	
    	model.setPayVoucherID(1);
    	assertEquals(1, model.getPayVoucherID());
    }
    
    @Test
    public void testGetTutorID() {
    	
    	model.setTutorID(1);
    	assertEquals(1, model.getTutorID());
    }
}
