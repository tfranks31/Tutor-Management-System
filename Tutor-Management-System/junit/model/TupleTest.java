package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class TupleTest {
	private String leftString;
	private int middleInt;
	private Boolean rightBool;
	private Tuple<String, Integer, Boolean> model;
	
	@Before
	public void setUp(){
		leftString = "left String";
		middleInt = 7;
		rightBool = true;
        model = new Tuple<String, Integer, Boolean>(leftString,middleInt,rightBool);
    }
	
	@Test
    public void testGetLeft(){
		assertEquals(model.getLeft(), leftString);
    }
	
	public void testGetMiddle(){
		assertTrue(model.getMiddle() == middleInt);
    }
	
	@Test
    public void testGetRight(){
		assertTrue(model.getRight());
    }
	
	@Test
    public void testSetLeft(){
        model.setLeft("new Left String");
        assertEquals(model.getLeft(), "new Left String");
    }
	
	@Test
    public void testSetMiddle(){
        model.setMiddle(21);
        assertTrue(model.getMiddle() == 21);
    }
	
	@Test
    public void testSetRight(){
		model.setRight(false);
        assertTrue(!model.getRight());
    }
}
