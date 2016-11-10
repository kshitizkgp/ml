import static org.junit.Assert.*;

import org.junit.Test;
import junit.framework.TestCase;

public class ToolTest extends TestCase{
	private int value1;
	private int value2;
	
	public ToolTest(String testName) {
		super(testName);
	}
	protected void setUp() throws Exception {
		super.setUp();
		value1 = 3;
		value2 = 5;
	}
	protected void tearDown() throws Exception {
		super.tearDown();
		value1 = 0;
		value2 = 0;
	}
}
