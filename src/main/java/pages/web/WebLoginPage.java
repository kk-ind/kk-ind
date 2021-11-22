package pages.web;

import iSAFE.ApplicationKeywords;
import baseClass.BaseClass;

public class WebLoginPage extends ApplicationKeywords {

	private static final String txt_password = "Password#xpath=(//span[text()='Password']/preceding::input)[2]";
	private static final String btn_register = "Register#xpath=//button[@type='submit']";
	private static final String lbl_heading = "Heading#xpath=//h4[contains(text(),'Registeration')]";
	private static final String lbl_heading2 = "Paragraph#xpath=//p[contains(text(),'registeration')]";
	private static final String btn_signOut = "Sign Out#xpath=//span[contains(text(),'Sign Out')]";
	private static final String txt_email = "Email#xpath=//span[text()='Email Address']/preceding::input";
	private static final String btn_login = "Login#xpath=//button[@type='submit']";
	
	
	public WebLoginPage(BaseClass obj) {
		super(obj);
	}

	public void adminLogin(String email, String password, String adminName) {
		String btn_adminName = "Admin Name#xpath=//span[text()='"+adminName+"']";
		try {
			typeIn(txt_email, email);
			typeIn(txt_password, password);
			clickOn(btn_login);
			if(isElementDisplayed(btn_adminName)) {
				testStepPassed("Admin login successful");	
			}else {
				testStepFailed("Admin login failed");	
			}
		} catch (Exception e) {
			writeToLogFile("Error", e.toString());
			testStepFailed("Admin login failed");
		}
	}
	
	public void tenantSetPassword(String password) {
		try {
			typeIn(txt_password, password);
			clickOn(btn_register);
			if(getText(lbl_heading).contains("Registeration") && getText(lbl_heading2).contains("registeration")) {
				testStepPassed("Tenant password set successful");
				clickOn(btn_signOut);
			}else {
				testStepFailed("Tenant password set is unsuccessful");
			}
		} catch (Exception e) {
			writeToLogFile("Error", e.toString());
			testStepFailed("Failed to set new password to tenant");
		}
	}	

}
