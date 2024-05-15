package utils;

import org.testng.IAnnotationTransformer;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class Report extends TestListenerAdapter implements IAnnotationTransformer{
	
	@Override
	public void onTestFailure(ITestResult tr) {
		try {
			System.out.println("getMethod: "+tr.getMethod());
			System.out.println("getStatus: "+tr.getStatus());
			System.out.println("getName: "+tr.getName());
			System.out.println("getTestName: "+tr.getTestName());
			System.out.println("getMethodName: "+tr.getMethod().getMethodName());
			System.out.println("getClass: "+tr.getMethod().getTestClass().getName());
		}catch(Exception e) {
			System.out.println("Exception : "+e.getMessage());
		}
	}
}
