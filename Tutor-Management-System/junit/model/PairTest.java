package model;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class PairTest {
	private String leftString;
	private String rightString;
	private Pair<String, String> model;
	
	@Before
	public void setUp(){
		leftString = "left String";
		rightString = "right String";
        model = new Pair<String, String>(leftString,rightString);
    }
	
	@Test
    public void testGetLeft(){
		assertEquals(model.getLeft(), leftString);
    }
	
	@Test
    public void testGetRight(){
		assertEquals(model.getRight(), rightString);
    }
	
	@Test
    public void testSetLeft(){
        model.setLeft("new Left String");
        assertEquals(model.getLeft(), "new Left String");
    }
	
	@Test
    public void testSetRight(){
		model.setLeft("new Right String");
        assertEquals(model.getLeft(), "new Right String");
    }
}

