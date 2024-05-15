package pages;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import pages.LambdatestPage;

import utils.Log;


public class LambdatestPage extends LoadableComponent <LambdatestPage> {
	
	private String appURL;
	private WebDriver driver;
	private boolean isPageLoaded;
	int maxElementWait = 20;
	String parentHandle;

	/**********************************************************************************************
	 ********************************* WebElements of Lamdatest Page ***********************************
	 **********************************************************************************************/

	@CacheLookup
	@FindBy(css = ".tracking-widest1")
	WebElement lnkIntegrations;

	@CacheLookup
	@FindBy(xpath="//div[@id='__next']/div/section/div/div/div/div/div/a")
	WebElement btnUpgrade;

	@CacheLookup
	@FindBy(id = "header")
	WebElement headerPanel;
	
	@FindBy(css = ".text-shadow")
	WebElement txtIntegration;
	
	@FindBy(xpath = "//button[@id='showInput']")
	WebElement btnShowMessage;

	/**********************************************************************************************
	 ********************************* WebElements of lambdatest Page - Ends ****************************
	 **********************************************************************************************/

	/**
	 * constructor of the class
	 * 
	 * @param driver
	 *            : Webdriver
	 * 
	 * @param url
	 *            : UAT URL
	 */
	public LambdatestPage(WebDriver driver, String url) {
		appURL = url;
		this.driver = driver;
		
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, maxElementWait);
		PageFactory.initElements(finder, this);
	}

	@Override
	protected void isLoaded() {

		
		if (!isPageLoaded) {
			Assert.fail();
		}
		
		FluentWait <WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(maxElementWait)).pollingEvery(Duration.ofMillis(500)).ignoring(StaleElementReferenceException.class).withMessage("Page Load Timed Out");
		
			
		WebElement waitElement = wait.until(ExpectedConditions.visibilityOf(lnkIntegrations));
		if (isPageLoaded && !(waitElement.isDisplayed() && waitElement.isEnabled())) {
			Log.fail("Lambdatest Page did not open up. Site might be down.");
		}
		

	}// isLoaded

	@Override
	protected void load() {

		isPageLoaded = true;
		driver.get(appURL);

		

	}// load

	public void clickSeeAllIntegration() {
		
	     try {
	    	 btnUpgrade.getText();
	    	 headerPanel.isDisplayed();
	    	 parentHandle =  driver.getWindowHandle();
	    	 ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", txtIntegration);
			Thread.sleep(500);
			FluentWait <WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(maxElementWait)).pollingEvery(Duration.ofMillis(500)).ignoring(StaleElementReferenceException.class).withMessage("Page Load Timed Out");
			
			WebElement waitElement = wait.until(ExpectedConditions.visibilityOf(lnkIntegrations));
			
			try {
				String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,Keys.RETURN);
				lnkIntegrations.sendKeys(selectLinkOpeninNewTab);
			}
			catch (NoSuchElementException e) {
				throw new SkipException(lnkIntegrations + " not found in page!!");
			}	
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//final long startTime = StopWatch.startTime();
		
			
		
	//	Log.event("Searched the provided product!", StopWatch.elapsedTime(startTime));
		
	}//searchProduct
	
	public String getWindowhandles() {
		String childHandle = null;
	     Set<String> handles = driver.getWindowHandles();
		for (String handle : handles) {
			driver.switchTo().window(handle);
			if(driver.getTitle().contains("Integrations")) {
				childHandle = driver.getWindowHandle();
				Log.message("Child window handle (Integration page) : "+handle);
			}
			else
				Log.message("Parent window handle (Lambdatest Home page) : "+handle);
		}			
		return childHandle;
	//	Log.event("Searched the provided product!", StopWatch.elapsedTime(startTime));
		
	}
	
	
	public boolean clickOnLink(String linkName) {
		try {
			driver.findElement(By.linkText(linkName)).click();
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
			String currentUrl = driver.getCurrentUrl();
			linkName = linkName.replace(" ", "-");
			if(currentUrl.contains(linkName.toLowerCase())) {
				System.out.println("Validated the URL after navigating to "+linkName);
				return true;
			}else {
				System.out.println("Error while validting the URL");
				return false;
			}			
		
	}catch(Exception e) {
		Log.message("Error while clicking on the link :"+e.getMessage());
		return false;
	}
	}
	
	public boolean enterMessagAndValidate(String messageText) {
		try {
			driver.findElement(By.id("user-message")).sendKeys(messageText);
			FluentWait <WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(maxElementWait)).pollingEvery(Duration.ofMillis(500)).ignoring(StaleElementReferenceException.class).withMessage("Page Load Timed Out");
			WebElement btntoGetMessage = wait.until(ExpectedConditions.visibilityOf(btnShowMessage));
			btntoGetMessage.click();
			WebElement getMessage = wait.until(ExpectedConditions.visibilityOf(
					driver.findElement(By.cssSelector("p[id='message']"))));
			String actualMessage = getMessage.getText();
			
			if(actualMessage.equals(messageText)) {
				System.out.println("Validated the Message displayed in right handside ");
				return true;
			}else {
				System.out.println("Error while validting the message");
				return false;
			}			
		
	}catch(Exception e) {
		Log.message("Error while clicking on the link :"+e.getMessage());
		return false;
	}
	}
}
