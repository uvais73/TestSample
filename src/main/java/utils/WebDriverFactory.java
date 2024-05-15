package utils;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;

import io.github.bonigarcia.wdm.WebDriverManager;
//import io.github.bonigarcia.wdm.WebDriverManager;
import utils.Log;
import utils.WebDriverFactory;

import utils.PropertyReader;

public class WebDriverFactory {
	private static final long maxPageLoadWait = 20;
	private static Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);
	private static PropertyReader configProperty = PropertyReader.getInstance();
	
	static String driverHost;
	static String driverPort;
	static String browserName;
	static String deviceName;
	static URL hubURL;
	
	static DesiredCapabilities ieCapabilities1 = new DesiredCapabilities();
	static DesiredCapabilities firefoxCapabilities1 = new DesiredCapabilities();
	static DesiredCapabilities chromeCapabilities1 = new DesiredCapabilities();
	static DesiredCapabilities safariCapabilities1 = new DesiredCapabilities();
	static DesiredCapabilities edgeCapabilities1 = new DesiredCapabilities();
	static ChromeOptions opt = new ChromeOptions();
	
	/**
	 * Webdriver to get the web driver with browser name and platform and setting
	 * the desired capabilities for browsers
	 * 
	 * @param browserWithPlatform - Browser With Platform
	 * @param proxy               - Proxy
	 * @return driver - WebDriver Instance
	 * @throws MalformedURLException
	 */
	public static WebDriver getDriver(String browserWithPlatform, Proxy proxy) throws MalformedURLException {
		String browser = null;
		String platform = null;
		String sauceUserName = null;
		String sauceAuthKey = null;
		String bsUserName = null;
		String bsAuthKey = null;
		String lambdaUserName = null;
		String lambdaAuthKey = null;
		WebDriver driver = null;
		String userAgent = null;
		// Get invoking test name
		String callerMethodName = new Exception().getStackTrace()[1].getMethodName();
				// driver initialization part
		synchronized (System.class) {
			// From local to sauce lab for browser test

			/*
			 * System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
			 * System.out.println( "^" + System.getProperty("runSauceLabFromLocal") + " ^ "
			 * + System.getenv("runSauceLabFromLocal"));
			 */

			if (configProperty.hasProperty("runLambdaTestFromLocal")
					&& configProperty.getProperty("runLambdaTestFromLocal").trim().equalsIgnoreCase("true")) {

				lambdaUserName = configProperty.hasProperty("lambdaUserName")
						? configProperty.getProperty("lambdaUserName")
						: null;
				lambdaAuthKey = configProperty.hasProperty("lambdaAuthKey")
						? configProperty.getProperty("lambdaAuthKey")
						: null;

						

						DesiredCapabilities lambdaDesiredCapabilities = new DesiredCapabilities();

						// String browserName =
						// Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
						// .getParameter("browserName");

						String browserName = browserWithPlatform.split("&")[0];

						String browserVersion = browserWithPlatform.split("&")[1];
						System.out.println(browserVersion);
						String platformName = browserWithPlatform.split("&")[2];

						String lambdaConnect = configProperty.hasProperty("lambdaConnect") ? configProperty.getProperty("lambdaConnect")
								: null;
						String tunnelIdentifier = configProperty.hasProperty("tunnelIdentifier")
								? configProperty.getProperty("tunnelIdentifier")
								: "";

						String projectName = configProperty.hasProperty("projectName") ? configProperty.getProperty("projectName")
								: null;
						String resolution = configProperty.hasProperty("resolution") ? configProperty.getProperty("resolution") : null;

						

						AbstractDriverOptions browserOptions = null;

						if (browserName.toLowerCase().contains("chrome")) {
							browserOptions = new ChromeOptions();
							browserOptions.setPlatformName(platformName);
							browserOptions.setBrowserVersion(browserVersion);
							HashMap<String, Object> ltOptions = new HashMap<String, Object>();
							ltOptions.put("project", projectName);
							ltOptions.put("w3c", true);
							ltOptions.put("build", projectName);
							ltOptions.put("name", callerMethodName);
							ltOptions.put("selenium_version", "4.9.0");
							ltOptions.put("network", true);
							ltOptions.put("visual", true);
					        ltOptions.put("video", true);
					        ltOptions.put("console", true);
							if (Boolean.parseBoolean(lambdaConnect)) {
								browserOptions.setCapability("tunnel", true);
								browserOptions.setCapability("tunnelIdentifier", tunnelIdentifier);
							}

							browserOptions.setCapability("LT:Options", ltOptions);

						} else if (browserName.toLowerCase().contains("firefox")) {
							browserOptions = new FirefoxOptions();
							browserOptions.setPlatformName(platformName);
							browserOptions.setBrowserVersion(browserVersion);
							HashMap<String, Object> ltOptions = new HashMap<String, Object>();
							ltOptions.put("project", projectName);
							ltOptions.put("w3c", true);
							ltOptions.put("build", projectName);
							ltOptions.put("name", callerMethodName);
							ltOptions.put("selenium_version", "4.9.0");
							ltOptions.put("visual", true);
					        ltOptions.put("video", true);
					        ltOptions.put("console", true);
							if (Boolean.parseBoolean(lambdaConnect)) {
								browserOptions.setCapability("tunnel", true);
								browserOptions.setCapability("tunnelIdentifier", tunnelIdentifier);
							}

							browserOptions.setCapability("LT:Options", ltOptions);
						} else if (browserName.toLowerCase().contains("edge")) {
							browserOptions = new EdgeOptions();
							browserOptions.setPlatformName("macOS Catalina");
							browserOptions.setBrowserVersion("87.0");
							HashMap<String, Object> ltOptions = new HashMap<String, Object>();
							ltOptions.put("project", projectName);
							ltOptions.put("w3c", true);
							ltOptions.put("build", projectName);
							ltOptions.put("name", callerMethodName);
							ltOptions.put("selenium_version", "4.9.0");
							ltOptions.put("visual", true);
					        ltOptions.put("video", true);
					        ltOptions.put("console", true);
					        ltOptions.put("enableDebugLogsInMartian" ,false);
					        ltOptions.put("enableNetworkThrottling" ,false);
					        ltOptions.put("enableSSEInMartian" ,false);
							if (Boolean.parseBoolean(lambdaConnect)) {
								browserOptions.setCapability("tunnel", true);
								browserOptions.setCapability("tunnelIdentifier", tunnelIdentifier);
							}
							browserOptions.setCapability("LT:Options", ltOptions);
						} else if (browserName.toLowerCase().contains("safari")) {
							browserOptions = new SafariOptions();
							browserOptions.setPlatformName(platformName);
							browserOptions.setBrowserVersion(browserVersion);
							HashMap<String, Object> ltOptions = new HashMap<String, Object>();
							ltOptions.put("project", projectName);
							ltOptions.put("w3c", true);
							ltOptions.put("build", projectName);
							ltOptions.put("name", callerMethodName);
							ltOptions.put("selenium_version", "4.9.0");
							ltOptions.put("visual", true);
					        ltOptions.put("video", true);
					        ltOptions.put("console", true);
							if (Boolean.parseBoolean(lambdaConnect)) {
								browserOptions.setCapability("tunnel", true);
								browserOptions.setCapability("tunnelIdentifier", tunnelIdentifier);
							}
							browserOptions.setCapability("LT:Options", ltOptions);
						}

						driver = new RemoteWebDriver(
								new URL("https://" + lambdaUserName + ":" + lambdaAuthKey + "@hub.lambdatest.com/wd/hub"),
								browserOptions);
						driver.manage().window().maximize();

						String lambdaSessionId = (((RemoteWebDriver) driver).getSessionId()).toString();

						String lambdaLink = "https://automation.lambdatest.com/logs/?sessionID=" + lambdaSessionId;

						System.out.println("LambdaTest link for " + callerMethodName + ":: " + lambdaLink);
						
						return driver;
				//driver = createlambdaTestWebDriver(browserWithPlatform, callerMethodName, lambdaUserName, lambdaAuthKey);
				//return driver;
			}

			boolean runScriptsInCloudFromJenkin = System.getProperty("runSauceLabFromLocal") != null ? true : false;
			boolean runScriptsInAWS = System.getProperty("runAWS") != null ? true : false;
			String cloudDecider = "";
			String configDecider = "";
			if (runScriptsInCloudFromJenkin) {
				// jenkin
				cloudDecider = System.getProperty("runSauceLabFromLocal").equalsIgnoreCase("true")
						? "runSauceLabFromLocal"
						: "runBrowserStackFromLocal";
				System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++");
				System.out.println("^" + System.getProperty("runSauceLabFromLocal") + " ^ "
						+ System.getenv("runSauceLabFromLocal"));
			} else if (runScriptsInAWS) {
				cloudDecider = "runAWS";
			} else {
				// config
				configDecider = configProperty.hasProperty("runSauceLabFromLocal")
						&& configProperty.getProperty("runSauceLabFromLocal").trim().equalsIgnoreCase("true")
								? "runSauceLabFromLocal"
								: configProperty.hasProperty("runBrowserStackFromLocal") && configProperty
										.getProperty("runBrowserStackFromLocal").trim().equalsIgnoreCase("true")
												? "runBrowserStackFromLocal"
												: configProperty.hasProperty("runAWS") && configProperty
														.getProperty("runAWS").trim().equalsIgnoreCase("true")
																? "runAWS"
																: "local";
			}

			sauceUserName = configProperty.hasProperty("sauceUserName") ? configProperty.getProperty("sauceUserName")
					: null;
			sauceAuthKey = configProperty.hasProperty("sauceAuthKey") ? configProperty.getProperty("sauceAuthKey")
					: null;

			bsUserName = configProperty.hasProperty("bsUserName") ? configProperty.getProperty("bsUserName") : null;
			bsAuthKey = configProperty.hasProperty("bsAuthKey") ? configProperty.getProperty("bsAuthKey") : null;

			switch (cloudDecider.toUpperCase()) {}

			switch (configDecider.toUpperCase()) {}

			/*
			 * if (cloudDecider.equalsIgnoreCase("runSauceLabFromLocal")) { driver =
			 * createSaucelabsWebDriver(callerMethodName, sauceUserName, sauceAuthKey);
			 * return driver; } else if
			 * (cloudDecider.equalsIgnoreCase("runBrowserStackFromLocal")) { driver =
			 * createBrowserStackWebDriver(callerMethodName, bsUserName, bsAuthKey); return
			 * driver; } else if (configDecider.equalsIgnoreCase("runSauceLabFromLocal")) {
			 * driver = createSaucelabsWebDriver(callerMethodName, sauceUserName,
			 * sauceAuthKey); return driver; } else if
			 * (configDecider.equalsIgnoreCase("runBrowserStackFromLocal")) { driver =
			 * createBrowserStackWebDriver(callerMethodName, bsUserName, bsAuthKey); return
			 * driver; }
			 */
			/*
			 * if (configProperty.hasProperty("runSauceLabFromLocal") &&
			 * configProperty.getProperty("runSauceLabFromLocal").trim().equalsIgnoreCase(
			 * "true")) {
			 * 
			 * driver = createSaucelabsWebDriver(callerMethodName, sauceUserName,
			 * sauceAuthKey); return driver; }
			 * 
			 * // From local to sauce lab for browser test else if
			 * (configProperty.hasProperty("runBrowserStackFromLocal") &&
			 * configProperty.getProperty("runBrowserStackFromLocal").trim().
			 * equalsIgnoreCase("true")) {
			 * 
			 * driver = createBrowserStackWebDriver(callerMethodName, bsUserName,
			 * bsAuthKey); return driver; }
			 */
		}
		// To support local to local execution by grid configuration
		if (browserWithPlatform.contains("_")) {
			browser = browserWithPlatform.split("_")[0].toLowerCase().trim();
			platform = browserWithPlatform.split("_")[1].toUpperCase().trim();
			if (configProperty.hasProperty("runSauceLabFromLocal")
					&& configProperty.getProperty("runSauceLabFromLocal").trim().equalsIgnoreCase("false")) {
				platform = platform.split(" ")[0];
			}

		} else {
			platform = "ANY";
			browser = browserWithPlatform;
		}

		try {
			String headless = configProperty.getProperty("headless") != null ? configProperty.getProperty("headless")
					: "false";

			if ("chrome".equalsIgnoreCase(browser)) {
				//WebDriverManager.chromedriver().setup();
				ChromeOptions chromeOptions = new ChromeOptions();
				if (configProperty.hasProperty("runUserAgentDeviceTest")
						&& configProperty.getProperty("runUserAgentDeviceTest").equalsIgnoreCase("true")) {
					deviceName = configProperty.hasProperty("deviceName") ? configProperty.getProperty("deviceName")
							: null;
					// userAgent = mobEmuUA.getUserAgent(deviceName);
					// if (userAgent != null && deviceName != null) {
					// driver = new RemoteWebDriver(hubURL, setChromeUserAgent(deviceName,
					// userAgent));
					if (deviceName != null) {
						Map<String, String> mobileEmulation = new HashMap<>();
						mobileEmulation.put("deviceName", deviceName);
						chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);

						driver = new ChromeDriver(chromeOptions);
					} else {
						logger.error(
								"Given user agent configuration not yet implemented (or) check the parameters(deviceName) value in config.properties: "
										+ deviceName);
					}
				} else {
					chromeOptions.addArguments("--start-maximized");
					chromeOptions.addArguments("--disable-web-security");
					// chromeOpt.setExperimentalOption("w3c", true);
					Map<String, Object> prefs = new HashMap<String, Object>();
					prefs.put("credentials_enable_service", false);
					prefs.put("profile.password_manager_enabled", false);
					chromeOptions.setExperimentalOption("prefs", prefs);

					if (!headless.equals("true") && !headless.equals("false"))
						headless = "false";

					chromeOptions.setHeadless(Boolean.parseBoolean(headless));
					chromeCapabilities1.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
					chromeCapabilities1.setPlatform(Platform.fromString(platform));
					if (proxy != null)
						chromeCapabilities1.setCapability(CapabilityType.PROXY, proxy);

					// driver = new RemoteWebDriver(hubURL, chromeCapabilities1);

					driver = new ChromeDriver(chromeOptions);
					driver.manage().window().maximize();
				}
			} else if ("iexplorer".equalsIgnoreCase(browser)) {
				ieCapabilities1.setCapability("enablePersistentHover", false);
				ieCapabilities1.setCapability("ignoreZoomSetting", true);
				ieCapabilities1.setCapability("nativeEvents", false);
				ieCapabilities1.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
				ieCapabilities1.setBrowserName("internet explorer");
				ieCapabilities1.setPlatform(Platform.fromString(platform));

				if (proxy != null)
					ieCapabilities1.setCapability(CapabilityType.PROXY, proxy);

				driver = new RemoteWebDriver(hubURL, ieCapabilities1);
			} else if (browser.toLowerCase().contains("edge")) {
				WebDriverManager.edgedriver().setup();
				EdgeOptions edgeOptions = new EdgeOptions();

				if (configProperty.hasProperty("runUserAgentDeviceTest")
						&& configProperty.getProperty("runUserAgentDeviceTest").equalsIgnoreCase("true")) {
					deviceName = configProperty.hasProperty("deviceName") ? configProperty.getProperty("deviceName")
							: null;
					// userAgent = mobEmuUA.getUserAgent(deviceName);
					// if (userAgent != null && deviceName != null) {
					// driver = new RemoteWebDriver(hubURL, setChromeUserAgent(deviceName,
					// userAgent));
					if (deviceName != null) {
						Map<String, String> mobileEmulation = new HashMap<>();
						mobileEmulation.put("deviceName", deviceName);
						edgeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);

						driver = new EdgeDriver(edgeOptions);
					} else {
						logger.error(
								"Given user agent configuration not yet implemented (or) check the parameters(deviceName) value in config.properties: "
										+ deviceName);
					}
				} else {
					edgeOptions.setHeadless(Boolean.parseBoolean(headless));
					edgeCapabilities1.setPlatform(Platform.fromString(platform));
					edgeCapabilities1.setBrowserName("MicrosoftEdge");
					// driver = new RemoteWebDriver(hubURL, edgeCapabilities1);

					driver = new EdgeDriver(edgeOptions);
					driver.manage().window().maximize();
				}
			} else if (browser.toLowerCase().contains("firefox")) {

				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();
				driver.manage().window().maximize();
			} else if ("safari".contains(browser.split("\\&")[0])) {
				if (configProperty.hasProperty("runUserAgentDeviceTest")
						&& configProperty.getProperty("runUserAgentDeviceTest").equalsIgnoreCase("true")) {
					
				} else {
					safariCapabilities1.setCapability("prerun",
							"https://gist.githubusercontent.com/saucyallison/3a73a4e0736e556c990d/raw/d26b0195d48b404628fc12342cb97f1fc5ff58ec/disable_fraud.sh");
					driver = new RemoteWebDriver(hubURL, safariCapabilities1);
				}
				// To run a ZAP TC's use Browser opt as zap
			} else if ("zap".equalsIgnoreCase(browser)) {
				Proxy zapChromeProxy = new Proxy();
				zapChromeProxy.setHttpProxy("localhost:8082");
				zapChromeProxy.setFtpProxy("localhost:8082");
				zapChromeProxy.setSslProxy("localhost:8082");
				chromeCapabilities1.setCapability(ChromeOptions.CAPABILITY, opt);
				chromeCapabilities1.setCapability(CapabilityType.PROXY, zapChromeProxy);
				chromeCapabilities1.setPlatform(Platform.fromString(platform));
				driver = new RemoteWebDriver(hubURL, chromeCapabilities1);
			} else {
				synchronized (WebDriverFactory.class) {
					FirefoxOptions foptions = new FirefoxOptions();

					if (!headless.equals("true") && !headless.equals("false"))
						headless = "false";

					foptions.setHeadless(Boolean.parseBoolean(headless));
					firefoxCapabilities1.setCapability("unexpectedAlertBehaviour", "ignore");
					firefoxCapabilities1.setPlatform(Platform.fromString(platform));
					firefoxCapabilities1.merge(foptions);
					driver = new RemoteWebDriver(hubURL, firefoxCapabilities1);
				}
			}
			// driver.manage().window().maximize();
			driver.manage().timeouts().pageLoadTimeout(maxPageLoadWait, TimeUnit.SECONDS);
			Assert.assertNotNull(driver,
					"Driver did not intialize...\n Please check if hub is running / configuration settings are corect.");

		} catch (UnreachableBrowserException e) {
			e.printStackTrace();
			throw new SkipException("Hub is not started or down.");
		} catch (WebDriverException e) {

			try {
				if (driver != null) {
					driver.quit();
				}
			} catch (Exception e1) {
				e.printStackTrace();
			}

			if (e.getMessage().toLowerCase().contains("error forwarding the new session empty pool of vm for setup")) {
				throw new SkipException("Node is not started or down.");
			} else if (e.getMessage().toLowerCase()
					.contains("error forwarding the new session empty pool of vm for setup")
					|| e.getMessage().toLowerCase().contains("cannot get automation extension")
					|| e.getMessage().toLowerCase().contains("chrome not reachable")) {
				Log.message("&emsp;<b> --- Re-tried as browser crashed </b>");
				try {
					driver.quit();
				} catch (WebDriverException e1) {
					e.printStackTrace();
				}
				
			} else {
				throw e;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception encountered in getDriver Method." + e.getMessage().toString());
		} finally {
			// ************************************************************************************************************
			// * Update start time of the tests once free slot is assigned by
			// RemoteWebDriver - Just for reporting purpose
			// *************************************************************************************************************/
			try {
				Field f = Reporter.getCurrentTestResult().getClass().getDeclaredField("m_startMillis");
				f.setAccessible(true);
				f.setLong(Reporter.getCurrentTestResult(), Calendar.getInstance().getTime().getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		
		try {
			browserWithPlatform = browserWithPlatform.split(" ")[0];
		} catch (Exception e) {
			// TODO: handle exception
		}
		Log.addExecutionEnvironmentToReport(driver, browserWithPlatform);
		return driver;
	}
	
	public static WebDriver createlambdaTestWebDriver(String browserConfig,String testName, String lambdaUserName, String lambdaAuthKey)
			throws MalformedURLException {

		WebDriver driver = null;
		String userAgent = null;

		DesiredCapabilities lambdaDesiredCapabilities = new DesiredCapabilities();

		// String browserName =
		// Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest()
		// .getParameter("browserName");

		String browserName = browserConfig.split("&")[0];

		String browserVersion = browserConfig.split("&")[1];
		System.out.println(browserVersion);
		String platformName = browserConfig.split("&")[2];

		String lambdaConnect = configProperty.hasProperty("lambdaConnect") ? configProperty.getProperty("lambdaConnect")
				: null;
		String tunnelIdentifier = configProperty.hasProperty("tunnelIdentifier")
				? configProperty.getProperty("tunnelIdentifier")
				: "";

		String projectName = configProperty.hasProperty("projectName") ? configProperty.getProperty("projectName")
				: null;
		String resolution = configProperty.hasProperty("resolution") ? configProperty.getProperty("resolution") : null;

		

		AbstractDriverOptions browserOptions = null;

		if (browserName.toLowerCase().contains("chrome")) {
			browserOptions = new ChromeOptions();
			browserOptions.setPlatformName(platformName);
			browserOptions.setBrowserVersion(browserVersion);
			HashMap<String, Object> ltOptions = new HashMap<String, Object>();
			ltOptions.put("project", projectName);
			ltOptions.put("w3c", true);
			ltOptions.put("build", projectName);
			ltOptions.put("name", testName);
			if (Boolean.parseBoolean(lambdaConnect)) {
				browserOptions.setCapability("tunnel", true);
				browserOptions.setCapability("tunnelIdentifier", tunnelIdentifier);
			}

			browserOptions.setCapability("LT:Options", ltOptions);

		} else if (browserName.toLowerCase().contains("firefox")) {
			browserOptions = new FirefoxOptions();
			browserOptions.setPlatformName(platformName);
			browserOptions.setBrowserVersion(browserVersion);
			HashMap<String, Object> ltOptions = new HashMap<String, Object>();
			ltOptions.put("project", projectName);
			ltOptions.put("w3c", true);
			ltOptions.put("build", projectName);
			ltOptions.put("name", testName);
			if (Boolean.parseBoolean(lambdaConnect)) {
				browserOptions.setCapability("tunnel", true);
				browserOptions.setCapability("tunnelIdentifier", tunnelIdentifier);
			}

			browserOptions.setCapability("LT:Options", ltOptions);
		} else if (browserName.toLowerCase().contains("edge")) {
			browserOptions = new EdgeOptions();
			browserOptions.setPlatformName("macOS Sierra");
			browserOptions.setBrowserVersion("87.0");
			HashMap<String, Object> ltOptions = new HashMap<String, Object>();
			ltOptions.put("project", projectName);
			ltOptions.put("w3c", true);
			ltOptions.put("build", projectName);
			ltOptions.put("name", testName);

			if (Boolean.parseBoolean(lambdaConnect)) {
				browserOptions.setCapability("tunnel", true);
				browserOptions.setCapability("tunnelIdentifier", tunnelIdentifier);
			}
			browserOptions.setCapability("LT:Options", ltOptions);
		} else if (browserName.toLowerCase().contains("safari")) {
			browserOptions = new SafariOptions();
			browserOptions.setPlatformName(platformName);
			browserOptions.setBrowserVersion(browserVersion);
			HashMap<String, Object> ltOptions = new HashMap<String, Object>();
			ltOptions.put("project", projectName);
			ltOptions.put("w3c", true);
			ltOptions.put("build", projectName);
			ltOptions.put("name", testName);

			if (Boolean.parseBoolean(lambdaConnect)) {
				browserOptions.setCapability("tunnel", true);
				browserOptions.setCapability("tunnelIdentifier", tunnelIdentifier);
			}
			browserOptions.setCapability("LT:Options", ltOptions);
		}

		driver = new RemoteWebDriver(
				new URL("https://" + lambdaUserName + ":" + lambdaAuthKey + "@hub.lambdatest.com/wd/hub"),
				browserOptions);
		driver.manage().window().maximize();

		String lambdaSessionId = (((RemoteWebDriver) driver).getSessionId()).toString();

		String lambdaLink = "https://automation.lambdatest.com/logs/?sessionID=" + lambdaSessionId;

		logger.debug("LambdaTest link for " + testName + ":: " + lambdaLink);
		Log.addExecutionEnvironmentToReport(driver, browserName + "_" + platformName);
		Log.addLambdaJobUrlToReport(driver, lambdaLink);
		return driver;
	}

	
	public static WebDriver getDriver(String browserWithPlatform) throws MalformedURLException {
		String browser = null;
		String platform = null;
		String browserVersion = null;
		String platformName = null;
		String lambdaTestUserName = null;
		String authKey = null;
		String localhost = null;
		String port = null;
		String platformVersion = null;
		WebDriver driver = null;
	
		
		String userAgent = null;
		//long startTime = StopWatch.startTime();

		// Get invoking test name to pass on to Jenkins
		String callerMethodName = new Exception().getStackTrace()[1].getMethodName();
		String driverInitializeInfo[] = null;

		// Handling System property variable overridden on parallel execution
		// till web driver initialization part
		synchronized (System.class) {
			// From local to sauce lab for browser test
			if (configProperty.hasProperty("runLambdaTestFromLocal") && configProperty.getProperty("runLambdaTestFromLocal").equalsIgnoreCase("true")) {
				System.out.println("name " + configProperty.getProperty("lambdaUserName"));
				lambdaTestUserName = configProperty.hasProperty("lambdaUserName")
					      ? configProperty.getProperty("lambdaUserName") : null;
					    authKey = configProperty.hasProperty("lambdaAuthKey") ? configProperty.getProperty("lambdaAuthKey")
					      : null;
				
				if (configProperty.hasProperty("runDesktop")
					      && configProperty.getProperty("runDesktop").equalsIgnoreCase("true")) {

					     if (browserWithPlatform.contains("&")) {
					      driverInitializeInfo = browserWithPlatform.split("&");
					      browser = driverInitializeInfo[0];

					      // BrowserType enum available in selenium remote class,
					      // so
					      // here pointing our framework BrowserType
					      browser =driverInitializeInfo[0];
					      browserVersion = driverInitializeInfo[1];
					      platform = driverInitializeInfo[2];
					     }

					     DesiredCapabilities caps = new DesiredCapabilities();
					    // caps.setCapability("build", "1.0");
					    // caps.setCapability("buildName", "Lambda test cross browser testing");
					     caps.setCapability("browserName", browser);
					     //caps.setCapability("version", browserVersion);
					     caps.setCapability("platformName", platform);
					     //caps.setCapability("network", true);
					   //  caps.setCapability("console", true);
					 //    caps.setCapability("visual", true);
					     URL hub = new URL("https://"+lambdaTestUserName+ ":" + authKey +"@hub.lambdatest.com/wd/hub");
					     
					     try {
					    	 driver = new RemoteWebDriver(hub,caps);
					    	 return driver;
					     }catch(Exception e) {
					    	 Log.message(e.getMessage());
					     }
					     
			 }
			
			}
		}
		return driver;
	}
}
