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
import utils.DataProviderUtils;

import utils.WebDriverFactory;

public class Task1 {
	WebDriver driver;

	

	@Test(description = "Lambdatest Automation Assignment", dataProviderClass = DataProviderUtils.class, dataProvider = "parallelTestDataProvider")
	public void test001(String browser) throws InterruptedException, MalformedURLException {
		try {
		driver = WebDriverFactory.getDriver(browser,null);
		String site = "https://www.lambdatest.com";
		LambdatestPage lambdaPage = new LambdatestPage(driver, site).get();
		String ExpectedUrl = "https://www.lambdatest.com/integrations";
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		WebElement header = driver.findElement(By.id("header"));
		wait.until(ExpectedConditions.visibilityOf(header));
		WebElement seeAllIntegraion = driver.findElement(By.cssSelector(".clearfix a[href*='integration1']"));
		lambdaPage.clickSeeAllIntegration();
		String childHandle = lambdaPage.getWindowhandles();
		driver.switchTo().window(childHandle);
		
		WebElement intHeading = driver.findElement(By.xpath("//h1[text()='LambdaTest Integrations']"));
		wait.until(ExpectedConditions.visibilityOf(intHeading));
		String ActualUrl = driver.getCurrentUrl();
		// assertEquals(ExpectedUrl,ActualUrl,,);
		assertEquals(ExpectedUrl, ActualUrl, "Verified the current Url is matched with expected Url");
		System.out.println("Closing the current window...");
		driver.close();
		driver.quit();
		}catch(Exception e) {
			System.out.println("Error: "+e.getMessage());
			Assert.fail();
		}
	}

	
}
