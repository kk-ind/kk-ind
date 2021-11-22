package iSAFE;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.internal.TouchAction;
import automationFramework.APIKeywords;
import automationFramework.Mail;
import baseClass.BaseClass;
//import io.appium.java_client.TouchShortcuts;

public class ApplicationKeywords extends APIKeywords {

	public String randomNumber = null;
	String executionCount = null;

	public ApplicationKeywords(BaseClass obj) {
		super(obj);
	}

	public ApplicationKeywords() {

	}

	/**
	 * @author Mohamed
	 * @apiNote To get the random generated value
	 * @return random generated int value
	 */
	public String getRandomNumber() {

		int randomNumber = (int) Math.round(Math.random() * 1000000);
		String value = Integer.toString(randomNumber);
		return value;
	}

	/**
	 * @author Mohamed
	 * @param xpath
	 * @param timeUnit
	 * @apiNote To check the element is present in DOM
	 * @return Boolean
	 */
	public boolean isElementPresent(String xpath, int timeUnit) {
		driver.manage().timeouts().implicitlyWait(timeUnit, TimeUnit.SECONDS);
		try {
			driver.findElement(By.xpath(xpath));
			return true;
		} catch (NoSuchElementException e) {
			ApplicationKeywords.writeToLogFile("INFO", e.toString());
			return false;
		}
	}

	/**
	 * @author Mohamed
	 * @apiNote Helper method to perform default scroll right to left in mobile
	 *          device
	 */
	public void swipeRighttoLeft() {
		Dimension size = driver.manage().window().getSize();
		System.out.println(size);
		int starty = (int) (size.height * 0.80);
		int endy = (int) (size.width * 0.20);
		int startx = size.width / 2;
		System.out.println("starty = " + starty + " ,endy = " + endy + " , startx = " + startx);
//		((TouchShortcuts) driver).swipe(startx, starty, starty, endy, 3000);

	}

	/**
	 * @author Mohamed
	 * @param locator
	 * @apiNote To clear the field value
	 */
	public void clearField(String locator) {
		try {
			WebElement inputField = driver.findElement(By.xpath(locator));
			inputField.click();
			for (int i = 0; i <= 3; i++) {
				inputField.click();
				inputField.clear();
			}
			writeToLogFile("Info", "Text field value cleard");
		} catch (Exception e) {
			writeToLogFile("Error", e.toString());
		}
	}

	/**
	 * @author Mohamed
	 * @param email
	 * @param password
	 * @apiNote To get tenant registration url to set password and complete
	 *          registration process
	 * @return URL to set password
	 */
	public String getMailContent(String email, String password) {
		String regURL = null;
		Mail mail = new Mail();
		try {
			mail.checkReceivedEMail("Complete your registration to Atalo", email, password);
			writeToLogFile("INFO", mail.eMailBody);
			String test = mail.eMailBody.toString();
			regURL = StringUtils.substringBetween(test, "a href=\"", "\" target=");
			System.out.println("URL to set your password : " + regURL);
		} catch (Exception e) {
			writeToLogFile("Error", e.toString());
			testStepFailed("Mail retrieval Failed");
		}
		return regURL;
	}

	/**
	 * @author Mohamed
	 * @param menu
	 * @apiNote To navigate to all the sub menu with the given submenu parameter
	 */
	public void navigateToMenu(String menu) {
		String btn_menu = menu + " side menu#xpath=(//span[contains(text(),'" + menu + "')])[1]";
		clickOn(btn_menu);
	}

}