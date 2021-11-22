package pages.android;

import iSAFE.ApplicationKeywords;

import baseClass.BaseClass;

public class AndroidTenantRegisterationPage extends ApplicationKeywords {
	private static final String lbl_heading = "Heading#xpath=//android.view.View/android.view.View[@content-desc='Tenant Registration']";
	private static final String btn_register = "Register#xpath=//android.view.View/android.widget.Button[@content-desc='Register']";
	private static final String txt_email = "Email#xpath=//android.widget.EditText[contains(@text,'customemail')]";
	private static final String txt_firstName = "First Name#xpath=//android.widget.EditText[contains(@text,'customname')]";
	private static final String txt_lastName = "Last Name#xpath=//android.widget.EditText[contains(@text,'customlast')]";
	private static final String btn_submit = "Submit#xpath=//android.view.View/android.widget.Button[@content-desc='Submit']";
	private static final String btn_back = "Back#xpath=//android.view.View/android.widget.Button[@content-desc='Back']";
	private static final String btn_backArrow = "Back#xpath=(//android.widget.Button)[1]";
	
	public AndroidTenantRegisterationPage(BaseClass obj) {
		super(obj);
	}

	public void tenantRegisteration(String email, String firstName, String lastName) {
		String tenRegHeading = "//android.view.View/android.view.View[@content-desc='Tenant Registration']";
		try {
			clickOn(btn_register);
			if(isElementPresent(tenRegHeading, 10)) {
				testStepPassed("Application navigated to tenant registration page");
			}else {
				testStepFailed("Application failed to navigate to tenant registration page");
			}
			typeIn(txt_email, email);
			typeIn(txt_firstName, firstName);
			typeIn(txt_lastName, lastName);
			clickOnBackButton();
			clickOn(btn_submit);
		} catch (Exception e) {
			writeToLogFile("Error", e.toString());
			testStepFailed("Tenant registeration failed");
		}
	}
	
	
	public boolean validateRegisteration () {
		String lbl_regSuccess = "//android.view.View/android.view.View[@content-desc='Please check your email to complete registeration']";
		String lbl_alreadyRegistered = "//android.view.View[@content-desc='User with this email already exists']";
		try {
			if(isElementPresent(lbl_regSuccess, 4)) {
				testStepPassed("Tenant registered successfully");
				clickOn(btn_back);
				return true;
			}else if(isElementPresent(lbl_alreadyRegistered, 4)){
				testStepFailed("User already registered");
				clickOn(btn_backArrow);
				return false;
			}else {
				testStepFailed("Failed to register tenant");
				return false;
			}
		} catch (Exception e) {
			writeToLogFile("Error", e.toString());
			testStepFailed("Tenant registeration failed");
			return false;
		}
	}

}
