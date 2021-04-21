package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class EntryTest {
    private Entry entry;
    
    @Before
    public void setUp() {
    	entry = new Entry();
    }
    
    @Test
    public void testSetDate() {
    	entry.setDate("03/14/2021");
    	assertTrue(entry.getDate() == "03/14/2021");
    	
    }
    
    @Test
    public void testSetHours() {
    	entry.setHours(2.7);
    	assertTrue(entry.getHours() == 2.7);
    }
    
    @Test
    public void testSetServicePerformed() {
    	entry.setServicePerformed("tutoring");
    	assertTrue(entry.getServicePerformed() == "tutoring");
    }
    @Test
    public void testSetWherePerformed() {
    	entry.setWherePerformed("zoom");
    	assertTrue(entry.getWherePerformed() == "zoom");
    	
    }
    
    @Test
    public void testSetEntryID() {
    	
    	entry.setEntryID(1);
    	assertEquals(1, entry.getEntryID());
    }
    
    @Test
    public void testSetPayVoucherID() {
    	entry.setPayVoucherID(1);
    	assertEquals(1, entry.getPayVoucherID());
    }
    
    @Test
    public void testGetDate() {
    	entry.setDate("03/14/2021");
    	assertTrue(entry.getDate() == "03/14/2021");
    	
    }
    
    @Test
    public void testGetHours() {
    	entry.setHours(2.7);
    	assertTrue(entry.getHours() == 2.7);
    }
    
    @Test
    public void testGetServicePerformed() {
    	entry.setServicePerformed("tutoring");
    	assertTrue(entry.getServicePerformed() == "tutoring");
    }
    @Test
    public void testGetWherePerformed() {
    	entry.setWherePerformed("zoom");
    	assertTrue(entry.getWherePerformed() == "zoom");
    	
    }
    
    @Test
    public void testGetEntryID() {
    	
    	entry.setEntryID(1);
    	assertEquals(1, entry.getEntryID());
    }
    
    @Test
    public void testGetPayVoucherID() {
    	entry.setPayVoucherID(1);
    	assertEquals(1, entry.getPayVoucherID());
    }
}
