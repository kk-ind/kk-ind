package pages.android;

import iSAFE.ApplicationKeywords;
import automationFramework.GenericKeywords.browserType;
import baseClass.BaseClass;

public class AndroidLoginPage extends ApplicationKeywords {

	private static final String txt_email = "Email#xpath=//android.widget.EditText[1]";
	private static final String txt_password = "Password#xpath=//android.widget.EditText[2]";
	private static final String btn_login = "Login#xpath=//android.widget.Button[@content-desc='Login']";
	private static final String btn_skip = "Login#xpath=//android.widget.Button[@content-desc='SKIP']";
	private static final String btn_signOut = "Sign Out#xpath=//android.widget.Button[@content-desc='Sign Out']";
	private static final String emailXpath = "//android.widget.EditText[1]";
	private static final String passwordXpath = "//android.widget.EditText[2]";

	public AndroidLoginPage(BaseClass obj) {
		super(obj);
	}

	public void entertenLoginCreds(String email, String password) {
		try {
			clearField(emailXpath);
			typeIn(txt_email, email);
			clearField(passwordXpath);
			typeIn(txt_password, password);
			clickOnBackButton();
			clickOn(btn_login);
		} catch (Exception e) {
			writeToLogFile("Error", e.toString());
			testStepFailed("Failed to enter login credentials");
		}
	}

	public void loginValidation(String scenario) {
		String skipButton = "//android.widget.Button[@content-desc='SKIP']";
		String invalidEmail = "//android.view.View[@content-desc='Email not recognized']";
		String invalidPwd = "//android.view.View[@content-desc='Incorrect password']";
		String welcomeLbl = " //android.view.View[contains(@content-desc,'Welcome')]";

		switch (scenario) {
		case "Positive":
			if (isElementPresent(welcomeLbl, 8) || isElementPresent(skipButton, 15) ) {
				testStepPassed("Tenant login successful");
			} else {
				testStepFailed("Tenant login failed");
			}

			break;
		case "Invalid Email":
			if (isElementPresent(invalidEmail, 8)) {
				testStepPassed("Tenant login failed using invalid email");
			} else {
				testStepFailed("Expected error on invalid email is not displayed");
			}
			break;
		case "Invalid Password":
			if (isElementPresent(invalidPwd, 8)) {
				testStepPassed("Tenant login failed using invalid password");
			} else {
				testStepFailed("Expected error on entering invalid password is not displayed");
			}
			break;
		}
	}

}
