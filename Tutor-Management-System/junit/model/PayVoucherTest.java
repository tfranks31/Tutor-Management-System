package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;


public class PayVoucherTest{
    private PayVoucher model;
    Entry entry = new Entry();

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
        model.setDueDate("3/20/21");
        assertTrue(model.getDueDate() == "3/20/21");
    }

    @Test
    public void testCalculatePay(){
        double pay = 25 * 7.25;
        model.calculatePay(25, 7.25);
        assertTrue(model.getTotalPay() == pay);
    }

    @Test
    public void testCalculateTotalHours(){
        //Sets hours of each entry to its index in the default entry array in
        //the payVoucher. Then calls getTotalHours()
        for (int i = 0; i < 10; i++){ 
            entry.setHours((double) 2);
            model.setEntry(i, entry);
        }
        
        model.CalculateTotalHours();
        assertTrue(model.getTotalHours() == 20.0);
    }
    
    @Test
    public void testSetTotalHours(){
        model.setTotalHours(13);
        assertTrue(model.getTotalHours() == 13);
    }
    
    @Test
    public void testSetEntry() {
    	Entry entry = new Entry();
    	entry.setDate("3/14/2021");
    	entry.setHours(2.0);
    	entry.setServicePerformed("Tutoring");
    	entry.setWherePerformed("zoom");
    	model.setEntry(0, entry);
    	assertEquals(model.getEntry(0), entry);
    }
}
