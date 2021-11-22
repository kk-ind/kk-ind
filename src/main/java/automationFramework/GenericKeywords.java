package automationFramework;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import utilities.DataDriver;
import utilities.PropertiesFile;
import utilities.TransferFiles;
import utilities.XMLReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.Select;

import automationFramework.GenericKeywords.browserType;
import baseClass.BaseClass;
import iSAFE.ApplicationKeywords;

public class GenericKeywords extends DataDriver {
	public String suiteResultsDir = "";
	public static List<String> cssSuiteDir = new ArrayList<String>();
	public DesiredCapabilities capabilityMobile = new DesiredCapabilities();
	public DesiredCapabilities capabilityWeb = new DesiredCapabilities();
	public RemoteWebDriver driver;
	public RemoteWebDriver adminSession;
	public RemoteWebDriver tenantSession;
	public static String outputDirectory, currentExcelBook, mainWindow;
	public String identifier, locator, locatorDescription;
	public String currentBrowser = "";
	public static Logger logger;
	public boolean testCaseExecutionStatus = false;
	public static int currentTestCaseNumber, currentExcel, currentStep, failureNo, screenshotNo;
	public static int rowCount, colCount;
	public static identifierType idType;
	public WebElement webElement;
	public boolean testFailure = false;
	public boolean loadFailure = false;
	public static int temp = 1;
	public static String testStatus = "";
	public int textLoadWaitTime, elementLoadWaitTime, implicitlyWaitTime, pageLoadWaitTime = 0;
	public static final String XSLT_FILE_CoverPage = "./src/main/resources/xsltfiles/CoverPage.xslt";
	public static final String XSLT_FILE_GridPage = "./src/main/resources/xsltfiles/gridReport.xslt";
	public static final String XSLT_FILE_SummaryPage = "./src/main/resources/xsltfiles/SummaryReport.xslt";
	public static final String XSLT_FILE_ReportPage = "./src/main/resources/xsltfiles/ResultPage.xslt";
	public static boolean windowreadyStateStatus = true;
	public int testFailureCount = 0;
	public static String timeStamp = "";
	public static List<Object> sessionIDswithHostName = new ArrayList<Object>();
	public static List<String> hostName = new ArrayList<String>();
	public static List<String> machineNames = new ArrayList<String>();
	public static List<String> testNameSuite = new ArrayList<String>();
	public static List<String> osName = new ArrayList<String>();
	public static List<String> browserName = new ArrayList<String>();
	public static List<String> browserVersionName = new ArrayList<String>();
	public static List<String> osVersionName = new ArrayList<String>();
	public String currentExecutionMachineName = "";
	public static List<String> testCaseResultsDir = new ArrayList<String>();
	public static List<String> coverPageResultsDir = new ArrayList<String>();
	public static List<String> summaryPageResultsDir = new ArrayList<String>();
	public static List<String> failurePageResultsDir = new ArrayList<String>();
	public static List<String> gridPageResultsDir = new ArrayList<String>();
	public int maxNoFailures = 0;
	public int totValidFailures = 0;
	public int totExceptionFailures = 0;
	public static Set<String> failureTcs = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	public static HashMap<String, String> failureTcsDescription = new HashMap<String, String>();
	public String udid = "";
	public static List<String> xmlReport = new ArrayList<String>();
	public static int manualScreenshotNo;

	public enum platFormName {
		IOS, ANDROID, WINDOWS, MAC, LINUX
	}

	public enum identifierType {
		xpath, name, id, lnktext, partiallinktext, classname, cssselector, tagname, androiduiautomator
	}

	public enum browserType {
		FIREFOX, EDGE, INTERNETEXPLORER, CHROME, OPERA, SAFARI, ADMIN, TENANT
	}
	// Below is commented and to be implemented if we need to use cross browser
//	public enum browserType {
//		FIREFOX, EDGE, INTERNETEXPLORER, CHROME, OPERA, SAFARI, ADMIN, TENANT
//	}

	public GenericKeywords(BaseClass obj) {

		super(obj);
		this.udid = obj.udid;
		this.testCaseExecutionStatus = obj.testCaseExecutionStatus;
		this.suiteResultsDir = obj.suiteResultsDir;
		this.maxNoFailures = obj.maxNoFailures;
		this.totValidFailures = obj.totExceptionFailures;
		this.elementLoadWaitTime = obj.elementLoadWaitTime;
		this.driver = obj.driver;
		this.adminSession = obj.adminSession;
		this.tenantSession = obj.tenantSession;
		this.testFailure = obj.testFailure;
		this.capabilityWeb = obj.capabilityWeb;
		this.capabilityMobile = obj.capabilityMobile;
		this.currentBrowser = obj.currentBrowser;
		this.locatorDescription = obj.locatorDescription;
		this.identifier = obj.identifier;
		this.locator = obj.locator;
		this.pageLoadWaitTime = obj.pageLoadWaitTime;
		this.testFailureCount = obj.testFailureCount;
		this.loadFailure = obj.loadFailure;
		this.webElement = obj.webElement;
		this.implicitlyWaitTime = obj.implicitlyWaitTime;
		this.totExceptionFailures = obj.totExceptionFailures;
		this.textLoadWaitTime = obj.textLoadWaitTime;
		this.currentExecutionMachineName = obj.currentExecutionMachineName;
	}

	public GenericKeywords() {

	}

	public void currentExecutionMachineName(String machineName) {
		currentExecutionMachineName = machineName;
	}

	public void testResultsFolder(String machineName, String host, String port, String browser, String os,
			String browserVersion, String osVersion) {

		suiteResultsDir = GenericKeywords.outputDirectory + "/" + machineName.replace(" ", "") + "-" + os + "("
				+ osVersion + ")" + "-" + browser + "-" + browserVersion;
		hostName.add(host);
		machineNames.add(machineName);
		testNameSuite.add(browser);
		osName.add(os);
		browserName.add(browser);
		browserVersionName.add(browserVersion);
		osVersionName.add(osVersion);
		File dir = new File(suiteResultsDir);
		dir.mkdir();
		(new File(suiteResultsDir + "/Resultfiles/")).mkdir();
		XMLReport.createXmlReport(suiteResultsDir + "/ResultFiles/" + machineName + ".xml", machineName);
		testCaseResultsDir.add(suiteResultsDir + "/Resultfiles/");
		coverPageResultsDir.add(suiteResultsDir + "/CoverPage.html");
		summaryPageResultsDir.add(suiteResultsDir + "/SummaryPage.html");
		failurePageResultsDir.add(suiteResultsDir + "/FailurePage.html");
		gridPageResultsDir.add(suiteResultsDir + "/GridPage.html");
		cssSuiteDir.add(suiteResultsDir + "/style.css");
		System.out.println(suiteResultsDir + "/ResultFiles/" + machineName + ".xml");
		xmlReport.add(suiteResultsDir + "/ResultFiles/" + machineName + ".xml");
		TransferFiles.transferLogo();
		TransferFiles.transferJS();
		TransferFiles.transferCss();
	}

	public void stopProcess(String process) {

		CommandLine command = new CommandLine("cmd");
		command.addArgument("/c");
		command.addArgument("taskkill");
		command.addArgument("/F");
		command.addArgument("/IM");
		command.addArgument(process);

		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);

		try {
			executor.execute(command, resultHandler);
		} catch (IOException e) {
			writeToLogFile("ERROR", e.toString());
		}

	}

	public boolean verifyTask(String processName) {
		String line;
		int processCount = 0;
		boolean flag = true;

		Process process;
		try {

			process = Runtime.getRuntime().exec(System.getenv("windir") + "/system32/" + "tasklist.exe");
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

			while ((line = input.readLine()) != null) {
				if (line.startsWith(processName)) {
					processCount += 1;
				}
			}
			if (processCount == 0) {
				flag = false;
			}
			input.close();
		} catch (IOException e) {
			writeToLogFile("ERROR", e.toString());
		}
		return flag;

	}

	public void switchApplication(String application) {
		browserType applicationName = browserType.valueOf(application.toUpperCase().trim());
		switch (applicationName) {
		case TENANT:
			driver = tenantSession;
			break;
		case ADMIN:
			driver = adminSession;
			break;
		}
		testStepInfo("Switched to '" + application + "' Application");
	}

	public void startServer(String host, String port) {
		if (verifyTask("node.exe")) {
			stopProcess("node.exe");
		}
		if (verifyTask("chromedriver.exe")) {
			stopProcess("chromedriver.exe");
		}
		if (verifyTask("adb.exe")) {
			// stopProcess("adb.exe");
		}
		String appiumNode = "D:/Data/SolutionsTeam/Official/Appium/node.exe";
		String appiumServer = "D:/Data/SolutionsTeam/Official/Appium/node_modules/appium/bin/appium.js";
		writeToLogFile("INFO", "---- Starting appium server ----");
		CommandLine command = new CommandLine("cmd");
		command.addArgument("/c");
		command.addArgument(appiumNode);
		command.addArgument(appiumServer);
		command.addArgument("--address");
		command.addArgument(host);
		command.addArgument("--port");
		command.addArgument(port);
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);
		try {
			executor.execute(command, resultHandler);
		} catch (IOException e) {
			writeToLogFile("ERROR", e.toString());
		}
	}

	public void openBrowser(String machineName, String host, String port, String browser, String os,
			String browserVersion, String osVersion) {

		platFormName platformName = platFormName.valueOf(os.toUpperCase().trim());
		browserType browserNamee = browserType.valueOf(browser.toUpperCase().trim());
		String gridExecution = getConfigProperty("GridExecution").toLowerCase();
		currentBrowser = browser;

		writeToLogFile("INFO", "Opening " + browser + " Browser...");

		try {

			capabilityMobile.setCapability("newCommandTimeout", getConfigProperty("AppiumTimeOut").toString().trim());

			switch (platformName) {

			case LINUX: /**
						 * Desired capability configurations for LINUX systems
						 */

				ChromeOptions options = new ChromeOptions();
				options.addArguments("test-type");
				capabilityWeb.setCapability(ChromeOptions.CAPABILITY, options);
				capabilityWeb.setBrowserName("chrome");
				if (gridExecution.contains("yes")) {
					driver = new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), capabilityWeb);
				}
				break;

			case IOS: /** Desired capability configurations for IOS systems */

				if (port.trim().toUpperCase().contains("CROSSBROWSERTESTING")) {

					capabilityMobile.setCapability("browser_api_name", "Mbl" + browser + browserVersion);
					capabilityMobile.setCapability("os_api_name",
							machineName.replace(" ", "") + "-" + os + osVersion + "sim");
					capabilityMobile.setCapability("record_video", "true");

				} else if (port.trim().toUpperCase().contains("BROWSERSTACK")) {

					if (machineName.toLowerCase().contains("iphone")) {
						capabilityMobile.setCapability("browserName", "iPhone");

					} else if (machineName.toLowerCase().contains("ipad")) {
						capabilityMobile.setCapability("browserName", "iPad");
					}

					capabilityMobile.setCapability("platform", os.toUpperCase());
					capabilityMobile.setCapability("device", machineName.toLowerCase());

				} else if (port.trim().toUpperCase().contains("SAUCELABS")) {
					capabilityMobile.setBrowserName("iexplore");
					capabilityMobile.setCapability("version", browserVersion);
					if (os.trim().toUpperCase().contains("LINUX")) {
						capabilityMobile.setCapability("platform", os.trim().toUpperCase());
					} else {
						capabilityMobile.setCapability("platform", os.trim().toUpperCase() + " " + osVersion);
					}

				} else {
					capabilityMobile.setCapability(MobileCapabilityType.PLATFORM_NAME, os);
					capabilityMobile.setCapability(MobileCapabilityType.PLATFORM_VERSION, osVersion);
					capabilityMobile.setCapability(MobileCapabilityType.DEVICE_NAME, machineName);
					capabilityMobile.setCapability(MobileCapabilityType.UDID, getConfigProperty("UDID"));
					capabilityMobile.setCapability(MobileCapabilityType.APP, getConfigProperty("Bundle ID"));
					capabilityMobile.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60 * 100);
					capabilityMobile.setCapability("usePrebuiltWDA", true);
				}
				driver = new IOSDriver<>(new URL("http://" + host + ":" + port + "/wd/hub"), capabilityMobile);
				break;

			case ANDROID: /** Desired capability configurations for Android systems */

				if (browserNamee.toString().equalsIgnoreCase("TENANT")) {

					capabilityMobile.setCapability("platformName", os.trim().toUpperCase());
					capabilityMobile.setCapability("platformVersion ", osVersion);
					capabilityMobile.setCapability("deviceName", getConfigProperty("DeviceName"));
					capabilityMobile.setCapability("appPackage", getConfigProperty("AppPackage"));
					capabilityMobile.setCapability("appActivity", getConfigProperty("AppActivity"));
						  

					tenantSession = new AndroidDriver<>(new URL("http://" + host + ":" + port + "/wd/hub"), capabilityMobile);
					tenantSession.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
					writeToLogFile("INFO", "Application: Open Successful : " + currentBrowser.toLowerCase());

				} else if (browserNamee.toString().equalsIgnoreCase("ADMIN")) {
					
					capabilityWeb.setCapability("platform", os.trim().toUpperCase());
					options = new ChromeOptions();
					options.addArguments("test-type");
					options.addArguments("disable-infobars");
					Map<String, Object> chromePrefs = new Hashtable<String, Object>();
					chromePrefs.put("download.default_directory", getConfigProperty("Download directory"));
					chromePrefs.put("download.prompt_for_download", false);
					options.setExperimentalOption("prefs", chromePrefs);
					capabilityWeb.setCapability(ChromeOptions.CAPABILITY, options);
					capabilityWeb.setBrowserName("chrome");

					if (gridExecution.contains("yes")) {
						adminSession = new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), capabilityWeb);
						adminSession.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
						adminSession.manage().window().maximize();
						writeToLogFile("INFO", "Browser: Open Successful : " + currentBrowser.toLowerCase());
					}

				}

				break;

			/* case ANDROID: *//** Desired capability configurations for Android systems */
			/*
			 * 
			 * if (port.trim().toUpperCase().contains("CROSSBROWSERTESTING")) {
			 * capability.setCapability("browser_api_name", "Mbl" + browser +
			 * browserVersion.replace(".0", "")); capability.setCapability("os_api_name",
			 * machineName.replace(" ", "") + "-And" + osVersion.replace(".", ""));
			 * capability.setCapability("record_video", "true");
			 * 
			 * } else if (port.trim().toUpperCase().contains("BROWSERSTACK")) {
			 * 
			 * capability.setCapability("browserName", os.toLowerCase());
			 * capability.setCapability("platform", os.toUpperCase());
			 * capability.setCapability("device", machineName);
			 * 
			 * } else if (port.trim().toUpperCase().contains("SAUCELABS")) {
			 * 
			 * capability.setBrowserName("chrome"); capability.setCapability("version",
			 * browserVersion); if (os.trim().toUpperCase().contains("LINUX")) {
			 * capability.setCapability("platform", os.trim().toUpperCase()); } else {
			 * capability.setCapability("platform", os.trim().toUpperCase() + " " +
			 * osVersion); }
			 * 
			 * } else {
			 * 
			 * capability.setCapability("platformName", os.trim().toUpperCase());
			 * capability.setCapability("platformVersion ", osVersion);
			 * capability.setCapability("deviceName", "Android");
			 * capability.setCapability("udid", udid); //capability.setCapability("noReset",
			 * true);
			 * 
			 * if (getConfigProperty("AppType").equalsIgnoreCase("web")) {
			 * capability.setCapability(CapabilityType.BROWSER_NAME, "chrome");
			 * capability.setCapability("recreateChromeDriverSessions", true);
			 * capability.setCapability("chromedriverExecutable",
			 * getConfigProperty("Chrome Driver Path")); } else {
			 * capability.setCapability("appPackage", getConfigProperty("AppPackage"));
			 * capability.setCapability("appActivity", getConfigProperty("AppActivity"));
			 * //capability.setCapability("app", getConfigProperty("AppPath")); }
			 * 
			 * }
			 * 
			 * driver = new AndroidDriver<>(new URL("http://" + host + ":" + port +
			 * "/wd/hub"), capability); break;
			 * 
			 */ case WINDOWS: /** Desired capability configurations for Windows systems */

				if (port.trim().toUpperCase().contains("CROSSBROWSERTESTING")) {
					if (browserNamee.toString().equalsIgnoreCase("InternetExplorer")) {

						capabilityWeb.setCapability("browser_api_name", "IE" + browserVersion.replace(".0", ""));

					} else if (browserNamee.toString().equalsIgnoreCase("Firefox")) {

						capabilityWeb.setCapability("browser_api_name", "FF" + browserVersion.replace(".0", ""));

					} else {

						capabilityWeb.setCapability("browser_api_name", browser + browserVersion.replace(".0", "").trim());
					}
					capabilityWeb.setCapability("os_api_name", "Win" + osVersion);
					capabilityWeb.setCapability("record_video", "true");

				} else if (port.trim().toUpperCase().contains("BROWSERSTACK")) {
					capabilityWeb.setCapability("os", os.trim().toUpperCase());
					capabilityWeb.setCapability("os_version", osVersion.trim());

					if (browserNamee.toString().equalsIgnoreCase("InternetExplorer")) {
						capabilityWeb.setCapability("browser", "IE");
					} else {
						capabilityWeb.setCapability("browser", browser);
					}

					capabilityWeb.setCapability("browserVersion", browserVersion);

				} else if (port.trim().toUpperCase().contains("SAUCELABS")) {

					capabilityWeb.setBrowserName("chrome");
					capabilityWeb.setCapability("version", browserVersion);
					if (os.trim().toUpperCase().contains("LINUX")) {
						capabilityWeb.setCapability("platform", os.trim().toUpperCase());
					} else {
						capabilityWeb.setCapability("platform", os.trim().toUpperCase() + " " + osVersion);
					}

				} else {

					capabilityWeb.setCapability("platform", os.trim().toUpperCase());

					if (browserNamee.toString().equalsIgnoreCase("CHROME")) {

						options = new ChromeOptions();
						options.addArguments("test-type");
						options.addArguments("disable-infobars");
						Map<String, Object> chromePrefs = new Hashtable<String, Object>();
						chromePrefs.put("download.default_directory", getConfigProperty("Download directory"));
						chromePrefs.put("download.prompt_for_download", false);
						options.setExperimentalOption("prefs", chromePrefs);
						capabilityWeb.setCapability(ChromeOptions.CAPABILITY, options);
						capabilityWeb.setBrowserName("chrome");

					} else if (browserNamee.toString().equalsIgnoreCase("FIREFOX")) {

						capabilityWeb.setBrowserName("firefox");

					} else if (browserNamee.toString().equalsIgnoreCase("InternetExplorer")) {

						capabilityWeb.setBrowserName("internet explorer");
						capabilityWeb.setCapability("nativeEvents", false);
						capabilityWeb.setCapability("ie.ensureCleanSession", true);

					} else if (browserNamee.toString().equalsIgnoreCase("Edge")) {

						capabilityWeb.setBrowserName("MicrosoftEdge");
						capabilityWeb.setPlatform(Platform.WIN10);

					}

				}

				if (gridExecution.contains("yes")) {
					driver = new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), capabilityWeb);
				}

				maximiseWindow();
				break;

			case MAC:

				if (port.trim().toUpperCase().contains("CROSSBROWSERTESTING")) // (targetExecution.trim().equalsIgnoreCase("Browserstack"))
				{
					capabilityWeb.setCapability("browser_api_name", browser + browserVersion.replace(".0", "").trim());
					capabilityWeb.setCapability("os_api_name", "Mac" + osVersion.trim());
					capabilityWeb.setCapability("record_video", "true");

				} else if (port.trim().toUpperCase().contains("BROWSERSTACK")) // (targetExecution.trim().equalsIgnoreCase("Browserstack"))
				{
					capabilityWeb.setCapability("os", "OS X");
					capabilityWeb.setCapability("os_version", osVersion.trim());
					capabilityWeb.setCapability("browser", browser);
					capabilityWeb.setCapability("browser_version", browserVersion);

				} else if (port.trim().toUpperCase().contains("SAUCELABS")) // (targetExecution.trim().equalsIgnoreCase("SauceLabs"))
				{
					capabilityWeb.setBrowserName("chrome");
					capabilityWeb.setCapability("version", browserVersion);
					if (os.trim().toUpperCase().contains("LINUX")) {
						capabilityWeb.setCapability("platform", os.trim().toUpperCase());
					} else {
						capabilityWeb.setCapability("platform", os.trim().toUpperCase() + " " + osVersion);
					}

				} else {

					capabilityWeb.setCapability("platform", os.trim().toUpperCase());

					if (browserNamee.toString().equalsIgnoreCase("CHROME")) {

						options = new ChromeOptions();
						options.addArguments("test-type");
						capabilityWeb.setCapability(ChromeOptions.CAPABILITY, options);
						capabilityWeb.setBrowserName("chrome");

					} else if (browserNamee.toString().equalsIgnoreCase("SAFARI")) {

						capabilityWeb.setBrowserName("safari");

					}

				}
				if (gridExecution.contains("yes")) {
					driver = new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), capabilityWeb);
				}
				break;
			}

//			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
//			writeToLogFile("INFO", "Browser: Open Successful : " + currentBrowser.toLowerCase());

			if ((platformName.toString().equalsIgnoreCase("android"))
					&& (getConfigProperty("AppType").equalsIgnoreCase("web"))) {

				navigateTo(getConfigProperty("AppURL"));

			} else if (!(platformName.toString().equalsIgnoreCase("android"))) {

				if (os.toUpperCase().trim().contains("IOS")) {
					for (String contextName : ((AppiumDriver<?>) driver).getContextHandles()) {

						writeToLogFile("INFO", "CURRENT VIEW" + contextName);
						if (contextName.contains("WEBVIEW")) {
							((AppiumDriver<?>) driver).context(contextName);
						}
						if (contextName.contains("SAFARI")) {
							((AppiumDriver<?>) driver).context(contextName);
						}
					}
				}

				testStepPassed("Open Browser : " + currentBrowser.toLowerCase());
				navigateTo(getConfigProperty("AppURL"));
			}

		} catch (

		TimeoutException e) {

			testStepFailed("Page fail to load within in " + getConfigProperty("pageLoadWaitTime") + " seconds",
					e.toString());

		} catch (Exception e) {

			writeToLogFile("ERROR",
					"Browser: Open Failure/Navigation cancelled, please check the application window. Error: "
							+ e.toString());
			testStepFailed("Browser: Open Failure/Navigation cancelled, please check the application window.",
					e.toString());

		}
	}

	public void navigateTo(String url) {
		try {
			writeToLogFile("INFO", "Navigating to URL : " + url);
			driver.get(url);
			writeToLogFile("INFO", "Navigation Successful : " + url);
			testStepPassed("Navigate to : " + url);
		} catch (TimeoutException e) {
			testStepFailed("Page fail to load within in " + pageLoadWaitTime + " seconds", e.toString());
		} catch (Exception e) {
			writeToLogFile("ERROR", "Browser: Open Failure/Navigation cancelled, please check the application window.");
			testStepFailed(
					"Browser: Open Failure/Navigation cancelled, please check the application window. URL: " + url,
					e.toString());
		}

	}

	public void closeBrowser() {
		try {
			writeToLogFile("INFO", "Closing Browser...");
			if (currentBrowser.contains("chrome")) {
				deleteAllCookies();
				driver.quit();
			} else {
				deleteAllCookies();
				driver.close();
			}
			testStepPassed("Browser closed");
		} catch (Exception e) {
			writeToLogFile("ERROR", "Browser: Close Failure");
			testStepFailed("Browser close failure", e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	private WebElement identifyBy(String identifier) {
		WebElement webElement = null;
		identifierType i = identifierType.valueOf(identifier);
		switch (i) {
		case xpath:
			webElement = driver.findElement(By.xpath(locator));
			break;
		case id:
			webElement = driver.findElement(By.id(locator));
			break;
		case name:
			webElement = driver.findElement(By.name(locator));
			break;
		case lnktext:
			webElement = driver.findElement(By.linkText(locator));
			break;
		case partiallinktext:
			webElement = driver.findElement(By.partialLinkText(locator));
			break;
		case classname:
			webElement = driver.findElement(By.className(locator));
			break;
		case cssselector:
			webElement = driver.findElement(By.cssSelector(locator));
			break;
		case tagname:
			webElement = driver.findElement(By.tagName(locator));
			break;
		case androiduiautomator:
			webElement = ((AndroidDriver<WebElement>) driver).findElementByAndroidUIAutomator(locator);
			break;
		default:
			writeToLogFile("Error", "Element not found '" + locator + "'");
		}
		return webElement;
	}

	private WebElement findWebElementFrom(WebElement driverElement) {
		WebElement webElement = null;
		identifierType i = identifierType.valueOf(identifier);
		switch (i) {
		case xpath:
			webElement = driverElement.findElement(By.xpath("." + locator));
			break;
		case id:
			webElement = driverElement.findElement(By.id(locator));
			break;
		case name:
			webElement = driverElement.findElement(By.name(locator));
			break;
		case lnktext:
			webElement = driverElement.findElement(By.linkText(locator));
			break;
		case partiallinktext:
			webElement = driverElement.findElement(By.partialLinkText(locator));
			break;
		case classname:
			webElement = driverElement.findElement(By.className(locator));
			break;
		case cssselector:
			webElement = driverElement.findElement(By.cssSelector(locator));
			break;
		case tagname:
			webElement = driverElement.findElement(By.tagName(locator));
			break;
		default:
			writeToLogFile("Error", "Element not found '" + locator + "'");
		}
		return webElement;
	}

	private List<WebElement> findWebElementsFrom(WebElement driverElement) {
		List<WebElement> webElements = new ArrayList<WebElement>();
		identifierType i = identifierType.valueOf(identifier);
		switch (i) {
		case xpath:
			webElements = driverElement.findElements(By.xpath("." + locator));
			break;
		case id:
			webElements = driverElement.findElements(By.id(locator));
			break;
		case name:
			webElements = driverElement.findElements(By.name(locator));
			break;
		case lnktext:
			webElements = driverElement.findElements(By.linkText(locator));
			break;
		case partiallinktext:
			webElements = driverElement.findElements(By.partialLinkText(locator));
			break;
		case classname:
			webElements = driverElement.findElements(By.className(locator));
			break;
		case cssselector:
			webElements = driverElement.findElements(By.cssSelector(locator));
			break;
		case tagname:
			webElements = driverElement.findElements(By.tagName(locator));
			break;
		default:
			writeToLogFile("Error", "Element not found '" + locator + "'");
		}
		return webElements;
	}

	private List<WebElement> findWebElements() {
		List<WebElement> webElements = new ArrayList<WebElement>();
		identifierType i = identifierType.valueOf(identifier);
		switch (i) {
		case xpath:
			webElements = driver.findElements(By.xpath(locator));
			break;
		case id:
			webElements = driver.findElements(By.id(locator));
			break;
		case name:
			webElements = driver.findElements(By.name(locator));
			break;
		case lnktext:
			webElements = driver.findElements(By.linkText(locator));
			break;
		case partiallinktext:
			webElements = driver.findElements(By.partialLinkText(locator));
			break;
		case classname:
			webElements = driver.findElements(By.className(locator));
			break;
		case cssselector:
			webElements = driver.findElements(By.cssSelector(locator));
			break;
		case tagname:
			webElements = driver.findElements(By.tagName(locator));
			break;
		default:
			writeToLogFile("Error", "Element not found '" + locator + "'");
		}
		return webElements;
	}

	public void waitTimeForException(int sec) {
		try {
			Thread.sleep(sec * 1000);
		} catch (Exception e) {
			testStepFailed("", e.toString());
		}
	}

	public void waitForElement(WebElement driverElement, String objectLocator, int timeout) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= timeout; i++) {

				try {
					if (driverElement == null) {
						findWebElement(objectLocator);
					} else {
						findWebElementFrom(driverElement, objectLocator);
					}
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed("Invalid Selector Exception occured for locator [" + locator
							+ "]. Please make sure locator syntax is correct. Error:" + e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {

					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("",
							locatorDescription + " element found but its not in editable/clickable state within "
									+ elementLoadWaitTime + " timeouts");
				}
			}
		} catch (Exception e) {
			testStepFailed("Exception Error '" + e.toString() + "'");
		} finally {
			// Re-setting the implicit wait is set for the life of the WebDriver
			// object instance
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public void waitForElement(String objectLocator, int timeout) {
		waitForElement(null, objectLocator, timeout);
	}

	public void waitForElement(String objectLocator) {
		waitForElement(null, objectLocator, elementLoadWaitTime);
	}

	public void waitForElement(WebElement driverElement, String objectLocator) {
		waitForElement(driverElement, objectLocator, elementLoadWaitTime);
	}

	public WebElement findWebElement(String objectLocator) {
		parseidentifyByAndlocator(objectLocator);
		return identifyBy(identifier);
	}

	public WebElement findWebElementFrom(WebElement driverElement, String objectLocator) {
		parseidentifyByAndlocator(objectLocator);
		return findWebElementFrom(driverElement);
	}

	public List<WebElement> findWebElementsFrom(WebElement driverElement, String objectLocator) {
		parseidentifyByAndlocator(objectLocator);
		return findWebElementsFrom(driverElement);
	}

	public List<WebElement> findWebElements(String objectLocator) {
		parseidentifyByAndlocator(objectLocator);
		return findWebElements();
	}

	public void verifyElementText(WebElement driverElement, String objectLocator, String expectedText) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					if (driverElement == null) {
						webElement = findWebElement(objectLocator);
					} else {
						webElement = findWebElementFrom(driverElement, objectLocator);
					}
					String actualText = webElement.getText();
					if (actualText.trim().equalsIgnoreCase(expectedText.trim())) {
						testStepPassed("Verification: " + locatorDescription + " element contains text '" + expectedText
								+ "'");
					} else {
						testStepFailed("Verification: " + locatorDescription + " element does not contains text '"
								+ expectedText + "'," + "instead following text is displayed '" + actualText + "' ");
					}
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed(
							"Invalid Selector Exception occured. Please make sure locator syntax is correct. Error:"
									+ e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed(locatorDescription + " element not within " + elementLoadWaitTime + " timeouts", "");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public void verifyElementText(String objectLocator, String expectedText) {
		verifyElementText(null, objectLocator, expectedText);
	}

	public void typeIn(String objectLocator, String inputValue) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {

					webElement = findWebElement(objectLocator);
					webElement.click();
					webElement.clear();
					webElement.clear();
					webElement.clear();

					
					webElement.sendKeys(inputValue);
					testStepPassed("Type '" + inputValue + "' in: " + locatorDescription);
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed("Invalid Selector Exception occured for locator [" + locator
							+ "]. Please make sure locator syntax is correct. Error:" + e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("",
							locatorDescription + " element not found within " + elementLoadWaitTime + " timeouts");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	@Deprecated
	public void sendkeys(String Locator, String inputValue) {

		try {
			driver.findElement(By.xpath(Locator)).clear();
			driver.findElement(By.xpath(Locator)).sendKeys(inputValue);
			testStepPassed("Type '" + inputValue + "' in : " + locatorDescription);
		} catch (Exception e) {
			writeToLogFile("ERROR", "Typing '" + inputValue + "' in : " + locatorDescription);
			testStepFailed("Element is not in editable state '" + locatorDescription, e.toString());
		}

	}

	public void refreshPage() {

		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					driver.navigate().refresh();
					writeToLogFile("INFO", "Sucessfully Refreshed browser");
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed("Invalid Selector Exception occured for locator [" + locator
							+ "]. Please make sure locator syntax is correct. Error:" + e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("Error on refreshing browser", "");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public void clickOnBackButton() {

		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					driver.navigate().back();
					writeToLogFile("INFO", "Sucessfully moved to 'Back' page");
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed("Invalid Selector Exception occured for locator [" + locator
							+ "]. Please make sure locator syntax is correct. Error:" + e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("Error on moving to 'Back' page", "");
				}

			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public void clickOnForwardButton() {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					driver.navigate().forward();
					writeToLogFile("INFO", "Sucessfully moved to 'Next' page");
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed("Invalid Selector Exception occured for locator [" + locator
							+ "]. Please make sure locator syntax is correct. Error:" + e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("Error on moving to 'Next' page", "");
				}

			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}

	}

	public void alertOk() {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					Alert alert = driver.switchTo().alert();
					alert.accept();
					testStepPassed("Click on Alert OK button");
					break;
				} catch (NoAlertPresentException e) {
					writeToLogFile("Info", "NoAlertPresentException occured. Retrying..............");
					waitTimeForException(1);
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("Click on Alert OK button failed", "");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}

	}

	public void alertCancel() {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					Alert alert = driver.switchTo().alert();
					alert.dismiss();
					testStepPassed("Click on Alert Cancel button");
					break;
				} catch (NoAlertPresentException e) {
					writeToLogFile("Info", "NoAlertPresentException occured. Retrying..............");
					waitTimeForException(1);
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("Click on Alert Cancel button failed", "");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public String getAlertText() {
		String alertText = null;
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					Alert alert = driver.switchTo().alert();
					alertText = alert.getText();
					writeToLogFile("INFO", "Alert text [" + alertText + "] is retrieved successfully ");
					break;
				} catch (NoAlertPresentException e) {
					writeToLogFile("Info", "NoAlertPresentException occured. Retrying..............");
					waitTimeForException(1);
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("Retrieveing Alert text failed", "");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
		return alertText;
	}

	public boolean isAlertWindowPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (Exception E) {
			return false;
		}
	}

	public void verifyElementIsDisplayed(String objLocator) {

		waitForElement(objLocator, elementLoadWaitTime);
		if (isElementDisplayed(objLocator)) {
			testStepPassed(locatorDescription + " is displayed");
		} else {
			testStepFailed(locatorDescription + " is not displayed");
		}
	}

	public void verifyElement(String objLocator) {

		waitForElementToDisplay(objLocator, elementLoadWaitTime);
		for (int i = 1; i <= elementLoadWaitTime; i++) {
			try {
				testStepPassed("Verify Element : " + locatorDescription);
				break;
			} catch (InvalidSelectorException e) {
				writeToLogFile("Info", "InvalidSelectorException occured. Retrying..............");
				waitTimeForException(1);
			} catch (StaleElementReferenceException e) {
				writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
				waitTimeForException(1);
			} catch (NoSuchElementException e) {

				writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
				waitTimeForException(1);
			} catch (ElementNotVisibleException e) {
				writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
				waitTimeForException(1);
			} catch (UnreachableBrowserException e) {
				testStepFailed("", e.toString());
			} catch (WebDriverException e) {
				writeToLogFile("Info", "WebDriverException occured. Retrying..............");
				waitTimeForException(1);
			} catch (Exception e) {
				testStepFailed("", "Exception Error '" + e.toString() + "'");
			}
			if (i == elementLoadWaitTime) {
				testStepFailed("Verify Element : " + locatorDescription, "");
			}

		}

	}

	public void mouseOver(String objLocator) {

		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					Actions builder = new Actions(driver);
					webElement = findWebElement(objLocator);
					builder.moveToElement(webElement).build().perform();
					testStepPassed("Move the mouse over '" + locatorDescription + "'");
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed("Invalid Selector Exception occured for locator [" + locator
							+ "]. Please make sure locator syntax is correct. Error:" + e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("Mouse over action on '" + locatorDescription + "' element failed", "");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public void mouseOver(WebElement driverElement, String objLocator) {

		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					Actions builder = new Actions(driver);
					webElement = findWebElementFrom(driverElement, objLocator);
					builder.moveToElement(webElement).build().perform();
					testStepPassed("Move the mouse over '" + locatorDescription + "'");
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed("Invalid Selector Exception occured for locator [" + locator
							+ "]. Please make sure locator syntax is correct. Error:" + e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("Mouse over action on '" + locatorDescription + "' element failed", "");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public void waitTime(long waittime) {
		writeToLogFile("INFO", "Waiting for " + waittime + " seconds...");
		try {
			Thread.sleep(waittime * 1000);
		} catch (InterruptedException e) {
			writeToLogFile("ERROR", "Thread.sleep operation failed, during waitTime function call");
		}
	}

	public void selectFromDropdown(String objLocator, String valueToSelect) {

		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					webElement = findWebElement(objLocator);
					Select select = new Select(webElement);
					select.selectByVisibleText(valueToSelect);
					testStepPassed("Select '" + valueToSelect + "' from : " + locatorDescription);
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed("Invalid Selector Exception occured for locator [" + locator
							+ "]. Please make sure locator syntax is correct. Error:" + e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("Could not select '" + valueToSelect + "' from " + locatorDescription
							+ "dropdown within " + elementLoadWaitTime + " seconds", "");
				}
			}
		} finally {
			// Re-setting the implicit wait is set for the life of the WebDriver
			// object instance
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public void selectFromDropdown(String objLocator, int indexNumber) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					webElement = findWebElement(objLocator);
					Select select = new Select(webElement);
					select.selectByIndex(indexNumber);
					testStepPassed(
							"Select '" + indexNumber + "' (index)option from " + locatorDescription + "dropdown");
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed("Invalid Selector Exception occured for locator [" + locator
							+ "]. Please make sure locator syntax is correct. Error:" + e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("Could not select '" + indexNumber + "' (index)option from " + locatorDescription
							+ "dropdown within " + elementLoadWaitTime + " seconds", "");
				}
			}
		} finally {
			// Re-setting the implicit wait is set for the life of the WebDriver
			// object instance
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public String getTextSelectedOption(String objLocator) {

		String selectedText = null;
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					webElement = findWebElement(objLocator);
					Select select = new Select(webElement);
					selectedText = select.getFirstSelectedOption().getText().toString();
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed("Invalid Selector Exception occured for locator [" + locator
							+ "]. Please make sure locator syntax is correct. Error:" + e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("Could not retrieve selected option from " + locatorDescription + "dropdown within "
							+ elementLoadWaitTime + " seconds", "");
				}
			}
		} finally {
			// Re-setting the implicit wait is set for the life of the WebDriver
			// object instance
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
		return selectedText;
	}

	public void verifyPageTitle(String partialTitle) {

		String actualPageTitle = getPageTitle();
		if (actualPageTitle.contains(partialTitle)) {
			testStepPassed("Page title[" + partialTitle + "] is displayed as expected");
		} else {
			testStepFailed("Page title[" + partialTitle + "] is not displayed as expected, instead we get title as ["
					+ actualPageTitle + "]");
		}

	}

	public String getPageTitle() {
		String pageTitle = null;
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					pageTitle = driver.getTitle();
					break;
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("Unable to get the page title within " + elementLoadWaitTime + " seconds");
				}
			}
		} finally {
			// Re-setting the implicit wait is set for the life of the WebDriver
			// object instance
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
		return pageTitle;
	}

	public String getAttributeValue(String objLocator, String attributeName) {

		String getAttributeValue = null;
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					webElement = findWebElement(objLocator);
					getAttributeValue = webElement.getAttribute(attributeName);
					writeToLogFile("Info", "Sucessfully got the attribute value '" + getAttributeValue + "' for '"
							+ locatorDescription + "' element");
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed("Invalid Selector Exception occured for locator [" + locator
							+ "]. Please make sure locator syntax is correct. Error:" + e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("Unable to retrieve value for the attribute[" + attributeName + "] from "
							+ locatorDescription + " element within " + elementLoadWaitTime + " seconds", "");

				}
			}
		} finally {
			// Re-setting the implicit wait is set for the life of the WebDriver
			// object instance
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
		return getAttributeValue;
	}

	public void verifyAttribute(String objLocator, String attributeName, String expectedAttributeValue) {
		String actualAttributeValue = getAttributeValue(objLocator, attributeName);
		if (actualAttributeValue.equalsIgnoreCase(expectedAttributeValue)) {
			testStepPassed("Attribute Value[" + expectedAttributeValue + "] for the Attribute name[" + attributeName
					+ "] is equal as expected for the element " + locatorDescription + "");
		} else {
			testStepFailed("Attribute Value[" + expectedAttributeValue + "] for the Attribute name[" + attributeName
					+ "] is equal as expected for the element " + locatorDescription
					+ ", instead we get the following attribute value [" + actualAttributeValue + "]");
		}
	}

	public void clickAndHold(String objLocator) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					webElement = findWebElement(objLocator);
					Actions builder = new Actions(driver);
					builder.clickAndHold(webElement).build().perform();
					testStepPassed("Performing click and hold action on '" + locatorDescription + "' element");
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed("Invalid Selector Exception occured for locator [" + locator
							+ "]. Please make sure locator syntax is correct. Error:" + e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("Performing click and hold action on '" + locatorDescription
							+ "' element failed within " + elementLoadWaitTime + " seconds timeout", "");
				}
			}
		} finally {
			// Re-setting the implicit wait is set for the life of the WebDriver
			// object instance
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public void clickOn(String objLocator) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					webElement = findWebElement(objLocator);
					if (currentExecutionMachineName.toLowerCase().contains("iphone")
							|| currentExecutionMachineName.toLowerCase().contains("ipad")) {
						Actions actions = new Actions(driver);
						actions.moveToElement(webElement);
						actions.click().build().perform();
						testStepPassed("Click on: " + locatorDescription);
					} else {
						webElement.click();
						testStepPassed("Click on: " + locatorDescription);
					}
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed(
							"Invalid Selector Exception occured. Please make sure locator syntax is correct. Error:"
									+ e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}

				if (i == elementLoadWaitTime) {
					testStepFailed(locatorDescription + " element found but its not in editable/clickable state within "
							+ elementLoadWaitTime + " timeouts", "");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public void clickOnSpecialElement(String objectLocator) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					webElement = findWebElement(objectLocator);
					JavascriptExecutor executor = (JavascriptExecutor) driver;
					executor.executeScript("arguments[0].click();", webElement);
					testStepPassed("Click on: " + locatorDescription);
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed(
							"Invalid Selector Exception occured. Please make sure locator syntax is correct. Error:"
									+ e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}

				if (i == elementLoadWaitTime) {
					testStepFailed(locatorDescription + " element found but its not in editable/clickable state within "
							+ elementLoadWaitTime + " timeouts", "");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public void switchToWindow(String pageTitle) {
		try {
			Set<String> windowHandles = driver.getWindowHandles();
			for (String windowHandle : windowHandles) {
				driver.switchTo().window(windowHandle);
				waitTimeForException(2);
				String getTitle = getPageTitle();
				if (getTitle.contains(pageTitle)) {
					testStepPassed("Switched to window containing title:" + pageTitle);
					break;
				}
			}
		} catch (Exception e) {
			testStepFailed("Switch to window :" + pageTitle, "");
		}
		return;
	}

	public void switchToDefaultFrame() {
		try {
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			testStepFailed("Exception Error '" + e.toString() + "'");
		}
	}

	public void switchToFrame(String objectLocator) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					webElement = findWebElement(objectLocator);
					driver.switchTo().frame(webElement);
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed(
							"Invalid Selector Exception occured. Please make sure locator syntax is correct. Error:"
									+ e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}

				if (i == elementLoadWaitTime) {
					testStepFailed("Switching to frame " + locatorDescription + " did not happen within "
							+ elementLoadWaitTime + " seconds", "");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public void dragAndDrop(String sourceObjLocator, String destinationObjLocator) {
		String sourceDesc = null, destinationDesc = null;
		WebElement sourceElement = null;
		WebElement destinationElement = null;
		try {
			sourceElement = findWebElement(sourceObjLocator);
			sourceDesc = locatorDescription;
			destinationElement = findWebElement(destinationObjLocator);
			destinationDesc = locatorDescription;
			(new Actions(driver)).dragAndDrop(sourceElement, destinationElement).perform();
			testStepPassed("Drag '" + sourceDesc + "' and drop on '" + destinationDesc + "'");
		} catch (Exception e) {
			testStepFailed("Drag '" + sourceDesc + "' and drop on '" + destinationDesc + "' failed");
		}
	}

	public void clearEditBox(String objLocator) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {

					webElement = findWebElement(objLocator);
					webElement.click();
					webElement.clear();
					writeToLogFile("INFO", locatorDescription + " text field is cleared");
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed("Invalid Selector Exception occured for locator [" + locator
							+ "]. Please make sure locator syntax is correct. Error:" + e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("",
							locatorDescription + " element not found within " + elementLoadWaitTime + " timeouts");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public void rightClick(String objLocator) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					webElement = findWebElement(objLocator);
					Actions builder = new Actions(driver);
					builder.contextClick(webElement).build().perform();
					testStepPassed("Right Click on '" + locatorDescription + "'");
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed("Invalid Selector Exception occured for locator [" + locator
							+ "]. Please make sure locator syntax is correct. Error:" + e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed(locatorDescription + " element found but its not in editable/clickable state within "
							+ elementLoadWaitTime + " timeouts", "");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public void doubleClick(String objLocator) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					webElement = findWebElement(objLocator);
					Actions builder = new Actions(driver);
					builder.doubleClick(webElement).build().perform();
					testStepPassed("Double Clicked on '" + locatorDescription + "'");
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed("Invalid Selector Exception occured for locator [" + locator
							+ "]. Please make sure locator syntax is correct. Error:" + e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed(locatorDescription + " element found but its not in editable/clickable state within "
							+ elementLoadWaitTime + " timeouts", "");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public boolean elementPresent(String objectLocator) {
		try {
			findWebElement(objectLocator);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		} catch (Exception e) {
			testStepFailed("Exception Error '" + e.toString() + "'");
			return false;
		}

	}

	public String getPageSource() {
		String pageSource = null;
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					pageSource = driver.getPageSource();
					break;
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed("Unable to get the page source within " + elementLoadWaitTime + " seconds");
				}
			}
		} finally {
			// Re-setting the implicit wait is set for the life of the WebDriver
			// object instance
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
		return pageSource;
	}

	public void verifyPageShouldContainText(String expectedText) {

		String pageSource = getPageSource();
		if (pageSource.contains(expectedText)) {
			testStepPassed("Verification: '" + expectedText + "' text is present in the page source");
		} else {
			testStepFailed("Verification: '" + expectedText + "' text is not present in the page source");
		}
	}

	public void verifyPageShouldNotContainText(String expectedText) {

		String pageSource = getPageSource();
		if (pageSource.contains(expectedText)) {
			testStepFailed("Verification: '" + expectedText + "' text is present in the page source");
		} else {
			testStepPassed("Verification: '" + expectedText + "' text is not present in the page source");
		}

	}

	public void verifyAlertTextShouldContain(String expectedAlertText) {

		String actualAlertText = getAlertText();
		writeToLogFile("INFO", "Alert Message: " + actualAlertText);
		if (actualAlertText.contains(expectedAlertText)) {
			testStepPassed("Verification: '" + expectedAlertText + "' text is displayed in alert window");
		} else {
			testStepFailed("Verification: '" + expectedAlertText
					+ "' text is not displayed in alert window, instead we get following alert text '" + actualAlertText
					+ "'");
		}

	}

	public void verifyTextFieldShouldContain(String objectLocator, String expectedText, String attributeName) {
		String actualText = getAttributeValue(objectLocator, attributeName);
		if (actualText.toLowerCase().contains(expectedText.toLowerCase())) {
			testStepPassed(
					"Verification: [" + expectedText + "] text is present in " + locatorDescription + " textfield");
		} else {
			testStepFailed("Verification: [" + expectedText + "] text is present in " + locatorDescription
					+ " textfield, instead we get following text [" + actualText + "]");
		}
	}

	public void verifyTextInTextField(String objectLocator, String expectedText) {
		String actualText = getText(objectLocator);
		if (actualText.equalsIgnoreCase(expectedText)) {
			testStepPassed(
					"Verification: [" + expectedText + "] text is present in " + locatorDescription + " textfield");
		} else {
			testStepFailed("Verification: [" + expectedText + "] text is present in " + locatorDescription
					+ " textfield, instead we get following text [" + actualText + "]");
		}
	}

	public String getText(String objLocator) {

		String textContents = null;
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					webElement = findWebElement(objLocator);
					textContents = webElement.getText();
					writeToLogFile("Info", "Sucessfully got the text '" + textContents + "' for " + locatorDescription);
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed("Invalid Selector Exception occured for locator [" + locator
							+ "]. Please make sure locator syntax is correct. Error:" + e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Retriving the text for " + locatorDescription + " failed. Exception Error: '"
							+ e.toString() + "'");
					break;
				}

				if (i == elementLoadWaitTime) {
					testStepFailed(locatorDescription + " element not found or its not in editable state within "
							+ elementLoadWaitTime + " timeouts");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
		return textContents;
	}

	public void deleteAllCookies() {
		try {
			driver.manage().deleteAllCookies();
			writeToLogFile("INFO", "Successfully deleted all cookies");
		} catch (Exception e) {
			windowreadyStateStatus = false;
			testStepFailed("Exception on deleting All cookies. Error: " + e.toString());
		}
	}

	public void maximiseWindow() {

		try {
			driver.manage().window().maximize();
			writeToLogFile("INFO", "Successfully Maximised Browser Window");
		} catch (Exception e) {
			windowreadyStateStatus = false;
			testStepFailed("Exception on maximize browser. Error: " + e.toString());
		}

	}

	public void closeAllBrowser() {
		deleteAllCookies();
		try {
			Set<String> windowhandles = driver.getWindowHandles();
			for (String handle : windowhandles) {
				driver.switchTo().window(handle);
				driver.close();
			}
		} catch (Exception e) {
			windowreadyStateStatus = false;
			testStepFailed("Exception on close all browser. Error: " + e.toString());
		}

	}

	public void closeChildBrowser(String windowTitle) {
		switchToWindow(windowTitle);
		try {
			driver.close();
			testStepPassed("Browser with [" + windowTitle + "] window title closed");
		} catch (Exception e) {
			testStepFailed("Exception on close child browser. Error: " + e.toString());
		}

	}

	public void waitForAlertWindow(int timeout) {
		for (int i = 0; i <= timeout; i++) {
			if (isAlertWindowPresent()) {
				break;
			} else {
				waitTime(1);
			}
			if (i == timeout) {
				testStepFailed("Alert Window is not present within '" + timeout + "' timeout", "");
			}
		}
	}

	public void waitForAlertWindow() {
		waitForAlertWindow(elementLoadWaitTime);
	}

	public void waitForChildWindow(String windowTitle, int timeout) {
		for (int i = 1; i <= timeout; i++) {
			boolean windowStatus = false;
			Set<String> AllHandle = driver.getWindowHandles();
			for (String han : AllHandle) {
				driver.switchTo().window(han);
				String getTitle = driver.getTitle();
				if (getTitle.trim().equalsIgnoreCase(windowTitle)) {
					testStepPassed("Child window with title[" + windowTitle + "] is displayed");
					windowStatus = true;
					break; // Inner for loop
				}
			}
			if (windowStatus) {
				break; // Outer for loop
			} else {
				waitTime(1);
			}
			if (i == timeout) {
				testStepFailed(
						"Child window with title[" + windowTitle + "] is not present within '" + timeout + "' timeout");
			}
		}

	}

	public void waitForChildWindow(String windowTitle) {
		waitForChildWindow(windowTitle, elementLoadWaitTime);
	}

	public void waitForElementToDisplay(WebElement driverElement, String objectLocator, int timeout) {

		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= timeout; i++) {
				try {
					if (isElementDisplayed(driverElement, objectLocator)) {
						break;
					} else {
						waitTime(1);
					}
				} catch (InvalidSelectorException e) {
					testStepFailed("Invalid Selector Exception occured for locator [" + locator
							+ "]. Please make sure locator syntax is correct. Error:" + e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {

					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}
				if (i == elementLoadWaitTime) {
					testStepFailed(locatorDescription + " element is not displayed within " + elementLoadWaitTime
							+ " timeouts");
				}
			}
		} catch (Exception e) {
			testStepFailed("Exception Error '" + e.toString() + "'");
		} finally {
			// Re-setting the implicit wait is set for the life of the WebDriver
			// object instance
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public void waitForElementToDisplay(String objectLocator, int timeout) {
		waitForElementToDisplay(null, objectLocator, timeout);
	}

	public boolean isElementDisplayed(WebElement driverElement, String objectLocator) {
		if (driverElement == null) {
			webElement = findWebElement(objectLocator);
		} else {
			webElement = findWebElementFrom(driverElement, objectLocator);
		}
		if (webElement.isDisplayed()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isElementDisplayed(String objectLocator) {
		return isElementDisplayed(null, objectLocator);
	}
	
	public void selectCheckBox(String objLocator) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					webElement = findWebElement(objLocator);
					if (!webElement.isSelected()) {
						webElement.click();
					}
					testStepPassed("Checked on the " + locatorDescription + " checkbox");
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed(
							"Invalid Selector Exception occured. Please make sure locator syntax is correct. Error:"
									+ e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}

				if (i == elementLoadWaitTime) {
					testStepFailed(locatorDescription + " element found but its not in editable/clickable state within "
							+ elementLoadWaitTime + " timeouts", "");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public void unSelectCheckBox(String objLocator) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					webElement = findWebElement(objLocator);
					if (webElement.isSelected()) {
						webElement.click();
					}
					testStepPassed("UnChecked on the " + locatorDescription + " checkbox");
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed(
							"Invalid Selector Exception occured. Please make sure locator syntax is correct. Error:"
									+ e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}

				if (i == elementLoadWaitTime) {
					testStepFailed(locatorDescription + " element found but its not in editable/clickable state within "
							+ elementLoadWaitTime + " timeouts", "");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
	}

	public boolean isCheckBoxSelected(String objLocator) {
		boolean status = false;
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			for (int i = 1; i <= elementLoadWaitTime; i++) {
				try {
					webElement = findWebElement(objLocator);
					if (webElement.isSelected()) {
						status = true;
					} else {
						status = false;
					}
					break;
				} catch (InvalidSelectorException e) {
					testStepFailed(
							"Invalid Selector Exception occured. Please make sure locator syntax is correct. Error:"
									+ e.getMessage());
					break;
				} catch (StaleElementReferenceException e) {
					writeToLogFile("Info", "StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {
					writeToLogFile("Info", "NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					writeToLogFile("Info", "ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					testStepFailed("", e.toString());
				} catch (WebDriverException e) {
					writeToLogFile("Info", "WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					testStepFailed("Exception Error '" + e.toString() + "'");
					break;
				}

				if (i == elementLoadWaitTime) {
					testStepFailed(locatorDescription + " element found but its not in editable/clickable state within "
							+ elementLoadWaitTime + " timeouts", "");
				}
			}
		} finally {
			driver.manage().timeouts().implicitlyWait(implicitlyWaitTime, TimeUnit.SECONDS);
		}
		return status;
	}

	public void verifyCheckBoxIsChecked(String objLocator) {

		if (isCheckBoxSelected(objLocator)) {
			testStepPassed("Verification: " + locatorDescription + " checkbox is checked");
		} else {
			testStepFailed("Verification: " + locatorDescription + " checkbox is not checked");
		}
	}

	public void verifyCheckBoxIsUnChecked(String objLocator) {
		if (isCheckBoxSelected(objLocator)) {
			testStepFailed("Verification: " + locatorDescription + " checkbox is checked");
		} else {
			testStepPassed("Verification: " + locatorDescription + " checkbox is not checked");
		}
	}

	public void parseidentifyByAndlocator(String identifyByAndLocator) {

		writeToLogFile("INFO", "Parsing Locator: " + identifyByAndLocator);
		try {
			locatorDescription = identifyByAndLocator.substring(0, identifyByAndLocator.indexOf("#"));
			identifyByAndLocator = identifyByAndLocator.substring(identifyByAndLocator.indexOf("#") + 1);
		} catch (Exception e) {
			locatorDescription = "";
		} finally {
			identifier = identifyByAndLocator.substring(0, identifyByAndLocator.indexOf("=", 0)).toLowerCase();
			locator = identifyByAndLocator.substring(identifyByAndLocator.indexOf("=", 0) + 1);
			writeToLogFile("INFO", currentExecutionMachineName + " - Locator Description : " + locatorDescription);
			writeToLogFile("INFO", currentExecutionMachineName + " - Identify Type: " + identifier);
			writeToLogFile("INFO", currentExecutionMachineName + " - Locator: " + locator);
			GenericKeywords.idType = identifierType.valueOf(identifier);
		}
	}

	public void setTimeouts() {
		testCaseExecutionStatus = false;
		elementLoadWaitTime = Integer.parseInt(getConfigProperty("ElementLoadWaitTime").toString().trim());
		textLoadWaitTime = Integer.parseInt(getConfigProperty("TextLoadWaitTime").toString().trim());
		pageLoadWaitTime = Integer.parseInt(getConfigProperty("PageLoadWaitTime").toString().trim());
		implicitlyWaitTime = Integer.parseInt(getConfigProperty("ImplicitlyWaitTime").toString().trim());
		writeToLogFile("INFO", "Element time out set");
	}

	public void captureScreenShot(String filename) {
		File scrFile = null;
		String scrPath = GenericKeywords.outputDirectory + "/Screenshots/";
		File file = new File(scrPath);
		file.mkdir();
		if (driver.getClass().getName().equals("org.openqa.selenium.remote.RemoteWebDriver")) {
			driver = (RemoteWebDriver) new Augmenter().augment(driver);
		}
		try {
			scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(scrPath + filename + ".png"));
		} catch (Exception e) {
			testStepFailed("", "Capture Screenshot method failed. Error:" + e.toString());
		} finally {
			if (scrFile == null) {
				writeToLogFile("INFO", "This WebDriver does not support screenshots");
			}
		}
	}

	public void testStepFailed(String userMessage, String execeptionMesage) {

		testFailure = true;
		GenericKeywords.failureNo++;

		if (!(userMessage.equalsIgnoreCase(""))) {
			totValidFailures++;
			failureTcs.add(currentTestCaseName);
			writeToLogFile("Error", userMessage);
		}

		if (!(execeptionMesage.equalsIgnoreCase(""))) {
			totExceptionFailures++;
			failureTcs.add(currentTestCaseName);
			System.out.println("^^^^^^^^^^^^^^" + currentTestCaseName);
			failureTcsDescription.put(currentTestCaseName, PropertiesFile.tcDescription.get(currentTestCaseName));
			writeToLogFile("Error", execeptionMesage);
		}

		if (!GenericKeywords.windowreadyStateStatus) {
			screenShot("TestFailure" + GenericKeywords.failureNo);
			GenericKeywords.windowreadyStateStatus = true;
		} else {
			captureScreenShot("TestFailure" + GenericKeywords.failureNo);
		}

		if (getConfigProperty("ExecuteRemainingStepsOnFailure(Yes/No)").toUpperCase().contains("YES")) {

			testCaseExecutionStatus = true;
			maxNoFailures++;
			elementLoadWaitTime = Integer.parseInt(getConfigProperty("OverideTimeoutOnFailure"));
			textLoadWaitTime = Integer.parseInt(getConfigProperty("OverideTimeoutOnFailure"));
			pageLoadWaitTime = Integer.parseInt(getConfigProperty("OverideTimeoutOnFailure"));
			implicitlyWaitTime = Integer.parseInt(getConfigProperty("OverideTimeoutOnFailure"));
			if (maxNoFailures == Integer.parseInt(getConfigProperty("MaxNoOfFailurePerTestCase"))) {
				testCaseExecutionStatus = false;
				testFailed();
			}
		} else if (getConfigProperty("ExecuteRemainingStepsOnFailure(Yes/No)").toUpperCase().contains("NO")) {
			testFailed();
		} else {
			writeToLogFile("ERROR", "Invalid options for ExecuteRemainingStepsOnFailure(Yes/No)");
			testFailed();
		}

	}

	public void testStepFailed(String userMessage) {

		testFailure = true;
		GenericKeywords.failureNo++;
		String scrPath = "../../Screenshots";

		if (!GenericKeywords.windowreadyStateStatus) {
			screenShot("TestFailure" + GenericKeywords.failureNo);
			GenericKeywords.windowreadyStateStatus = true;
		} else {
			captureScreenShot("TestFailure" + GenericKeywords.failureNo);
		}

		writeToLogFile("ERROR", userMessage);
		XmlReport("Fail", userMessage, currentExecutionMachineName, currentTestCaseName,
				scrPath + "/TestFailure" + GenericKeywords.failureNo, null);

		if (getConfigProperty("ExecuteRemainingStepsOnFailure(Yes/No)").toUpperCase().contains("YES")) {

			testCaseExecutionStatus = true;
			maxNoFailures++;
			elementLoadWaitTime = Integer.parseInt(getConfigProperty("OverideTimeoutOnFailure"));
			textLoadWaitTime = Integer.parseInt(getConfigProperty("OverideTimeoutOnFailure"));
			pageLoadWaitTime = Integer.parseInt(getConfigProperty("OverideTimeoutOnFailure"));
			implicitlyWaitTime = Integer.parseInt(getConfigProperty("OverideTimeoutOnFailure"));
			if (maxNoFailures == Integer.parseInt(getConfigProperty("MaxNoOfFailurePerTestCase"))) {
				testCaseExecutionStatus = false;
				testFailed();
			}
		} else if (getConfigProperty("ExecuteRemainingStepsOnFailure(Yes/No)").toUpperCase().contains("NO")) {

			testFailed();
		} else {
			writeToLogFile("ERROR", "Invalid options for ExecuteRemainingStepsOnFailure(Yes/No)");
			testFailed();
		}

	}

	public void stepFailed(String report) {

		testFailure = true;
		GenericKeywords.failureNo++;

		XmlReport("Fail", report, currentExecutionMachineName, currentTestCaseName);

		if (getConfigProperty("ExecuteRemainingStepsOnFailure(Yes/No)").toUpperCase().contains("YES")) {

			testCaseExecutionStatus = true;
			maxNoFailures++;
			elementLoadWaitTime = Integer.parseInt(getConfigProperty("OverideTimeoutOnFailure"));
			textLoadWaitTime = Integer.parseInt(getConfigProperty("OverideTimeoutOnFailure"));
			pageLoadWaitTime = Integer.parseInt(getConfigProperty("OverideTimeoutOnFailure"));
			implicitlyWaitTime = Integer.parseInt(getConfigProperty("OverideTimeoutOnFailure"));
			if (maxNoFailures == Integer.parseInt(getConfigProperty("MaxNoOfFailurePerTestCase"))) {
				testCaseExecutionStatus = false;
				testFailed();
			}
		} else if (getConfigProperty("ExecuteRemainingStepsOnFailure(Yes/No)").toUpperCase().contains("NO")) {
			testFailed();
		} else {
			writeToLogFile("ERROR", "Invalid options for ExecuteRemainingStepsOnFailure(Yes/No)");
			testFailed();
		}

	}

	public void manualScreenshot(String errMessage) {
		writeToLogFile("Info", errMessage);
		GenericKeywords.manualScreenshotNo++;
		String scrPath = "../../Screenshots";

		if (!GenericKeywords.windowreadyStateStatus) {
			screenShot("Screenshot" + GenericKeywords.manualScreenshotNo);
			GenericKeywords.windowreadyStateStatus = true;
		} else {
			captureScreenShot("Screenshot" + GenericKeywords.manualScreenshotNo);
		}
		XmlReport("Verify", errMessage, currentExecutionMachineName, currentTestCaseName,
				scrPath + "/Screenshot" + GenericKeywords.manualScreenshotNo, "");

	}

	public void testStepPassed(String errMessage) {
		writeToLogFile("Info", errMessage);
		XmlReport("Pass", errMessage, currentExecutionMachineName, currentTestCaseName);

	}

	public void testStepFailedDB(String errMessage) {
		testFailure = true;
		writeToLogFile("ERROR", errMessage);
		XmlReport("Fail", errMessage, currentExecutionMachineName, currentTestCaseName);

	}

	public void testStepInfo(String errMessage) {
		writeToLogFile("Info", errMessage);
		XmlReport("Info", errMessage, currentExecutionMachineName, currentTestCaseName);

	}

	public void testStepLink(String link, String errMessage) {
		writeToLogFile("INFO", errMessage);
		XmlReport("Link", errMessage, currentExecutionMachineName, currentTestCaseName, link);

	}

	public void reportStart(String testCaseDescription, String machineName) {

		GenericKeywords.writeToLogFile("INFO", "##### Start of Test Case : " + testCaseDescription + " #####");
		XmlReport(testCaseDescription, currentExecutionMachineName, currentTestCaseName);

	}

	public void reportStart() {

		String testCaseDescription = PropertiesFile.tcDescription.get(currentTestCaseName);
		GenericKeywords.writeToLogFile("INFO", "############# Start of Test Case[" + currentTestCaseName + "] : "
				+ testCaseDescription + " #############");
		XmlReport(testCaseDescription, currentExecutionMachineName, currentTestCaseName);

	}
}