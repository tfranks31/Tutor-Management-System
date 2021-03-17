package model;

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
    public void testGetTotalHours(){
        // No way of calculating totalhours has been implimented
        model.CalculateTotalHours(10);
        assertTrue(model.getTotalHours() == 10);
    }
}
