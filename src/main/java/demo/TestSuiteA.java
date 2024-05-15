package demo;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class TestSuiteA {
	
	@BeforeSuite
    public void testMethodA() {
        // Test method A logic
		System.out.println("Before group");
    }
}
