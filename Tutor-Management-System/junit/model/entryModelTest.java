package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class entryModelTest {
    private entryModel entry;
    
    @Before
    public void setUp() {
    	entry = new entryModel();
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
}
