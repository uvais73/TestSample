package pages;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import pages.LambdatestPlayGroundPage;

import utils.Log;


public class LambdatestPlayGroundPage extends LoadableComponent <LambdatestPlayGroundPage> {
	
	private String appURL;
	private WebDriver driver;
	private boolean isPageLoaded;
	int maxElementWait = 40;
	String parentHandle;

	/**********************************************************************************************
	 ********************************* WebElements of Lamdatest Page ***********************************
	 **********************************************************************************************/

	@CacheLookup
	@FindBy(linkText = "Simple Form Demo")
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
	public LambdatestPlayGroundPage(WebDriver driver, String url) {
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
		
			
		WebElement waitElement = wait.until(ExpectedConditions.visibilityOf(btnShowMessage));
		if (isPageLoaded && !(waitElement.isDisplayed() && waitElement.isEnabled())) {
			Log.fail("Lambdatest Page did not open up. Site might be down.");
		}
		

	}// isLoaded

	@Override
	protected void load() {

		isPageLoaded = true;
		driver.get(appURL);

		

	}// load

	
	
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
			if(linkName.equalsIgnoreCase("Drag-&-Drop-Sliders"))
				linkName = "drag-drop-range-sliders-demo";
			if(linkName.equalsIgnoreCase("Input-Form-Submit"))
				linkName = "input-form-demo";
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
			WebElement btntoGetMessage = wait.until(ExpectedConditions.visibilityOf(
					driver.findElement(By.xpath("//button[@id='showInput']"))));
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
		Log.message("Error while validating the message :"+e.getMessage());
		return false;
	}
	}
	
	public boolean dragAndDrop(int value) {
		int count = 0;
		try {
			List<WebElement> sliders =driver.findElements(By.cssSelector("h4.font-bold.text-black.mb-10"));
			for (WebElement slide : sliders) {
				count++;
				if(slide.getText().contains("Default value 15")) {
				Actions move = new Actions(driver);
				WebElement slider = slide.findElement(By.xpath(".."));
				System.out.println(slider.getText());
				slider = slider.findElement(By.cssSelector("div.sp__range-success >input"));
			    move.dragAndDropBy(slider, 212, 16).build().perform();
			    //((Actions) action).perform();
			    
			    break;
				}
			}
			
			WebElement getCurrentValue = sliders.get(count-1).findElement(By.xpath(".."));
			System.out.println(getCurrentValue.getText());
			getCurrentValue = getCurrentValue.findElement(By.cssSelector("div.sp__range-success >output"));
			String actualMessage = getCurrentValue.getText();
			
			if(actualMessage.equals(Integer.toString(value))) {
				System.out.println("Validated the Value displayed after drag and drop");
				return true;
			}else {
				System.out.println("Error while validting the value after drag and drop ");
				return false;
			}			
		
	}catch(Exception e) {
		System.out.println("Error while doing the drag and drop :"+e.getMessage());
		return false;
	}
	}
	
	public boolean submitAndVerifyError() {
		try {
			driver.findElement(By.xpath("//button[contains(text(),'Submit')]")).click();
			String actualMessage = driver.findElement(By.name("name")).getAttribute("validationMessage");  
				Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
				String browserName = cap.getBrowserName().toLowerCase();
			    
			    String expectedMessage = null;
			if(browserName.equalsIgnoreCase("safari"))
				expectedMessage = "Fill out this field";
			else
				expectedMessage = "Please fill out this field.";
			if(actualMessage.equals(expectedMessage)) {
				System.out.println("Validation error message displayed and validated");
				return true;
			}else {
				System.out.println("Error while validating validation error message");
				return false;
			}			
		
	}catch(Exception e) {
		System.out.println("Error while validation of fields :"+e.getMessage());
		return false;
	}
	}
	
	
	public boolean fillFormAndSubmit() {
		try {
			int num = (int)(Math.random()*(1000-1+1));
			driver.findElement(By.name("name")).sendKeys("Name"+num);
			System.out.println("Name entered as "+"Name"+num);
			driver.findElement(By.id("inputEmail4")).sendKeys("email"+num+"@gmail.com");
			System.out.println("Email entered as "+"email"+num+"@gmail.com");
			driver.findElement(By.cssSelector("#inputPassword4")).sendKeys("Pass1"+num);
			System.out.println("Password entered as "+"Pass1"+num);
			driver.findElement(By.xpath("//input[@id='company']")).sendKeys("Company"+num);
			System.out.println("Company entered as "+"Company"+num);
			driver.findElement(By.xpath("//form[@id='seleniumform']/div[2]/div[2]/input")).sendKeys("Website"+num);
			System.out.println("Website entered as "+"Website"+num);
			
			//Select the Country
			Select drpCountry = new Select(driver.findElement(By.name("country")));
			drpCountry.selectByVisibleText("United States");
			driver.findElement(By.cssSelector("#inputCity")).sendKeys("Miami");
			System.out.println("City entered as "+"Miami");
			
			driver.findElement(By.id("inputAddress1")).sendKeys("Address line 1 "+num);
			System.out.println("Address line 1 entered as "+"Address line 1 "+num);
			
			driver.findElement(By.id("inputAddress2")).sendKeys("Address line 2 "+num);
			System.out.println("Address line 2 entered as "+"Address line 2 "+num);
			
			driver.findElement(By.cssSelector("#inputState")).sendKeys("Florida");
			System.out.println("State entered as "+"Florida");
			
			driver.findElement(By.xpath("//input[@id='inputZip']")).sendKeys("303"+num);
			System.out.println("Zipcode entered as "+"303"+num);
			
			driver.findElement(By.xpath("//button[contains(text(),'Submit')]")).click();
			
			FluentWait <WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(maxElementWait)).pollingEvery(Duration.ofMillis(500)).ignoring(StaleElementReferenceException.class).withMessage("Page Load Timed Out");
			WebElement successMessage = wait.until(ExpectedConditions.visibilityOf(
					driver.findElement(By.cssSelector(".success-msg"))));
			
			if(successMessage.getText().equals("Thanks for contacting us, we will get back to you shortly.")) {
				System.out.println("Success message has been validated");
				return true;
			}else {
				System.out.println("Error while validating success message");
				return false;
			}			
		
	}catch(Exception e) {
		System.out.println("Error while validation Success message :"+e.getMessage());
		return false;
	}
	}
}
