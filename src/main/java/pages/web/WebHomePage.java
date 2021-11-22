package pages.web;

import iSAFE.ApplicationKeywords;
import baseClass.BaseClass;

public class WebHomePage extends ApplicationKeywords {

	private static final String btn_signOut = "Sign Out#xpath=//span[contains(text(),'Sign out')]";
	private static final String lbl_heading = "Heading#xpath=//h1[contains(text(),'Atalo')]";

	public WebHomePage(BaseClass obj) {
		super(obj);
	}

	public void adminSignOut(String adminName) {
		String btn_adminName = "Admin Name#xpath=//span[text()='" + adminName + "']";
		try {
			clickOn(btn_adminName);
			clickOn(btn_signOut);
			if (isElementDisplayed(lbl_heading)) {
				testStepPassed("Successfully signed out of the admin application");
			} else {
				testStepFailed("Sign out Failed");
			}
		} catch (Exception e) {
			writeToLogFile("Error", e.toString());
			testStepFailed("Sign out Failed");
		}
	}
	
	

	
}
