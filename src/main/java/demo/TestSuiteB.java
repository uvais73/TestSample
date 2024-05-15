package demo;

import org.testng.annotations.Test;



public class TestSuiteB {
	
	
	@Test(groups = "suiteC")
    public void testMethodB() {
        // Test method B logic
		System.out.println("Test Suiote B");
    }
	
}
