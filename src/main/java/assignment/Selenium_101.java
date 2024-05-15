package assignment;

import static org.testng.Assert.assertEquals;

import java.net.MalformedURLException;

import java.net.URL;

import java.time.Duration;

import java.util.ArrayList;

import java.util.HashMap;

import org.openqa.selenium.By;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.WindowType;

import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.edge.EdgeOptions;

import org.openqa.selenium.remote.RemoteWebDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.AfterMethod;

import org.testng.annotations.BeforeMethod;

import org.testng.annotations.Parameters;

import org.testng.annotations.Test;

import junit.framework.Assert;
import pages.LambdatestPage;
import pages.LambdatestPlayGroundPage;
import utils.DataProviderUtils;
import utils.Log;
import utils.WebDriverFactory;

public class Selenium_101 {
	

	

	@Test(description = "Test Scenario 01 - Validate the Message", dataProviderClass = DataProviderUtils.class, dataProvider = "parallelTestDataProvider")
	public void test001(String browser) throws InterruptedException, MalformedURLException {
		WebDriver driver = null;
		try {
		driver = WebDriverFactory.getDriver(browser,null);
		String site = "https://www.lambdatest.com/selenium-playground/";
		LambdatestPlayGroundPage lambdaPage = new LambdatestPlayGroundPage(driver, site).get();
		Assert.assertTrue("Error while navigating to Simple Form Demo page", lambdaPage.clickOnLink("Simple Form Demo"));
		Assert.assertTrue("Error while verifying the message", lambdaPage.enterMessagAndValidate("Welcome to LambdaTest"));
		((JavascriptExecutor) driver).executeScript("lambda-status=passed");
		}catch(Exception e) {
			System.out.println("Error: "+e.getMessage());
			((JavascriptExecutor) driver).executeScript("lambda-status=failed");
			Assert.fail();
		}finally {
			driver.close();
			driver.quit();
		}
	}

	
	@Test(description = "Test Scenario 02 - “Drag & Drop Sliders", dataProviderClass = DataProviderUtils.class, dataProvider = "parallelTestDataProvider")
	public void test002(String browser) throws InterruptedException, MalformedURLException {
		WebDriver driver = null;
		try {
		driver = WebDriverFactory.getDriver(browser,null);
		String site = "https://www.lambdatest.com/selenium-playground/";
		LambdatestPlayGroundPage lambdaPage = new LambdatestPlayGroundPage(driver, site).get();
		Assert.assertTrue("Error while navigating to Simple Form Demo page", lambdaPage.clickOnLink("Drag & Drop Sliders"));
		Assert.assertTrue("Error while verifying the message", lambdaPage.dragAndDrop(95));
		((JavascriptExecutor) driver).executeScript("lambda-status=passed");
		}catch(Exception e) {
			System.out.println("Error: "+e.getMessage());
			((JavascriptExecutor) driver).executeScript("lambda-status=failed");
			Assert.fail();
		}finally {
			driver.close();
			driver.quit();
		}
	}
	
	
	@Test(description = "Test Scenario 03 - Form Validation and Submit", dataProviderClass = DataProviderUtils.class, dataProvider = "parallelTestDataProvider")
	public void test003(String browser) throws InterruptedException, MalformedURLException {
		WebDriver driver = null;
		try {
		driver = WebDriverFactory.getDriver(browser,null);
		String site = "https://www.lambdatest.com/selenium-playground/";
		LambdatestPlayGroundPage lambdaPage = new LambdatestPlayGroundPage(driver, site).get();
		Assert.assertTrue("Error while navigating to input form submit page", lambdaPage.clickOnLink("Input Form Submit"));
		Assert.assertTrue("Error while verifying the field validation message", lambdaPage.submitAndVerifyError());
		Assert.assertTrue("Error while verifying the success message after filling the form", lambdaPage.fillFormAndSubmit());
		((JavascriptExecutor) driver).executeScript("lambda-status=passed");
		}catch(Exception e) {
			System.out.println("Error: "+e.getMessage());
			((JavascriptExecutor) driver).executeScript("lambda-status=failed");
			Assert.fail();
		}finally {
			driver.close();
			driver.quit();
		}
	}
	
}
