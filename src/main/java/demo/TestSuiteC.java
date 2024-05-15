package demo;

import org.testng.annotations.Test;

public class TestSuiteC {
	
	
	
	@Test(groups = "suiteB")
    public void testMethodB() {
        // Test method B logic
		System.out.println("Test Suiote C");
    }
	
}
